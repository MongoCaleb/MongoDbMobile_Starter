package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.MongoClient;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.userapikey.UserApiKeyCredential;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private StitchAppClient _stitchClient;
    private MongoClient _localClient;
    private RemoteMongoClient _atlasClient;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeUI();
        this.initializeStitch();
    }

    private void initializeUI() {
        mTextMessage = (TextView) findViewById(R.id.dataView);
        final CheckBox cbSync = findViewById(R.id.chkSync);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Button b = (Button) findViewById(R.id.fetchData);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dbName = ((TextView) findViewById(R.id.dbName)).getText().toString();
                String collectionName = ((TextView) findViewById(R.id.collectionName)).getText().toString();
                fetchData(dbName, collectionName, cbSync.isChecked());
            }
        });
    }

    private void initializeStitch() {
        _stitchClient =
                Stitch.initializeDefaultAppClient(this.getString(R.string.stitch_app_id));

        _stitchClient.getAuth().loginWithCredential(new UserApiKeyCredential(this.getString(R.string.userApiKey)))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        _localClient = _stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                        _atlasClient = _stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                    }
                });
    }

    private void fetchData(final String dbName, final String collectionName, final Boolean syncData) {
        RemoteFindIterable cursor = this._atlasClient.getDatabase(dbName).getCollection(collectionName).find();
        cursor.into(new ArrayList<Document>()).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                List<Document> docs = (List<Document>) task.getResult();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(collectionName, docs.toString());
                    String foo = jsonObject.toString(2);
                    mTextMessage.setText(foo);
                } catch (JSONException je) {
                    //TOAST
                }

                if (syncData) {
                    for (Document doc: docs ) {
                        _localClient.getDatabase(dbName).getCollection(collectionName)
                                .insertOne(doc);
                    }

                }
            }
        });
    }
}
