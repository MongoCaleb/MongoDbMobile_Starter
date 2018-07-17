package com.mongodb.mongodbmobile_starter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.MongoClient;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.auth.providers.facebook.FacebookCredential;
import com.mongodb.stitch.core.auth.providers.google.GoogleCredential;
import com.mongodb.stitch.core.auth.providers.userapikey.UserApiKeyCredential;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;

public class StitchHandler {

    public static StitchAppClient stitchClient;
    public static MongoClient localClient;
    public static RemoteMongoClient atlasClient;
    private static Context context;

    public StitchHandler(final Context c) {

        //create a Stitch client using the Stitch app ID in
        //the strings.xml file.
        StitchHandler.context = c;
        String stitchAppId = context.getString(R.string.stitch_app_id);
        StitchHandler.stitchClient = Stitch.initializeDefaultAppClient(stitchAppId);
    }

    /**
     * Use this method if your Stitch app is configured to allow
     * anonymous access
     */
    public static void AuthWithAnonymous() {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new AnonymousCredential())
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        } else {
                            //show toast
                            Exception e = task.getException();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Use this method if your Stitch app is configured with
     * API Key authentication.
     * REQUIREMENT: You must store the API Key in your strings.xml file
     * or modify this method accordingly.
     */
    public static void AuthWithApiKey() {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new UserApiKeyCredential(context.getString(R.string.userApiKey)))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        } else {
                            //show toast
                            Exception e = task.getException();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Use this method if your Stitch app is configured with
     * API Key authentication.
     * REQUIREMENT: You must pass the username and password, which will probably
     * come from a custom login UI.
     */
    public static void AuthWithUserPass(String username, String password) {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new UserPasswordCredential(username, password))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        } else {
                            //show toast
                            Exception e = task.getException();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Use this method if your Stitch app is configured with
     * Facebook authentication.
     * See https://docs.mongodb.com/stitch/authentication/facebook/
     * to learn more about configuring FB Auth.
     */
    public static void AuthWithFacebook() {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new FacebookCredential(context.getString(R.string.facebookToken)))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        } else {
                            //show toast
                            Exception e = task.getException();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Use this method if your Stitch app is configured with
     * Facebook authentication.
     * See https://docs.mongodb.com/stitch/authentication/google/
     * to learn more about configuring Google Auth.
     */
    public static void AuthWithGoogle() {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new GoogleCredential(context.getString(R.string.googleAuthCode)))
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        } else {
                            //show toast
                            Exception e = task.getException();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
