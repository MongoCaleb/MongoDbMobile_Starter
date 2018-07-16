package com.mongodb.mongodbmobile_starter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.MongoClient;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.userapikey.UserApiKeyCredential;

public class StitchHandler {

    public static StitchAppClient stitchClient;
    public static MongoClient localClient;
    public static RemoteMongoClient atlasClient;

    public StitchHandler(Context c){

        String stitchAppId = c.getString(R.string.stitch_app_id);
        StitchHandler.stitchClient = Stitch.initializeDefaultAppClient(stitchAppId);

        //Change the following if you want to use a different Auth mechanism
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new UserApiKeyCredential(c.getString(R.string.userApiKey)))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()){
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        }else {
                            //show toast
                            Exception e = task.getException();
                        }

                    }
                });
    }

}
