package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RemoteDataFragment extends Fragment {

    public static String remoteData = "-- no remote data downloaded yet --";
    private static TextView tvData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.remote_data, container, false);

        tvData = rootView.findViewById(R.id.remoteDataView);
        tvData.setText(remoteData);
        return rootView;
    }

    public static void RefreshData(){
        if (tvData!=null) tvData.setText(remoteData);
    }
}
