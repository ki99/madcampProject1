package com.example.project1.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.project1.R;
import com.example.project1.Classes.Tel;

import java.util.List;

public class TelAdapter extends BaseAdapter {
    //String TAG = TelAdapter.class.getSimpleName();

    Context context;
    List<Tel> items;
    LayoutInflater inflter;
    ImageButton callButton;
    ImageButton smsButton;


    public TelAdapter(Context context, List<Tel> data) {
        this.context = context;
        this.items = data;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
        //return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
        //return i;
    }
    //i번째 리스트의 view를 관리하는 메소드
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //tel_item을 그려줌.
        view = inflter.inflate(R.layout.tel_item, null);

        TextView txtName = (TextView) view.findViewById(R.id.name);
        TextView txtPhone = (TextView) view.findViewById(R.id.phone);
        callButton = view.findViewById(R.id.callButton);
        smsButton = view.findViewById(R.id.smsButton);

        txtName.setText(items.get(i).getName());
        txtPhone.setText(items.get(i).getPhone());

        ///////////전화버튼 누른면 전화할 수 있도록
        callButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                try{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+ items.get(i).getPhone()));
                    view.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (ActivityNotFoundException e){
                    Log.e("전화걸기", "전화걸기에 실패했습니다.", e);
                }
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                String smsBody = "";
                sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
                sendIntent.putExtra("address", items.get(i).getPhone()); // 받는사람 번호
                sendIntent.setType("vnd.android-dir/mms-sms");
                view.getContext().startActivity(sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        return view;
    }

//    InputStream inputstream;
//    Drawable drawable;
//    private void loadImage(ImageView imageView, String imageName) {
//        try {
//            inputstream = context.getAssets().open("images/" + imageName);
//            drawable = Drawable.createFromStream(inputstream, null);
//            imageView.setImageDrawable(drawable);
//        } catch (Exception ex) {
//            Log.e(TAG, ex.getMessage());
//        }
//    }
}
