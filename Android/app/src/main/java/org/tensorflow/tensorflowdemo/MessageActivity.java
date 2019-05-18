package org.tensorflow.tensorflowdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    String friend_id;

    String msg;
    private TextToSpeech myTTS;

    TextView tvfrommsg;

    Button send_msg;

    Friend f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tvfrommsg = findViewById(R.id.from_msg);
        send_msg = findViewById(R.id.send_msg);
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message();
            }
        });

        Intent intent = getIntent();
        friend_id = intent.getStringExtra("friend_id");
        msg =intent.getStringExtra("msg");
        initializeTextToSpeech();
    }



    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(MessageActivity.this,"There is no TTS Engine on your Device",Toast.LENGTH_LONG).show();
                }else {
                    myTTS.setLanguage(Locale.US);
                            if(msg.equals("")){
                                speak("no message");
                            }else{
                                tvfrommsg.setText("You have a message "+ msg);
                                speak("You have a message "+ msg);
                            }
                }
            }
        });
    }

    void send_message()
    {
        speak("waiting for message");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,10);
        }else {
            speak("Device not supported");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            send_message();
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){


        }else if(keyCode == 4){
            finish();
        }
        return true;
    }

    private void speak(String message) {

        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }else {
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
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
                       String msg =  result.get(0);


                        String user_id = LoginActivity.session.getusename();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("send_msg", "1");
                        map.put("msg_from", user_id);
                        map.put("msg_to", friend_id);
                        map.put("msg", msg);

                        String  url = SubUrls.urlBuilder(map);
                        new SendMessageExecuteTask().execute("message.php",url);

                    }
                }
                break;
        }
    }


    class SendMessageExecuteTask extends AsyncTask<String, Integer, String> {

        private final ProgressDialog i_pdDialog;
        SendMessageExecuteTask() {
            this.i_pdDialog = new ProgressDialog(MessageActivity.this, ProgressDialog.THEME_HOLO_DARK);
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
                    speak("Message sent");
                }else{
                    String msg = jsonObj.getString("msg");
                    speak(msg);
                    Toast.makeText(MessageActivity.this,msg,Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MessageActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }

}
