package com.test.fixlibrary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * desc   : FileUtils
 */
public class FileUtils {

    /**
     * copy文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(File sourceFile, File targetFile){
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            BufferedInputStream inBuffer = new BufferedInputStream(fileInputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            BufferedOutputStream outBuffer = new BufferedOutputStream(fileOutputStream);

            byte[] bytes = new byte[1024 * 10];
            int length;
            while ((length = inBuffer.read(bytes))!=-1){
                outBuffer.write(bytes,0,length);
            }
            outBuffer.flush();

            //关流
            fileInputStream.close();
            inBuffer.close();
            fileOutputStream.close();
            outBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
