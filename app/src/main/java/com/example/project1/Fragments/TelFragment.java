package com.example.project1.Fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project1.Adapters.TelAdapter;
import com.example.project1.Classes.Tel;
import com.example.project1.R;

import java.util.ArrayList;
import java.util.Collections;

public class TelFragment extends Fragment {
    ViewPager viewPager;
    ListView listView;
    ArrayList<Tel> telList;
    TelAdapter telAdapter;


    public TelFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //fragment_tel을 화면에 띄워줌, 이의 참조 획득
        View view = inflater.inflate(R.layout.fragment_tel, container, false);
        listView = view.findViewById(R.id.listView);
        telList = new ArrayList<Tel>();
        LoadContactsAyscn lca = new LoadContactsAyscn();
        lca.execute();
        return view;
    }


    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<String> contacts = new ArrayList<String>();

            Cursor c = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {

                String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                telList.add( new Tel(contactName, phNumber));

            }
            Collections.sort(telList);
            c.close();
            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);
//            pd.cancel();
//            ll.removeView(loadBtn);

//            TelAdapter telAdapter = new TelAdapter(getActivity().getApplicationContext(), telList);
            telAdapter = new TelAdapter(getActivity().getApplicationContext(), telList);
            listView.setAdapter(telAdapter);
        }

    }

}