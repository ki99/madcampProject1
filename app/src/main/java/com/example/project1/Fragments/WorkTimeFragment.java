package com.example.project1.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.project1.Activities.WorkTimeActivity;
import com.example.project1.Adapters.WorkTimeAdapter;
import com.example.project1.Classes.myTime;
import com.example.project1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;


public class WorkTimeFragment extends Fragment {

    ArrayList<myTime> myTimeList;
    Date startTime;
    Date endTime;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String startTimeText;
    String endTimeText;
    //    SQLiteDatabase sqliteDB;
    SQLiteDatabase myDB;
    String st; String ed;
    Cursor cursor;
    String title;
    View view;
    ListView worktimeview;
    private long restTime = 0;

    public WorkTimeFragment() { }

    public WorkTimeFragment(SQLiteDatabase md){
//        sqliteDB = sq;
        myDB = md;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //버튼과 리스트 화면에 띄움
        view = inflater.inflate(R.layout.fragment_work_time, container, false);

        //start & end Button, TextView, ListView에 대한 참조 획득
        final ImageButton startButton = (ImageButton) view.findViewById(R.id.startButton);
        final ImageButton endButton = (ImageButton) view.findViewById(R.id.endButton);
        final Button allDeleteButton = (Button) view.findViewById(R.id.allDeleteButton);
        final TextView rn = (TextView)view.findViewById(R.id.Running);
        final TextView st = (TextView)view.findViewById(R.id.Stopped);
        final TextView total = (TextView)view.findViewById(R.id.totalTime);
        worktimeview = (ListView) view.findViewById(R.id.workTimeView);
        //테이블 만들기
        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS TIME_T (TIME CHAR(20), BOOL INTEGER, REST CHAR(20), TITLE CHAR(40), HOUR LONG,MINUTE LONG,SECOND LONG, MEMO CHAR(400))";   myDB.execSQL(sqlCreateTbl);
//        String sqlCreateTbl1 = "CREATE TABLE IF NOT EXISTS TIME_T (TIME CHAR(20), BOOL INTEGER)";       sqliteDB.execSQL(sqlCreateTbl1);

        //{start time, end time} class의 List
        myTimeList = new ArrayList<myTime>();

        //DB의 TABLE 전부 리스트에 추가
        cursor = myDB.rawQuery("SELECT * FROM TIME_T", null);    initialize(cursor);     cursor.close();

        // Button 시작인지 끝인지 정하기 위해 last element의 int를 구하기
        cursor = myDB.rawQuery("SELECT * FROM TIME_T", null);        int lastInt = getLastElementInt(cursor);    cursor.close();

        // 버튼 시작,끝 정하기
        ButtonInitialize(startButton, endButton, lastInt, view, rn, st);

        //리스트에 불러온 DB들 띄우기
        WorkTimeAdapter workTimeAdapter = new WorkTimeAdapter(getActivity(),getActivity().getApplicationContext(), myTimeList, myDB);
        worktimeview.setAdapter(workTimeAdapter);



        setTime(total);



        //StartButton 눌렀을 때의 기능 구현
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    Thread.sleep(350); //1초 대기

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }



                출처: https://jink1982.tistory.com/185 [돼민이]


                startButton.setVisibility(view.GONE); endButton.setVisibility(view.VISIBLE);
                rn.setVisibility(View.VISIBLE); st.setVisibility(View.INVISIBLE);
                long now = System.currentTimeMillis();
                startTime = new Date(now); startTimeText = format.format(startTime);

                // StartTime TABLE에 추가
                String sqlInsert = "INSERT INTO TIME_T (TIME, BOOL) VALUES ('" + startTimeText + "', 0)"; myDB.execSQL(sqlInsert);
                setTime(total);
            }
        });

        //allDeleteButton 구현
        allDeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String myLastString = "myLastString";

                //에러를 고치기 위해 마지막 시간이 START시간인지 파악
                Cursor cs = myDB.rawQuery("SELECT * FROM TIME_T", null);
                int myLastInt = getLastElementInt(cs);
                cs.close();

                //마지막 시간이 START시간이면 그 시간을 myLastString에 저장
                if (myLastInt == 0) {
                    cs = myDB.rawQuery("SELECT * FROM TIME_T", null);
                    myLastString = getLastElementString(cs);
                    cs.close();
                }

                //테이블 삭제 후 재생성
                String sqlDropTbl = "DROP TABLE TIME_T"; myDB.execSQL(sqlDropTbl);
                String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS TIME_T (TIME CHAR(20), BOOL INTEGER, REST CHAR(20), TITLE CHAR(40), HOUR LONG,MINUTE LONG,SECOND LONG, MEMO CHAR(400))"; myDB.execSQL(sqlCreateTbl);

                //새로운 리스트 객체 생성
                myTimeList = new ArrayList<myTime>();

                //시작 누르고 초기화 했을때 시작 시간을 보존
                if (myLastInt == 0) {
                    String sqlInsert = "INSERT INTO TIME_T (TIME, BOOL) VALUES ('" + myLastString + "', 0)";
                    myDB.execSQL(sqlInsert);
                }

                //변경사항 ListView에 적용
                WorkTimeAdapter workTimeAdapter = new WorkTimeAdapter(getActivity(), getActivity().getApplicationContext(), myTimeList, myDB);
                worktimeview.setAdapter(workTimeAdapter);
                setTime(total);
            }
        });

        //endButton 눌렀을 때의 기능 구현
        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {

                    Thread.sleep(350); //1초 대기

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }



                출처: https://jink1982.tistory.com/185 [돼민이]
                startButton.setVisibility(view.VISIBLE); endButton.setVisibility(view.GONE);
                rn.setVisibility(View.INVISIBLE); st.setVisibility(View.VISIBLE);
                long now = System.currentTimeMillis();
                endTime = new Date(now); endTimeText = format.format(endTime);

                // endTime TABLE에 추가


                //START 시간 가져오기
                Cursor cs = myDB.rawQuery("SELECT * FROM TIME_T", null);
                while (cs.moveToNext()) {
                    if (cs.getInt(1) == 0) {
                        startTimeText = cs.getString(0);
                    }
                }
                cs.close();

                final EditText et = new EditText(getContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setTitle("TITLE")
                        .setMessage("TITLE를 입력하세요.")
                        .setCancelable(false)
                        .setView(et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String value = et.getText().toString();

                                title= value;

                                try{
                                    startTime = format.parse(startTimeText);
                                    endTime = format.parse(endTimeText);
                                } catch(ParseException e){
                                    e.printStackTrace();
                                }

                                long diff = endTime.getTime() - startTime.getTime() - restTime*1000*60;
                                long hour = diff/1000/60/60;
                                diff = diff - hour*1000*60*60;
                                long min = diff/1000/60;
                                diff = diff-min*1000*60;
                                long sec = diff/1000;






                                String sqlInsert = "INSERT INTO TIME_T (TIME, BOOL, REST, TITLE, HOUR, MINUTE, SECOND, MEMO) VALUES ('" + endTimeText + "', 1,0, '" + title + "', 0 ,0,0, '')"; myDB.execSQL(sqlInsert);
                                Cursor cs = myDB.rawQuery("SELECT * FROM TIME_T", null);
                                while (cs.moveToNext()) {
                                    if (cs.getString(0).equals(endTimeText)) {
                                        sqlInsert = "UPDATE TIME_T SET HOUR = '" + hour + "' WHERE TIME = '" + endTimeText + "'";myDB.execSQL(sqlInsert);
                                        sqlInsert = "UPDATE TIME_T SET MINUTE = '" + min + "' WHERE TIME = '" + endTimeText + "'";myDB.execSQL(sqlInsert);
                                        sqlInsert = "UPDATE TIME_T SET SECOND = '" + sec + "' WHERE TIME = '" + endTimeText + "'";myDB.execSQL(sqlInsert);

                                    }
                                }
                                cs.close();






                                myTimeList.add(0, new myTime(startTimeText, endTimeText));
                                WorkTimeAdapter workTimeAdapter = new WorkTimeAdapter(getActivity(), getActivity().getApplicationContext(), myTimeList, myDB);
                                worktimeview.setAdapter(workTimeAdapter);
                                setTime(total);
                            }

                        });

                AlertDialog alert = alt_bld.create();

                alert.show();








                //List에 추가해주고 listView에 적용
//                myTimeList.add(0, new myTime(startTimeText, endTimeText));
//                WorkTimeAdapter workTimeAdapter = new WorkTimeAdapter(getActivity().getApplicationContext(), myTimeList);
//                worktimeview.setAdapter(workTimeAdapter);
                setTime(total);
            }
        });

        //리스트 클릭했을 때 액티비티로 들어가는 기능 구현
        worktimeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WorkTimeActivity.class);
                String tempstartTimeText = myTimeList.get(position).getstartTime(); String tempendTimeText = myTimeList.get(position).getEndTime();
                intent.putExtra("tempstartTimeText", tempstartTimeText); intent.putExtra("tempendTimeText", tempendTimeText);

                startActivity(intent);

                WorkTimeAdapter workTimeAdapter = new WorkTimeAdapter(getActivity(), getActivity().getApplicationContext(), myTimeList, myDB);
                worktimeview.setAdapter(workTimeAdapter);
            }
        });
        workTimeAdapter = new WorkTimeAdapter(getActivity(), getActivity().getApplicationContext(), myTimeList, myDB);
        worktimeview.setAdapter(workTimeAdapter);
        setTime(total);
        return view;
    }


    public void setTime(TextView total) {
        Cursor cs = this.myDB.rawQuery("SELECT * FROM TIME_T", null);
        long setHour = 0;
        long setMin = 0;
        long setSec = 0;
        while (cs.moveToNext()) {
            setHour += cs.getLong(4);
            setMin += cs.getLong(5);
            setSec += cs.getLong(6);

        }
        cs.close();
        if (abs( setSec) > 59 ){
            long quotient = setSec/60;
            setMin += quotient;
            setSec = setSec - (60 * quotient);
        }
        if (abs( setMin) > 59 ){
            long quotient = setMin/60;
            setHour += quotient;
            setMin = setMin - (60 * quotient);
        }
        if(setHour == 0 ){
            if(setMin == 0)
                total.setText(setSec +"초");
            else
                total.setText(setMin+"분  " + setSec +"초");
        }
        else {
            total.setText(setHour+ "시  " + setMin+"분  " + setSec +"초");
        }
    }



/*        void show(final String endtime1)
        {

            final EditText edittext = new EditText(getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("TITLE을 수정하세요.");
            builder.setMessage("Title");
            builder.setView(edittext);
            builder.setPositiveButton("입력",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String showText = edittext.getText().toString();
                            String sqlInsert = "UPDATE TIME_T SET TITLE = '" + showText + "' WHERE TIME = '" + endtime1 + "'";
                            myDB.execSQL(sqlInsert);
                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        }
*/
    public String initialize(Cursor cursor) {
        int myint = 0;
        while (cursor.moveToNext()) {
            if (cursor.getInt(1) == 0) {
                st = cursor.getString(0);
                myint = 1;
            }
            if (cursor.getInt(1) == 1) {
                ed = cursor.getString(0);
                myTimeList.add(0, new myTime(st, ed));
                myint = 2;
            }
        }
        if (myint == 0) {return "Nothing in the Table";}
        else if (myint == 1){return st;}
        else {return ed;}
    }

    public int getLastElementInt(Cursor cs) {
        int myint = 0;
        while (cs.moveToNext()) {

            if (cs.getInt(1) == 0) {
                myint = 1;
            }
            if (cs.getInt(1) == 1) {
                myint = 2;
            }
        }
        if (myint == 0) {return -1;}
        else if (myint == 1){return 0;}
        else {return 1;}
    }

    public String getLastElementString(Cursor cs) {
        int myint = 0;
        while (cs.moveToNext()) {

            if (cs.getInt(1) == 0) {
                myint = 1;
                st = cs.getString(0);
            }
            if (cs.getInt(1) == 1) {
                myint = 2;
                ed = cs.getString(0);
            }
        }
        if (myint == 0) {return "None";}
        else if (myint == 1){return st;}
        else {return ed;}
    }

    public void ButtonInitialize(ImageButton startButton, ImageButton endButton,int lastInt, View view, TextView rn, TextView st) {
        if (lastInt == 0) {
            startButton.setVisibility(view.GONE);
            endButton.setVisibility(view.VISIBLE);
            rn.setVisibility(View.VISIBLE);
            st.setVisibility(View.INVISIBLE);
        } else {
            startButton.setVisibility(view.VISIBLE);
            endButton.setVisibility(view.GONE);
            rn.setVisibility(View.INVISIBLE);
            st.setVisibility(View.VISIBLE);
        }
    }

}