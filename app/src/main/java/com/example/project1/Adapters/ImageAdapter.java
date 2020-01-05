package com.example.project1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext); //항목의 모양
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300)); //이미지 크기 조정
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //이미지 가운데 설정
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageView.setPadding(8, 8, 8, 8);   //여백 설정
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);

        return imageView;
    }

    class ViewHolder{
        ImageView imageView;
    }
    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.image_01, R.drawable.image_02,
            R.drawable.image_03, R.drawable.image_04,
            R.drawable.image_05, R.drawable.image_06,
            R.drawable.image_07, R.drawable.image_08,
            R.drawable.image_09, R.drawable.image_10,
            R.drawable.image_11, R.drawable.image_12,
            R.drawable.image_13, R.drawable.image_14,
            R.drawable.image_15, R.drawable.image_16,
            R.drawable.image_17, R.drawable.image_18,
            R.drawable.image_19, R.drawable.image_20
    };
}
