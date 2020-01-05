package com.example.project1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project1.Adapters.SectionPageAdapter;
import com.example.project1.Classes.myTime;
import com.example.project1.Fragments.WorkTimeFragment;
import com.example.project1.Fragments.ImageFragment;
import com.example.project1.Fragments.TelFragment;
import com.example.project1.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private ViewPager mViewPager;
    String dbName = "sample.db";
    int dbMode = Context.MODE_PRIVATE;
    SQLiteDatabase myDB;


    //SectionPageAdapter에 대한 객체를 생성하여 참조 획득
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = openOrCreateDatabase(dbName,dbMode,null);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);


        //activity_main.xml에서 ViewPager에 대한 참조 획득
        mViewPager = (ViewPager) findViewById(R.id.pager_content);

        //각 탭의 title과 Fragment를 SectionPageAdapter을 거쳐 ViewPager과 연결시켜주는 메소드
        setupViewPager(mViewPager);

        //activity_main에서 TabLayout에 대한 참조 획득
        TabLayout tabLayout = (TabLayout) findViewById(R.id.layout_tab);

        //위에서 세팅한 ViewPager과 TabLayout을 연동시키는 메소드
        tabLayout.setupWithViewPager(mViewPager);

    }


    //SectionPageAdapter에 Fragment들 추가
    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new TelFragment(), "CONTACTS");
        adapter.addFragment(new ImageFragment(), "IMAGES");
        adapter.addFragment(new WorkTimeFragment(myDB), "MAD TIME");
        viewPager.setAdapter(adapter);
    }



}
