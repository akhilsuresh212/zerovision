package org.tensorflow.tensorflowdemo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.CameraActivity;
import org.tensorflow.demo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    private ImageView ivVideo;
    private ImageView ivDial;
    private ImageView ivSettings;
    private ImageView ivChat;
    private ImageView ivSupportCall;
    private ImageView ivMenu;

    private ImageView btnSpeechRecognizer;

    private TextToSpeech myTTS;
//    private CardView btnSpeechRecognizer;

    private ProgressDialog mProgressDialog;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private boolean alreadyadded = false;

    private String mStrLatitude;
    private String mStrLongitude;

    private int i = -1;

    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    ArrayList<Friend> list_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivVideo = findViewById(R.id.iv_video);
        ivDial = findViewById(R.id.iv_dial);
        ivSupportCall = findViewById(R.id.iv_camera);
        ivChat = findViewById(R.id.iv_chat);
        ivMenu = findViewById(R.id.iv_forwrd);
        ivSettings = findViewById(R.id.iv_settings);

        list_friends = new ArrayList<Friend>();




        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(0);
            }
        });
        ivDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(1);
            }
        });
        ivSupportCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(2);
            }
        });
        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(3);
            }
        });

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(4);
            }
        });
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation(5);
            }
        });



        btnSpeechRecognizer = findViewById(R.id.btnSpeechRecognizer);
        btnSpeechRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent,10);
                }else {
                    speak("Device not supported");
                }

            }
        });
        initializeTextToSpeech();
        initializeSpeechRecognizer();

    }

    void navigation(int i){
        Intent intent;
        String url;
        switch (i){
            case 0 :

                String user_id = LoginActivity.session.getusename();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("friends_list", "1");
                map.put("user_id", user_id);
                url = SubUrls.urlBuilder(map);
                new FriendListrExecuteTask().execute("friend.php",url);
                break;
            case 1 :
                intent = new Intent(MainActivity.this,DialActivity.class);
                startActivity(intent);
                break;
            case 2 :

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("org.appspot.apprtc");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }

//                String mimeString = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";
//
//
//                String data = "content://com.android.contacts/data/" + 29117;
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_VIEW);
//                sendIntent.setDataAndType(Uri.parse(data), mimeString);
//                sendIntent.setPackage("com.whatsapp");
//                startActivity(sendIntent);


//
//                String displayName = null;
//                String name="Makku 2017"; // here you can give static name.
//                Long _id ;
//                ContentResolver resolver = getApplicationContext().getContentResolver();
//                Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
//                while (cursor.moveToNext()) {
//                    _id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
//
//                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
//                    String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
//                    Log.e("ID ",String.valueOf(_id) + "  " + String.valueOf(displayName)+" "+mimeType);
//                    if(displayName != null) {
//                        if (displayName.equals(name)) {
//                            if (mimeType.equals(mimeString)) {
//                                String data = "content://com.android.contacts/data/" + _id;
//                                Intent sendIntent = new Intent();
//                                sendIntent.setAction(Intent.ACTION_VIEW);
//                                sendIntent.setDataAndType(Uri.parse(data), mimeString);
//                                sendIntent.setPackage("com.whatsapp");
//                                startActivity(sendIntent);
//                            }
//                        }
//                    }
//                }


                break;
            case 3:
                intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(MainActivity.this,AppsListActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            i++;
            if(i >= 6){
                i = 0;
            }

            if (i == 0){
                imageWhiteColor(ivChat);
                speak("Message");
                imageBlackColor(ivSupportCall);
                imageBlackColor(ivDial);
                imageBlackColor(ivMenu);
                imageBlackColor(ivSettings);
                imageBlackColor(ivVideo);
            } else if ( i == 1){
                imageWhiteColor(ivDial);
                speak("Dial");
                imageBlackColor(ivSupportCall);
                imageBlackColor(ivChat);
                imageBlackColor(ivMenu);
                imageBlackColor(ivSettings);
                imageBlackColor(ivVideo);
            }else if ( i == 2){
                speak("Support");
                imageWhiteColor(ivSupportCall);
                imageBlackColor(ivChat);
                imageBlackColor(ivDial);
                imageBlackColor(ivMenu);
                imageBlackColor(ivSettings);
                imageBlackColor(ivVideo);
            }else if ( i == 3){
                speak("Scan");
                imageWhiteColor(ivVideo);
                imageBlackColor(ivSupportCall);
                imageBlackColor(ivDial);
                imageBlackColor(ivMenu);
                imageBlackColor(ivSettings);
                imageBlackColor(ivChat);
            }else if ( i == 4){
                speak("Settings");
                imageWhiteColor(ivSettings);
                imageBlackColor(ivSupportCall);
                imageBlackColor(ivDial);
                imageBlackColor(ivMenu);
                imageBlackColor(ivChat);
                imageBlackColor(ivVideo);
            }else if ( i == 5){
                speak("Menu");
                imageWhiteColor(ivMenu);
                imageBlackColor(ivSupportCall);
                imageBlackColor(ivDial);
                imageBlackColor(ivChat);
                imageBlackColor(ivSettings);
                imageBlackColor(ivVideo);
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){

            navigation(i);
        }



        return true;
    }

    private void imageWhiteColor(ImageView imageView){
        imageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }
    private void imageBlackColor(ImageView imageView){
        imageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black));
    }



    private void initializeSpeechRecognizer() {

    }

    private void processResult(String command) {
        command = command.toLowerCase();
        if(command.indexOf("what") != -1){
            if(command.indexOf("your name") != -1){
                speak("My name is Zero vision");
            }
            if(command.indexOf("time") != -1){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is "+ time);
            }
        }

        if(command.indexOf("what") != -1){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 10 :
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result == null) {
                        speak("No voice results");
                    } else {
                        processResult(result.get(0));
                    }
                }
                break;
        }
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this,"There is no TTS Engine on your Device",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    speak("Welcome to Zero Vision");
                }
            }
        });
    }

    private void speak(String message) {

        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }else {
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    class UpdateLocationExecuteTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            return SubUrls.PostData(params[0],params[1]);
        }
        protected void onPostExecute(String l_strMsg) {
            try {
                JSONObject jsonObj = new JSONObject(l_strMsg);
                String status = jsonObj.getString("status");
                if(status.equals("1")){


                }else{
                    String msg = jsonObj.getString("msg");
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {

        }
    }


    class FriendListrExecuteTask extends AsyncTask<String, Integer, String> {
        private final ProgressDialog i_pdDialog;
        FriendListrExecuteTask() {
            this.i_pdDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
        }
        @Override
        protected String doInBackground(String... params) {
            return SubUrls.PostData(params[0],params[1]);
        }
        protected void onPostExecute(String l_strMsg) {
            if (i_pdDialog.isShowing()) {
                i_pdDialog.dismiss();
            }

            try {
                JSONObject jsonObj = new JSONObject(l_strMsg);
                String status = jsonObj.getString("status");
                if(status.equals("1")){
                    JSONArray jsonResult = jsonObj.getJSONArray("result");

                    list_friends.clear();

                    for(int i=0; i<jsonResult.length(); i++){
                        String username = jsonResult.getJSONObject(i).getString("username");
                        String mob = jsonResult.getJSONObject(i).getString("mob");
                        String user_id = jsonResult.getJSONObject(i).getString("user_id");
                        Friend f= new Friend();
                        f.setEmail(user_id);
                        f.setMob(mob);
                        f.setName(username);
                        list_friends.add(f);
                    }

                    if(list_friends.size() > 0){
                        Intent intent = new Intent(MainActivity.this,ViewFriendsActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST",(Serializable) list_friends);
                        intent.putExtra("BUNDLE",args);
                        startActivity(intent);
                    }else{

                        Toast.makeText(MainActivity.this,"No friends",Toast.LENGTH_LONG).show();
                    }

                }else{
                    String msg = jsonObj.getString("msg");
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }



//    public String hasWhatsapp(  getContactIDFromNumber(795486179).toString(),MAinactivity.this )
//    {
//        String rowContactId = null;
//        boolean hasWhatsApp;
//
//        String[] projection = new String[]{ContactsContract.RawContacts._ID};
//        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
//        String[] selectionArgs = new String[]{contactID, "com.whatsapp"};
//        Cursor cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
//        if (cursor != null) {
//            hasWhatsApp = cursor.moveToNext();
//            if (hasWhatsApp) {
//                rowContactId = cursor.getString(0);
//            }
//            cursor.close();
//        }
//        return rowContactId;
//    }


    public static int getContactIDFromNumber(String contactNumber, Context context)
    {
        contactNumber = Uri.encode(contactNumber);
        int phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,contactNumber),new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        while(contactLookupCursor.moveToNext())
        {
            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
        }
        contactLookupCursor.close();

        return phoneContactID;
    }




    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }


    public  int getContactIdForWhatsAppCall(String name,Context context)
    {

        Cursor cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and "+ContactsContract.Data.MIMETYPE+ "=?",
                new String[] {name,"vnd.android.cursor.item/vnd.com.whatsapp.voip.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount()>0)
        {
            cursor.moveToNext();
            int phoneContactID=  cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            System.out.println("9999999999999999          name  "+name+"      id    "+phoneContactID);
            return phoneContactID;
        }
        else
        {
            System.out.println("8888888888888888888          ");
            return 0;
        }
    }

    public  int getContactIdForWhatsAppVideoCall(String name,Context context)
    {
        Cursor  cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and "+ContactsContract.Data.MIMETYPE+ "=?",
                new String[] {name,"vnd.android.cursor.item/vnd.com.whatsapp.video.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            int phoneContactID=  cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            return phoneContactID;
        }
        else
        {
            System.out.println("8888888888888888888          ");
            return 0;
        }
    }


}
