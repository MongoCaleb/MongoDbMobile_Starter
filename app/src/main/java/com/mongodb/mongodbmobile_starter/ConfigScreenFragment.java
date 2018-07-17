package com.mongodb.mongodbmobile_starter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreenFragment extends Fragment {
    private ProgressDialog pd = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.config_screen, container, false);

        initializeUI(rootView);
        return rootView;
    }

    private void initializeUI(final View view) {

        final CheckBox cbSync = view.findViewById(R.id.chkSync);

        Button bFetch = view.findViewById(R.id.fetchData);
        bFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbName = ((TextView) view.findViewById(R.id.dbName)).getText().toString();
                String collectionName = ((TextView) view.findViewById(R.id.collectionName)).getText().toString();
                try {
                    fetchData(dbName, collectionName, cbSync.isChecked());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button bLocal = view.findViewById(R.id.showLocalData);
        bLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbName = ((TextView) view.findViewById(R.id.dbName)).getText().toString();
                String collectionName = ((TextView) view.findViewById(R.id.collectionName)).getText().toString();
                try {
                    LoadLocal(dbName, collectionName);
                    MainActivity.bottomNav.setSelectedItemId(R.id.navigation_local);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tvLabel = view.findViewById(R.id.labelStitchId);
        tvLabel.setText("I'm using the Stitch app with ID '" + getString(R.string.stitch_app_id) + "'");
    }

    private void fetchData(final String dbName, final String collectionName, final Boolean syncData) {

        this.pd = ProgressDialog.show(this.getContext(), "Downloading", "Downloading remote data...", true, true);

        RemoteFindIterable cursor = StitchHandler.atlasClient.getDatabase(dbName).getCollection(collectionName).find().limit(30);
        cursor.into(new ArrayList<Document>()).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    List<Document> docs = (List<Document>) task.getResult();

                    try {
                        ObjectMapper om = new ObjectMapper();
                        String pretty = om.writerWithDefaultPrettyPrinter().writeValueAsString(docs);
                        RemoteDataFragment.remoteData = pretty;
                    } catch (JsonProcessingException jpe) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), jpe.toString(), Toast.LENGTH_LONG).show();
                    }

                    if (syncData) {
                        pd.setTitle("Syncing");
                        pd.setMessage("Copying remote data to the local DB.");
                        for (Document doc : docs) {
                            Document query = new Document();
                            query.put("_id", doc.get("_id"));

                            StitchHandler.localClient.getDatabase(dbName).getCollection(collectionName)
                                    .replaceOne(query, doc, new ReplaceOptions().upsert(true));
                        }

                        try {
                            LoadLocal(dbName, collectionName);
                        } catch (JsonProcessingException jpe) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), jpe.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    pd.dismiss();
                    RemoteDataFragment.RefreshData();
                    MainActivity.bottomNav.setSelectedItemId(R.id.navigation_remote);

                } else {
                    pd.dismiss();
                    Exception e = task.getException();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void LoadLocal(String dbName, String collectionName) throws JsonProcessingException{
        FindIterable<Document> cursor =
                StitchHandler.localClient.getDatabase(dbName).getCollection(collectionName).find()
                        .limit(30);

        List<Document> localDocs = cursor.into(new ArrayList<Document>());

        ObjectMapper om = new ObjectMapper();
        String pretty = om.writerWithDefaultPrettyPrinter().writeValueAsString(localDocs);
        LocalDataFragment.localData = pretty;
    }
}