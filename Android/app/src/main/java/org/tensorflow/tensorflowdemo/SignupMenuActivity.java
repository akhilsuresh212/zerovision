package org.tensorflow.tensorflowdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import org.tensorflow.demo.R;


public class SignupMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);
        Button btnFriend = findViewById(R.id.btnFriend);
        Button btnBlind = findViewById(R.id.btnBlind);

        btnBlind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(SignupMenuActivity.this, SignupActivity.class);
                intentSignUp.putExtra("type","Blind");
                startActivity(intentSignUp);
                finish();
            }
        });

        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(SignupMenuActivity.this, SignupActivity.class);
                intentSignUp.putExtra("type","Friend");
                startActivity(intentSignUp);
                finish();
            }
        });

    }
}
