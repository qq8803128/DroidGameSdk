package droid.game.common.http;

import android.os.AsyncTask;
import droid.game.open.source.okhttp3.OkHttpClient;
import droid.game.open.source.okhttp3.Response;

import java.io.*;


public class DownloadManager {
    public static DownloadManager manager() {
        return Holder.holder;
    }

    private OkHttpClient mOkHttpClient;
    private File mFile;
    private String mUrl;
    private DownloadListener mDownloadListener;

    private DownloadManager() {
        super();

        createOkHttpClient();
    }

    private void createOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .build();
        }
    }

    public DownloadManager file(File file) {
        mFile = file;
        return this;
    }

    public DownloadManager url(String url) {
        mUrl = url;
        return this;
    }

    public DownloadManager listener(DownloadListener listener) {
        mDownloadListener = listener;
        return this;
    }

    public void enqueue() {
        new AsyncTask<Void, DownloadInfo, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                long contentLength = getContentLength(mUrl);
                if (contentLength > 0) {
                    publishProgress(new DownloadInfo(DownloadInfo.ON_START, new Object[]{contentLength}));
                    download(contentLength);
                } else {
                    publishProgress(new DownloadInfo(DownloadInfo.ON_ERROR, new Object[]{new Throwable("get download file length " + contentLength + " error")}));
                }
                return null;
            }

            protected void download(final long contentLength) {
                long range = 0;
                if (mFile != null && mFile.exists() && mFile.isFile()) {
                    range = mFile.length();
                }

                if (contentLength == range) {
                    publishProgress(new DownloadInfo(DownloadInfo.ON_COMPLETED, new Object[]{}));
                    return;
                }

                droid.game.open.source.okhttp3.Request request = new droid.game.open.source.okhttp3.Request.Builder()
                        .addHeader("RANGE", "bytes=" + range + "-")  //断点续传要用到的，指示下载的区间
                        .url(mUrl)
                        .build();

                InputStream is = null;
                RandomAccessFile savedFile = null;

                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (response != null) {
                        is = response.body().byteStream();
                        savedFile = new RandomAccessFile(mFile, "rw");
                        savedFile.seek(range);//跳过已经下载的字节
                        byte[] b = new byte[1024];
                        int total = 0;
                        int len;
                        while ((len = is.read(b)) != -1) {
                            total += len;
                            savedFile.write(b, 0, len);
                            //计算已经下载的百分比
                            final long current = total + range;
                            publishProgress(new DownloadInfo(DownloadInfo.ON_PROGRESS_CHANGED, new Object[]{contentLength, current}));
                        }
                        response.body().close();

                        publishProgress(new DownloadInfo(DownloadInfo.ON_COMPLETED, new Object[]{}));
                        return;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    closeIo(is);
                    closeIo(savedFile);
                } finally {
                    closeIo(is);
                    closeIo(savedFile);
                }
            }

            @Override
            protected void onProgressUpdate(DownloadInfo... values) {
                super.onProgressUpdate(values);
                switch (values[0].mState) {
                    case DownloadInfo.ON_START:
                        mDownloadListener.onStart((Long) values[0].mArg[0]);
                        break;
                    case DownloadInfo.ON_PROGRESS_CHANGED:
                        mDownloadListener.onProgressChanged((Long) values[0].mArg[0],(Long) values[0].mArg[1]);
                        break;
                    case DownloadInfo.ON_ERROR:
                        mDownloadListener.onError((Throwable) values[0].mArg[0]);
                        break;
                    case DownloadInfo.ON_COMPLETED:
                        mDownloadListener.onCompleted();
                        break;
                }
            }
        }.execute();
    }

    public final boolean execute() {
        boolean result = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            droid.game.open.source.okhttp3.Request request = new droid.game.open.source.okhttp3.Request.Builder().url(mUrl).build();
            Response response = mOkHttpClient.newCall(request).execute();
            byte[] buf = new byte[2048];
            int len = 0;
            if (mFile.exists() && mFile.isFile()) {
                mFile.delete();
            }
            File temp = new File(mFile.getAbsolutePath() + ".tmp");
            if (temp.exists() && temp.isFile()) {
                temp.delete();
            }
            is = response.body().byteStream();
            fos = new FileOutputStream(temp);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            temp.renameTo(mFile);
            result = true;
        } catch (Throwable e) {
            e.printStackTrace();
            closeIo(fos);
            closeIo(is);
        } finally {
            closeIo(fos);
            closeIo(is);
        }
        return result;
    }

    private final void closeIo(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        droid.game.open.source.okhttp3.Request request = new droid.game.open.source.okhttp3.Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static class Holder {
        private final static DownloadManager holder = new DownloadManager();
    }

    private static class DownloadInfo {
        static final int ON_START = 0;
        static final int ON_PROGRESS_CHANGED = 1;
        static final int ON_ERROR = 2;
        static final int ON_COMPLETED = 3;
        int mState;
        Object[] mArg;

        public DownloadInfo(int state, Object[] arg) {
            super();
            mState = state;
            mArg = arg;

        }
    }

    public interface DownloadListener {
        void onStart(long totalLength);

        void onProgressChanged(long totalLength, long currentLength);

        void onError(Throwable e);

        void onCompleted();
    }
}
