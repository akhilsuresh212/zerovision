package org.tensorflow.tensorflowdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.tensorflow.demo.R;

import java.util.Locale;

public class DialActivity extends AppCompatActivity implements View.OnClickListener {

    public  EditText phoneNumber;
    public  ImageButton button0;
    public  ImageButton button1;
    public  ImageButton button2;
    public  ImageButton button3;
    public  ImageButton button4;
    public  ImageButton button5;
    public  ImageButton button6;
    public  ImageButton button7;
    public  ImageButton button8;
    public  ImageButton button9;
    public  ImageButton buttonDelete;
    public ImageButton ButtonContract;

    private TextToSpeech myTTS;

    private static final int REQUEST = 112;


    int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        phoneNumber = findViewById(R.id.EditTextPhoneNumber);
        initializeTextToSpeech();
    }


    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(DialActivity.this,"There is no TTS Engine on your Device",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    speak("Dial Number");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            i++;
            if(i >= 14){
                i =0;
            }
            switch (i){
                case 0 :
                    speak("One");
                    break;
                case 1:
                    speak("Two");
                    break;
                case 2 :
                    speak("Three");
                    break;
                case 3 :
                    speak("Four");
                    break;
                case 4 :
                    speak("Five");
                    break;
                case 5 :
                    speak("Six");
                    break;
                case 6 :
                    speak("Seven");
                    break;
                case 7 :
                    speak("eight");
                    break;
                case 8 :
                    speak("nine");
                    break;
                case 9 :
                    speak("star");
                    break;
                case 10 :
                    speak("zero");
                    break;
                case 11 :
                    speak("hash");
                    break;
                case 12 :
                    speak("call");
                    break;
                case 13 :
                    speak("back space");
                    break;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            switch (i){
                case 0 :
                    onCharacterPressed('1');
                    break;
                case 1:
                    onCharacterPressed('2');
                    break;
                case 2 :
                    onCharacterPressed('3');
                    break;
                case 3 :
                    onCharacterPressed('4');
                    break;
                case 4 :
                    onCharacterPressed('5');
                    break;
                case 5 :
                    onCharacterPressed('6');
                    break;
                case 6 :
                    onCharacterPressed('7');
                    break;
                case 7 :
                    onCharacterPressed('8');
                case 8 :
                    onCharacterPressed('9');
                    break;
                case 9 :
                    onCharacterPressed('*');
                    break;
                case 10 :
                    onCharacterPressed('0');
                    break;
                case 11 :
                    onCharacterPressed('#');
                    break;
                case 12 :
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                        if (!hasPermissions(DialActivity.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(DialActivity.this, PERMISSIONS, REQUEST );
                        } else {
                            makeCall();
                        }
                    } else {
                        makeCall();
                    }

                    break;
                case 13 :
                    onDeletePressed();
                    break;
            }
        }else if(keyCode == 4){
            finish();
        }
        return  true;
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(DialActivity.this, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void makeCall()
    {
        speak("call");
        String number = phoneNumber.getText().toString();
        Intent intent4=new Intent(Intent.ACTION_CALL);
        intent4.setData(Uri.parse("tel:"+number));

        if (ActivityCompat.checkSelfPermission(DialActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        }else{
            startActivity(intent4);
        }

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Intent intent = new Intent(DialActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button0:
                onCharacterPressed('0');
                break;
            case R.id.Button1:
                onCharacterPressed('1');
                break;
            case R.id.Button2:
                onCharacterPressed('2');
                break;
            case R.id.Button3:
                onCharacterPressed('3');
                break;
            case R.id.Button4:
                onCharacterPressed('4');
                break;
            case R.id.Button5:
                onCharacterPressed('5');
                break;
            case R.id.Button6:
                onCharacterPressed('6');
                break;
            case R.id.Button7:
                onCharacterPressed('7');
                break;
            case R.id.Button8:
                onCharacterPressed('8');
                break;
            case R.id.Button9:
                onCharacterPressed('9');
                break;
            case R.id.ButtonStar:
                onCharacterPressed('*');
                break;
            case R.id.ButtonHash:
                onCharacterPressed('#');
                break;
            case R.id.ButtonDelete:
                onDeletePressed();
                break;
            case R.id.ButtonCall:
                makeCall();
                break;
            case R.id.ButtonContract:

                break;
        }
    }
    private void onDeletePressed() {
        speak("back space");
        CharSequence cur = phoneNumber.getText();
        int start = phoneNumber.getSelectionStart();
        int end = phoneNumber.getSelectionEnd();
        if (start == end) { // remove the item behind the cursor
            if (start != 0) {
                cur = cur.subSequence(0, start-1).toString() + cur.subSequence(start, cur.length()).toString();
                phoneNumber.setText(cur);
                phoneNumber.setSelection(start-1);
                if (cur.length() == 0) {
                    phoneNumber.setCursorVisible(false);
                }
            }
        } else { // remove the whole selection
            cur = cur.subSequence(0, start).toString() + cur.subSequence(end, cur.length()).toString();
            phoneNumber.setText(cur);
            phoneNumber.setSelection(end - (end - start));
            if (cur.length() == 0) {
                phoneNumber.setCursorVisible(false);
            }
        }
    }

    private void onCharacterPressed(char digit) {

        switch (digit){
            case '1' :
                speak("One");
                break;
            case '2':
                speak("Two");
                break;
            case  '3' :
                speak("Three");
                break;
            case '4' :
                speak("Four");
                break;
            case '5' :
                speak("Five");
                break;
            case  '6' :
                speak("Six");
                break;
            case '7' :
                speak("Seven");
                break;
            case '8' :
                speak("eight");
                break;
            case '9' :
                speak("nine");
                break;
            case '*' :
                speak("star");
                break;
            case '0' :
                speak("zero");
                break;
            case '#' :
                speak("hash");
                break;
            case 12 :
                speak("call");

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(DialActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(DialActivity.this, PERMISSIONS, REQUEST );
                    } else {
                        makeCall();
                    }
                } else {
                    makeCall();
                }


                break;
            case 13 :
                speak("back space");
                onDeletePressed();
                break;
        }

        CharSequence cur = phoneNumber.getText();

        int start = phoneNumber.getSelectionStart();
        int end = phoneNumber.getSelectionEnd();
        int len = cur.length();


        if (cur.length() == 0) {
            phoneNumber.setCursorVisible(false);
        }

        cur = cur.subSequence(0, start).toString() + digit + cur.subSequence(end, len).toString();
        phoneNumber.setText(cur);
        phoneNumber.setSelection(start+1);
    }
}
