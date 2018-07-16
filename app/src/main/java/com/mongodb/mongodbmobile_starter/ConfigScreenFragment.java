package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;

import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreenFragment extends Fragment {

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

        Button b = (Button) view.findViewById(R.id.fetchData);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbName = ((TextView) view.findViewById(R.id.dbName)).getText().toString();
                String collectionName = ((TextView) view.findViewById(R.id.collectionName)).getText().toString();
                fetchData(dbName, collectionName, cbSync.isChecked());
            }
        });
    }

    private void fetchData(final String dbName, final String collectionName, final Boolean syncData) {
        RemoteFindIterable cursor = StitchHandler.atlasClient.getDatabase(dbName).getCollection(collectionName).find();
        cursor.into(new ArrayList<Document>()).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    List<Document> docs = (List<Document>) task.getResult();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        // jsonObject.put(collectionName, docs.toString());
                        ObjectMapper om = new ObjectMapper();
                        String pretty = om.writerWithDefaultPrettyPrinter().writeValueAsString(docs);
                        RemoteDataFragment.remoteData = pretty;
                    } catch (JsonProcessingException je) {
                        //TOAST
                    }

                    if (syncData) {
                        for (Document doc : docs) {
                            Document query = new Document();
                            query.put("_id", doc.get("_id"));

                            StitchHandler.localClient.getDatabase(dbName).getCollection(collectionName)
                                    .replaceOne(query, doc, new ReplaceOptions().upsert(true));
                        }

                        FindIterable<Document> cursor =
                                StitchHandler.localClient.getDatabase(dbName).getCollection(collectionName).find();

                        List<Document> localDocs = cursor.into(new ArrayList<Document>());
                        try {
                            ObjectMapper om = new ObjectMapper();
                            String pretty = om.writerWithDefaultPrettyPrinter().writeValueAsString(localDocs);
                            LocalDataFragment.localData = pretty;
                        } catch (JsonProcessingException je) {
                            //TOAST
                        }

                    }

                   /* Fragment fragment = new RemoteDataFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.configLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

                } else {
                    Exception e = task.getException();
                    //TOAST
                }
            }
        });
    }
}