package com.test.fixlibrary;

import android.content.Context;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * desc   : FixDexUtils
 */
public class FixDexUtils {

    //修复文件可能有多个
    private static HashSet<File> loadedDex = new HashSet<>();

    //不建议这么写,demo演示用
    static {
        loadedDex.clear();
    }

    public static void loadDexFile(Context context) {
        //获取私有目录
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        //遍历筛选私有目录中的.dex文件
        File[] listFiles = fileDir.listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            //文件名以.dex结尾,且不是主包.dex文件
            if (listFiles[i].getName().endsWith(Constants.DEX_SUFFIX) && !"classes.dex".equalsIgnoreCase(listFiles[i].getName())) {
                loadedDex.add(listFiles[i]);
            }
        }
        //创建自定义的类加载器
        createDexClassLoader(context ,fileDir);
    }

    /**
     * @param context
     * @param fileDir
     * 创建自己的类加载器,加载私有目录的.dex文件,上面已经将修复包中的dex文件copy到私有目录
     */
    private static void createDexClassLoader(Context context, File fileDir) {
        //解压目录
       String optimizedDir = fileDir.getAbsolutePath()+File.separator+"opt_dex";
        File fileOpt = new File(optimizedDir);
        if (!fileOpt.exists()) {
            fileOpt.mkdirs();
        }

        for (File dex : loadedDex) {
            //创建自己的类加载器,临时的
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optimizedDir, null, context.getClassLoader());
            //有一个修复文件,就插装一次
            hotFix(classLoader,context);
        }
    }

    private static void hotFix(DexClassLoader classLoader, Context context) {
        try {
            //获取系统的PathClassLoader类加载器
            PathClassLoader pathClassLoader = (PathClassLoader)context.getClassLoader();
            //获取自己的dexElements数组

            Object myElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            //获取系统的dexElements数组
            Object systemElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathClassLoader));
            //合并数组,并排序,生成一个新的数组
            Object dexElements=ArrayUtils.combineArray(myElements,systemElements);
            //通过反射获取系统的pathList属性
            Object systemPathList = ReflectUtils.getPathList(pathClassLoader);
            //通过反射,将合并后新的dexElements赋值给系统的dexElements
            ReflectUtils.setFieldValue(systemPathList,"dexElements",dexElements);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
