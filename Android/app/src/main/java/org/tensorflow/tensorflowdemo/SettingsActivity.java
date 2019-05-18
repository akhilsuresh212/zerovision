package org.tensorflow.tensorflowdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import org.tensorflow.demo.R;

public class SettingsActivity extends AppCompatActivity {

    Button btn_add_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_add_friend =  findViewById(R.id.btn_addfriend);

        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,AddFriendActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyLongPress(keyCode, event);
    }
}
