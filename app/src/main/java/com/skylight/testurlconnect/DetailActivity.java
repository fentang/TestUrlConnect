package com.skylight.testurlconnect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tangfen on 2017/9/30.
 *
 * @创建者 tangfen
 * @创建时间 2017/9/30 9:53
 * @描述 ${TODO}
 */

public class DetailActivity extends Activity {
    @BindView(R.id.my_image)
    ImageView myImage;
    @BindView(R.id.my_textview)
    TextView mTextView;
    private Handler mHandler=new Handler();
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        ButterKnife.bind(this);

        Intent       intent =getIntent();
        final String bigPic =intent.getStringExtra("bigPic");
        String       desc   =intent.getStringExtra("desc");
        revertUrlToPic(bigPic);
        mTextView.setText(desc);

    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            myImage.setImageBitmap(mBitmap);
        }
    };
    private void revertUrlToPic(final String picUrl){
        final Bitmap[] bitmap = {null};
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(picUrl);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is=connection.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);
                    mBitmap=bitmap[0];
                    mHandler.post(mRunnable);
                    is.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   mHandler=null;
    }
}
