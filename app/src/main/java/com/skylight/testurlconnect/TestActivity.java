package com.skylight.testurlconnect;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tangfen on 2017/9/28.
 *
 * @创建者 tangfen
 * @创建时间 2017/9/28 9:53
 * @描述 ${TODO}
 */

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    private TextView titleText;
    private TextView authorText;
    private TextView contentText;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Detail detail= (Detail) msg.obj;
            titleText.setText(detail.getTitle());
            authorText.setText(detail.getAuthor());
            contentText.setText(detail.getContent());

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initView();
        new Thread(){
            @Override
            public void run() {
                initData();
            }
        }.start();

    }


    private void initView(){
        titleText=findViewById(R.id.title_text);
        authorText=findViewById(R.id.author_text);
        contentText=findViewById(R.id.content_text);

    }

    private void initData(){
        try {
            URL           url=new URL("http://www.imooc.com/api/teacher?type=3&cid=1");
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            if(conn.getResponseCode()==200){
                InputStream is=conn.getInputStream();
                byte[] b=new byte[1024];
                int len=0;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=is.read(b))!=-1){
                    baos.write(b,0,len);
                }

                String data=baos.toString();
                Log.e(TAG, "initData: "+"data="+data );
                JSONObject  object  =new JSONObject(data);
                String      object1 =object.getString("data");
                Gson        gson    =new Gson();
               final Detail detail  = gson.fromJson(object1,Detail.class);
                Message msg=mHandler.obtainMessage();
                msg.obj=detail;
                mHandler.sendMessage(msg);
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titleText.setText(detail.getTitle());
                        authorText.setText(detail.getAuthor());
                        contentText.setText(detail.getContent());
                    }
                });



                int        status =object.getInt("status");
                String data1=object.getString("data");
                String msg=object.getString("msg");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // titleText.setText(stat);
                    }
                });*/

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
