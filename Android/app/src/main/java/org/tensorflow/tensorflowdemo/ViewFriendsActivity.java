package org.tensorflow.tensorflowdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ViewFriendsActivity extends Activity {

    ListView list;
    ArrayList<Friend> list_friends;
    private TextToSpeech myTTS;
    int i = 0;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);

        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("BUNDLE");
        list_friends = (ArrayList<Friend>) args.getSerializable("ARRAYLIST");

        MyListAdapter adapter=new MyListAdapter(this,list_friends);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoMessage();
            }
        });

        initializeTextToSpeech();
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(ViewFriendsActivity.this,"There is no TTS Engine on your Device",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    speak("select Friend");
                }
            }
        });
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Friend f = list_friends.get(i);
            speak(f.name);
            i++;
            if(list_friends.size() >= i){
                i=0;
            }
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            gotoMessage();
        }else if(keyCode == 4){
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    void gotoMessage()
    {

        Friend f = list_friends.get(i);
        String user_id = LoginActivity.session.getusename();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("get_message", "1");
        map.put("user_id",user_id );
        map.put("friend_id", f.getEmail());
        String url = SubUrls.urlBuilder(map);
        new NewMessageExecuteTask().execute("message.php",url);

    }


    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);
        }else {
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }
    }


//    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            Intent intent = new Intent(ViewFriendsActivity.this,MainActivity.class);
//            startActivity(intent);
//        }
//        return super.onKeyLongPress(keyCode, event);
//    }

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

                    }
                }
                break;
        }
    }

    protected void sendSMS() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    class NewMessageExecuteTask extends AsyncTask<String, Integer, String> {
        private final ProgressDialog i_pdDialog;
        NewMessageExecuteTask() {
            this.i_pdDialog = new ProgressDialog(ViewFriendsActivity.this, ProgressDialog.THEME_HOLO_DARK);
        }
        @Override
        protected String doInBackground(String... params) {
            return SubUrls.PostData(params[0],params[1]);
        }
        protected void onPostExecute(String l_strMsg) {
            if (i_pdDialog.isShowing()) {
                i_pdDialog.dismiss();
            }
            String msg ="";
            try {
                JSONObject jsonObj = new JSONObject(l_strMsg);
                String status = jsonObj.getString("status");
                if(status.equals("1")){
                    JSONArray jsonResult = jsonObj.getJSONArray("result");
                    for(int i=0; i<jsonResult.length(); i++){
                        msg += jsonResult.getJSONObject(i).getString("msg")+" ";
                    }
                }else{
                    msg = jsonObj.getString("msg");
                    Toast.makeText(ViewFriendsActivity.this,msg,Toast.LENGTH_LONG).show();
                }

                Friend f = list_friends.get(i);
                Intent intent=new Intent(ViewFriendsActivity.this,MessageActivity.class);
                intent.putExtra("friend_id",f.getEmail());
                intent.putExtra("msg",msg);
                startActivity(intent);

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ViewFriendsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        speak("back");
    }
}
