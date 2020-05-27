package com.test.tinkerdemo.activitys;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.fixlibrary.Constants;
import com.test.fixlibrary.FileUtils;
import com.test.fixlibrary.FixDexUtils;
import com.test.tinkerdemo.BaseActivity;
import com.test.tinkerdemo.R;
import com.test.tinkerdemo.err.Calculate;

import java.io.File;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btExe = findViewById(R.id.bt_exe);
        Button btFix = findViewById(R.id.bt_fix);


        btExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟异常
                Calculate calculate = new Calculate();
                Toast.makeText(SecondActivity.this, "计算结果: "+calculate.calculator(),Toast.LENGTH_LONG).show();
            }
        });


        btFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerms();
            }
        });
    }


    private void requestPerms() {
        //权限,简单处理下
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
            String[] perms= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms,200);
            }else {
                update();
            }
        }
    }

    private void update() {
        //将下载的修复包,复制到私有目录,解压从.dex文件中取到对应的.class文件
        //从sd卡取修复包
        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);
        //目标文件
        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + Constants.DEX_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
            Log.e("update","删除原有dex文件(已使用的)");
        }
        //将SD卡中的修复包copy到私有目录
        FileUtils.copyFile(sourceFile,targetFile);
        Log.e("update","copy完成");
        FixDexUtils.loadDexFile(this);
    }
}
