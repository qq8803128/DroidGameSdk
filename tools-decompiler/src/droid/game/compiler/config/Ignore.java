package droid.game.compiler.config;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static droid.game.compiler.util.FileUtils.getConfigDir;
import static droid.game.compiler.util.FileUtils.json;

public class Ignore {
    private static Map<String, Data> mIgnores;

    public static boolean isIgnore(File file) {
        if (mIgnores == null) {
            List<Data> data = json(getConfigDir(), "ignore.json", new TypeToken<List<Data>>() {
            }.getType());
            mIgnores = new HashMap<>();
            for (Data d : data) {
                mIgnores.put(d.name, d);
            }
        }

        /*if (mIgnores.keySet().contains(file.getName())){
            Data data = mIgnores.get(file.getName());
            if ((data.file && file.isFile())|| (!data.file && file.isDirectory())){
                return true;
            }
        }*/
        for (String key : mIgnores.keySet()) {
            Data data = mIgnores.get(key);
            if (data.eq){
                if (mIgnores.keySet().contains(file.getName()) && ((data.file && file.isFile()) || (!data.file && file.isDirectory()))){
                    return true;
                }
            }else {
                if (file.getName().startsWith(key) && ((data.file && file.isFile()) || (!data.file && file.isDirectory()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Data {
        private String name;
        private boolean file;
        private boolean eq;
    }
}
