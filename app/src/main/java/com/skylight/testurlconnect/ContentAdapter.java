package com.skylight.testurlconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by tangfen on 2017/9/28.
 *
 * @创建者 tangfen
 * @创建时间 2017/9/28 16:37
 * @描述 ${TODO}
 */

public class ContentAdapter extends BaseAdapter {
    private static final String TAG = "ContentAdapter";
    private Context mContext;
    private List<Information> mList;

    public ContentAdapter(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        Log.e(TAG, "getCount: " + mList.size());
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        Log.e(TAG, "getItem: " + mList.get(i));
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mList == null ? 0 : i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.content_item, null);
            holder.mImageView = view.findViewById(R.id.image_item);
            holder.mTextView = view.findViewById(R.id.text_item);
            holder.descView = view.findViewById(R.id.desc_text);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mImageView.setImageBitmap(revertBitmap(mList.get(i).getPicSmall()));
        holder.mTextView.setText(mList.get(i).getId() + "");
        holder.descView.setText(mList.get(i).getDescription());

        return view;
    }

    public static Bitmap revertBitmap(final String bitmapUrl) {
        final Bitmap[] bitmap = {null};
        new Thread() {
            @Override
            public void run() {
                try {
                    URL               url  = new URL(bitmapUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(0);
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        return bitmap[0];

    }
}

class ViewHolder {
    public TextView mTextView;
    public ImageView mImageView;
    public TextView descView;
}
