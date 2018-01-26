package com.aiqing.client;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aiqing.newssdk.SDKHelper;
import com.aiyou.toolkit.common.LogUtils;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        SDKHelper.open(this);
    }

    long start = System.currentTimeMillis();
    long time = System.nanoTime();
    int count = 0;
    int musicCount = 0;
    ArrayList<File> list = new ArrayList<>();
    private int upLimit = 8000;

    public void scanDir(File dirFile) {
        long start = System.currentTimeMillis();
        long time = System.nanoTime();
        int count = 0;
        int musicCount = 0;
//        File dirFile = new File(dirString);
        list.add(dirFile);
        while (list.size() > 0) {
            File dir = list.remove(0);
            if (dir == null) {
                continue;
            }
            File files[] = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                } else {
                    count++;
                    if (file.getAbsolutePath().endsWith("mp3")) {
//                        Log.e("", "这是音乐文件");
                        musicCount++;
                    }
                }
            }
        }
        long end = System.nanoTime();
        LogUtils.e("cost time=" + (end - time) / 1000000 + ";musicCount=" + musicCount + ";totalCount=" + count + ";spend=" + (System.currentTimeMillis() - start));
    }

    @Override
    protected void onResume() {
        super.onResume();
        final File path = Environment.getExternalStorageDirectory();

        if (!EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "文件权限",
                    RC_CAMERA_AND_LOCATION, WRITE_EXTERNAL_STORAGE);
        } else {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    FileSizeCalc fileSizeCalc = new FileSizeCalc();
                    fileSizeCalc.getFileSize(path);
//                    ScanFileManager scanFileManager = new ScanFileManager(path);
//                    scanFileManager.scan();
                    scanDir(path);
                }
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
//        RxBus.getDefault().post();

    }

    private static final int RC_CAMERA_AND_LOCATION = 1;

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
//                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    public void open(View v) {
        SDKHelper.open(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
