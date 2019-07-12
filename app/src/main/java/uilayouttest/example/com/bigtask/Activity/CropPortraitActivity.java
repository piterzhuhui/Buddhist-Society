package uilayouttest.example.com.bigtask.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Views.ClipViewLayout;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 朱慧 on 18-6-10.
 */

public class CropPortraitActivity extends AppCompatActivity {

    private ClipViewLayout clipViewLayout;
    private TextView tv_cro_portrait_ok, tv_cro_portrait_cancel;

    private String TAG = "CropPortraitActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        setContentView(R.layout.activity_crop_portrait);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        clipViewLayout = (ClipViewLayout) findViewById(R.id.clipView_layout);
        tv_cro_portrait_ok = (TextView) findViewById(R.id.tv_cro_portrait_ok);
        tv_cro_portrait_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateUriAndReturn();
            }
        });
        tv_cro_portrait_cancel = (TextView) findViewById(R.id.tv_cro_portrait_cancel);
        tv_cro_portrait_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {

            //Bitmap过大，不能用Bundle进行传输，故使用Uri来代替
            Bundle bundle = intent.getExtras();
            Uri bmpUri = (Uri) bundle.getParcelable("bitmapUri");
            if (bmpUri == null) {
                //由于保存图片需要时间，所以，直接在新的Activity里进行读取
                bmpUri = Uri.fromFile(new File(getCacheDir(), "user_portrait_temp.jpg"));
            }
            clipViewLayout.setImageSrc(bmpUri);
        }

    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
        zoomedCropBitmap = clipViewLayout.clip();
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "user_portrait.jpg"));
        Log.i(TAG, getCacheDir().getAbsolutePath());
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent(CropPortraitActivity.this, ModifyPortraitActivity.class);
            intent.putExtra("portrait", "sucess");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }


}
