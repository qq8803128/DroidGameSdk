/*
 * Copyright (C) 2017 Beijing Didi Infinity Technology and Development Co.,Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.game.virtualapk.internal;

import droid.game.plugin.manager.PluginManager;

/**
 * Created by renyugang on 16/8/15.
 */
public class Constants {
    public static String KEY_IS_PLUGIN = "isPlugin";
    public static String KEY_TARGET_PACKAGE = "target.package";
    public static String KEY_TARGET_ACTIVITY = "target.activity";
    
    
    public static String TAG = "VA";
    public static String TAG_PREFIX = TAG + ".";

    public static String OPTIMIZE_DIR(){ return PluginManager.manager().getConfiguration().getOptimizeDir();}
    public static String NATIVE_DIR(){return PluginManager.manager().getConfiguration().getNativeDir();}

    public static boolean COMBINE_RESOURCES() {return PluginManager.manager().getConfiguration().isCombineResources();}
    public static boolean COMBINE_CLASSLOADER() {return  PluginManager.manager().getConfiguration().isCombineClassLoader();}
    public static boolean DEBUG(){return PluginManager.manager().getConfiguration().isDebug();}
    
    public static int MAX_COUNT_SINGLETOP() {return  PluginManager.manager().getConfiguration().getMaxSingleTopCount();}
    public static int MAX_COUNT_SINGLETASK() {return  PluginManager.manager().getConfiguration().getMaxSingleTaskCount();}
    public static int MAX_COUNT_SINGLEINSTANCE() {return  PluginManager.manager().getConfiguration().getMaxSingleInstanceCount();}

    public static String corePackage() {return  PluginManager.manager().getConfiguration().getCorePackage();}
    public static String STUB_ACTIVITY_STANDARD() {return  PluginManager.manager().getConfiguration().getStubStandardActivityFormat();}
    public static String STUB_ACTIVITY_SINGLETOP() {return  PluginManager.manager().getConfiguration().getStubSingleTopActivityFormat();}
    public static String STUB_ACTIVITY_SINGLETASK() {return  PluginManager.manager().getConfiguration().getStubSingleTaskActivityFormat();}
    public static String STUB_ACTIVITY_SINGLEINSTANCE() {return  PluginManager.manager().getConfiguration().getStubSingleInstanceActivityFormat();}

    public static String FILE_NAME() {return  PluginManager.manager().getConfiguration().getFileName();}
}
