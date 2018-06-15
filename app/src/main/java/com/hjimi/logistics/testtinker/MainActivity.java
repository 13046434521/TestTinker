package com.hjimi.logistics.testtinker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.library.TinkerLoadLibrary;
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

public class MainActivity extends AppCompatActivity {
    private ImageView mImg;
    private Button installBtn;
    private Button unInstallBtn;
    private Button infoBtn;
    private Button hackBtn;
    private Button nohackBtn;
    private Button armeabiBtn;
    private Button v7aBtn;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        installBtn=findViewById(R.id.btn_load_install);
        unInstallBtn=findViewById(R.id.btn_load_unInstall);
        mImg = findViewById(R.id.iv_main_test);
        hackBtn = findViewById(R.id.btn_load_hack);
        nohackBtn = findViewById(R.id.btn_load_nohack);
        armeabiBtn = findViewById(R.id.btn_load_armeabi);
        v7aBtn = findViewById(R.id.btn_load_armeabiv7a);
        infoBtn=findViewById(R.id.btn_load_info);
        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                }
//                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                Toast.makeText(MainActivity.this,"安装补丁",Toast.LENGTH_SHORT).show();
            }
        });

        unInstallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerInstaller.cleanPatch(MainActivity.this);
                Toast.makeText(MainActivity.this,"卸载补丁",Toast.LENGTH_SHORT).show();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,welcomeImi(),Toast.LENGTH_SHORT).show();
            }
        });

        hackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CPU_ABI = android.os.Build.CPU_ABI;
                // 将tinker library中的 CPU_ABI架构的so 注册到系统的library path中。
                TinkerLoadLibrary.installNavitveLibraryABI(MainActivity.this, CPU_ABI);
            }
        });
        nohackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CPU_ABI = android.os.Build.CPU_ABI;
                boolean a=TinkerLoadLibrary.loadLibraryFromTinker(getApplicationContext(), "lib/" + CPU_ABI, "native-lib");
                Toast.makeText(MainActivity.this,"so库安装："+a,Toast.LENGTH_SHORT).show();
            }
        });
        armeabiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerLoadLibrary.loadArmLibrary(getApplicationContext(), "native-lib");
            }
        });
        v7aBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerApplicationHelper.loadArmV7aLibrary(TestTinkerLike.getmTestTinkerLike(), "native-lib");
            }
        });

        mImg.setImageResource(R.mipmap.test);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String getImi();

    public native String getNews();

    public native String welcomeImi();
}
