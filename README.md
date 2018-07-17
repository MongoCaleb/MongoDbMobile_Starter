# MongoDbMobile_Starter
An Android starter project for working with MongoDB Mobile &amp; Stitch.

# Requirements
For a list of platforms supported by MongoDB Mobile, see the 
[MongoDB Mobile docs](https://docs.mongodb.com/stitch/mongodb/mobile/mobile-overview/).

# Getting Started

1. Clone or download this repo.
2. Copy the MongoDB Mobile tarball from one of the following locations:

   | Platform    | Link |
   |-------------|------|
   | x86_64      | https://s3.amazonaws.com/mciuploads/mongodb-mongo-v4.0/embedded-sdk/embedded-sdk-android-x86_64-latest.tgz |
   | arm64-v8a   | https://s3.amazonaws.com/mciuploads/mongodb-mongo-v4.0/embedded-sdk/embedded-sdk-android-arm64-latest.tgz  |
   | armeabi-v7a | https://s3.amazonaws.com/mciuploads/mongodb-mongo-v4.0/embedded-sdk/embedded-sdk-android-arm32-latest.tgz  |


3. Extract the tarball to /app/src/main/jniLibs/_platform_, where 
_platform_ matches one of the 3 listed above. When finished, your 
directory structure should look similar to this:
   ```
   MongoDbMobile_Starter
   |-> app
      |-> src
         |-> main
            |-> jniLibs
                  |-> cmake
                  |-> pkgconfig
                  |-> libaccumulator.so
                  |-> etc.
   ```

4. Open the project in Android Studio.

5. Compile and run the project. The name of the database and
   collection have been pre-entered for you. Click `Get Remote Data` to 
   fetch the data from Atlas. If `sync data locally` is checked, the data 
   will be copied to the MongoDB Mobile database, too.

6. The app will switch to the Remote tab and display the JSON data that 
   was returned. If you chose to sync the data, you can swipe or select 
   the local tab to see the local data.

## Customize it!
This app is configured to use a test account with some simple data. When 
you are ready to customize the app and use your own data, follow these steps:

1. Change the following values:
   
   a. In `res/values/strings.xml`, replace **starterappdata-tqlwq** with your 
      Stitch App Id. Don't have a Stitch app yet? Cool -- [It's free and easy 
      to get started](https://docs.mongodb.com/stitch/procedures/create-stitch-app/)!

   b. Depending on the authentication provider(s) you want to use, change 
      the appropriate values in `strings.xml`. For example, this app 
      defaults to using an API Key. If you also want to use API Key auth, 
      in your Stitch app, generate a 
      new [API KEY](https://docs.mongodb.com/stitch/authentication/api-key/), 
      and then replace the value in the `userApiKey` field.

   c. If you haven't done so, add data to your Atlas instance and create 
      the Stitch rules to access that data. See https://docs.mongodb.com/stitch/procedures/integrate-mongo/
      for more information.

   d. In `MainActivity.java`, on or near line 34, be sure to call the 
      correct Auth method for your app if you are not using API Key Auth.

   e. In the `config_screen.xml`, remove or replace the text values for 
      the database and collection EditText elements, but keep the hints.
   
2. Compile and run the project.

## Read More
https://docs.mongodb.com/stitch/mongodb/mobile/build-mobile/

