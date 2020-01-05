package com.example.project1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import android.app.AlertDialog;

import com.example.project1.Activities.MainActivity;
import com.example.project1.Classes.myTime;
import com.example.project1.Fragments.WorkTimeFragment;
import com.example.project1.R;
import com.example.project1.Classes.Tel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

public class WorkTimeAdapter extends BaseAdapter  {
    //String TAG = TelAdapter.class.getSimpleName();

    Context context;
    List<myTime> items;
    LayoutInflater inflter;
    SQLiteDatabase myDB;
    private String showText;
    /////////
    Activity activity;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private OnItemClick mCallback;

    public WorkTimeAdapter(Activity activity, Context context, List<myTime> data, SQLiteDatabase mydb) {
        this.activity = activity;
        this.context = context;
        this.items = data;
        inflter = (LayoutInflater.from(context));
        myDB = mydb;
    }

    public interface OnItemClick {
        void onClick (ListView listview);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
        //return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
        //return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.time_item, null);
        TextView stt = (TextView)view.findViewById(R.id.stt);
        TextView edt = (TextView)view.findViewById(R.id.edt);
        TextView ttl = (TextView)view.findViewById(R.id.ttl);
        ImageButton ntBtn = (ImageButton)view.findViewById(R.id.newTitleButton);
        Button dltBtn = (Button)view.findViewById(R.id.deleteButton);

        String myedt = items.get(i).getEndTime();
        String mytxt = "";
        Cursor cs = myDB.rawQuery("SELECT * FROM TIME_T", null);
        while (cs.moveToNext()) {
            if (cs.getString(0).equals(myedt)) {
                mytxt = cs.getString(3);
            }
        }
        cs.close();
        ttl.setText("" +mytxt);
        stt.setText("시작  : " +items.get(i).getstartTime());
        edt.setText("끝      : " +items.get(i).getEndTime());

        ntBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                final EditText edittext = new EditText(activity);


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("TITLE을 수정하세요.");
                builder.setMessage("Title");
                builder.setView(edittext);
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String showText = edittext.getText().toString();
                                String sqlInsert = "UPDATE TIME_T SET TITLE = '" + showText + "' WHERE TIME = '" + items.get(i).getEndTime() + "'";
                                myDB.execSQL(sqlInsert);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }


        });

        dltBtn.setOnClickListener(new Button.OnClickListener() {
        public void onClick(View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("삭제 하시겠습니까?");
            builder.setMessage("진짜루?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String sqlDelete = "DELETE FROM TIME_T WHERE TIME = '" + items.get(i).getEndTime() + "'" ;
                            myDB.execSQL(sqlDelete) ;
                            sqlDelete = "DELETE FROM TIME_T WHERE TIME = '" + items.get(i).getstartTime() + "'" ;
                            myDB.execSQL(sqlDelete) ;
                            items.remove(i);
                            notifyDataSetChanged();



                        }
                    });
            builder.setNegativeButton("아니요",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        }
        });

        return view;
    }

}