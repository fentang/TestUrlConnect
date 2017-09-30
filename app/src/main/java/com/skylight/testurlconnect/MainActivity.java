package com.skylight.testurlconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // private Button click;
    //  private ImageView mImageView;
    //  private ListView mListView;

    @BindView(R.id.test)
    Button click;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.my_listview)
    ListView mListView;

    private List<Information> myList = new ArrayList();
    private ContentAdapter contentAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10001:
                    returnBitmap((String) msg.obj);
                    Log.e(TAG, "handleMessage: " + "msg.obj=" + msg.obj);
                    //    mImageView.setImageBitmap(bitmap);
                    break;
                case 10002:
                    Bitmap bitmap1 = (Bitmap) msg.obj;
                    mImageView.setImageBitmap(bitmap1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

       /* click= (Button) findViewById(R.id.test);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mListView = (ListView) findViewById(R.id.my_listview);*/
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
        new Thread() {
            @Override
            public void run() {
                initImage();
                initData();
            }
        }.start();


        contentAdapter = new ContentAdapter(this, myList);
        contentAdapter.notifyDataSetChanged();
        mListView.setAdapter(contentAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("desc",myList.get(i).getDescription());
                intent.putExtra("bigPic",myList.get(i).getPicBig());
                startActivity(intent);
            }
        });

    }

    private void initImage() {
        try {
            URL               url  = new URL("http://www.imooc.com/api/teacher?type=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                InputStream           is    = conn.getInputStream();
                byte[]                bytes = new byte[1024];
                int                   len   = 0;
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }

                String     output  = baos.toString();
                JSONObject object  = new JSONObject(output);
                String     data    = object.getString("data");
                JSONObject object1 = new JSONObject(data);
                String     pic     = object1.getString("pic2");
              /*  JSONObject object1= (JSONObject) array.get(1);
                String pic= object1.getString("pic2");*/
                Log.e(TAG, "initImage: " + "pic=" + pic);

              /*  Message msg=Message.obtain();
                msg.what=10001;
                msg.obj=pic;
                mHandler.sendMessage(msg);*/
                is.close();
                baos.close();
                returnBitmap(pic);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initData() {
        try {
            URL               url        = new URL("http://www.imooc.com/api/teacher?type=2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (connection.getResponseCode() == 200) {
                InputStream is  = connection.getInputStream();
                byte[]      b   = new byte[1024];
                int         len = 0;
                while ((len = is.read(b)) != -1) {
                    baos.write(b, 0, len);
                }
                JSONObject object = new JSONObject(baos.toString());
                String     data   = object.getString("data");
                Gson       gson   = new Gson();
                ArrayList<Information> list = gson.fromJson(data, new TypeToken<ArrayList<Information>>() {
                }.getType());
                for (int i = 0; i < list.size(); i++) {
                    Information information = list.get(i);
                    //   MyDetail    myDetail    = new MyDetail(information.getId(), information.getDescription(), information.getPicSmall());
                   /* MyDetail myDetail = new MyDetail();
                    myDetail.setId(information.getId());
                    myDetail.setPic(information.getPicSmall());
                    myDetail.setDesc(information.getDescription());*/
                    myList.add(information);
                    Log.e(TAG, "initData: " + "id=" + information.getId() + "desc=" + information.getDescription() + "small=" + information.getPicSmall());
                }

                is.close();
                baos.flush();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void returnBitmap(final String bitmapUrl) {
        final Bitmap[] bitmap = {null};
        new Thread() {
            @Override
            public void run() {
                try {
                    URL               url  = new URL(bitmapUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);
                    is.close();
                    Message msg = Message.obtain();
                    msg.what = 10002;
                    msg.obj = bitmap[0];
                    mHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
