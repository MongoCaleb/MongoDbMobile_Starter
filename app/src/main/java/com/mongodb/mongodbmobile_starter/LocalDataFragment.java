package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class LocalDataFragment extends Fragment {

    public static String localData = "-- no local data --";
    private static TextView tvData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.local_data, container, false);


        tvData = rootView.findViewById(R.id.dataView);
        tvData.setText(localData);

        return rootView;
    }

    public static void RefreshData(){
        if (tvData!=null) tvData.setText(localData);
    }

}
