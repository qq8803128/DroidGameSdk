package droid.game.android.oaid;

import android.content.Context;
import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;
import droid.game.common.device.OaidProvider;
import droid.game.common.log.LogRecord;

public class AndroidOaidProvider {
    public static void init(Context context) {
        try {
            JLibrary.InitEntry(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//初始化
    }

    public static void requestOaid(Context context){
        try {
            final long durationStart = System.currentTimeMillis();

            int errorCode = MdidSdkHelper.InitSdk(context, true, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean b, IdSupplier idSupplier) {
                    if (idSupplier == null) {
                        return;
                    }
                    OaidProvider.init(
                            idSupplier.isSupported(),
                            idSupplier.getOAID(),
                            idSupplier.getAAID(),
                            idSupplier.getVAID()
                    );
                    long durationEnd = System.currentTimeMillis();
                    LogRecord.e("耗时：" + (durationEnd - durationStart) + "毫秒");
                }
            });

            if (errorCode == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {
                LogRecord.e("获取OAID：" + "不支持的设备");
            } else if (errorCode == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {
                LogRecord.e("获取OAID：" + "加载配置文件出错");
            } else if (errorCode == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {
                LogRecord.e("获取OAID：" + "不支持的设备厂商");
            } else if (errorCode == ErrorCode.INIT_ERROR_RESULT_DELAY) {
                LogRecord.e("获取OAID：" + "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程");
            } else if (errorCode == ErrorCode.INIT_HELPER_CALL_ERROR) {
                LogRecord.e("获取OAID：" + "反射调用出错");
            } else {
                LogRecord.e("获取OAID：" + "获取成功");
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
