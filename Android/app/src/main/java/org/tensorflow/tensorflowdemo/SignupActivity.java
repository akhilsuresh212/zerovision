package org.tensorflow.tensorflowdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;

import java.util.HashMap;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView tvFirstName;
    private AutoCompleteTextView tvMobileNo, otp;
    private AutoCompleteTextView tvEmail;
    private EditText tvPassword;
    private EditText tvConfirmPassword;
    private Button btnSignUp;
    private Button btnOtp;

    private LinearLayout llSignUp, llOtp;

    private ProgressDialog mProgressDialog;

    private String name;
    private String mobNo;
    private String email;
    private String password;
    private String confirmpassword;

    private String loginType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            loginType = bundle.getString("type");
        tvFirstName = (AutoCompleteTextView) findViewById(R.id.name);
        tvMobileNo = (AutoCompleteTextView) findViewById(R.id.contact_no);
        otp = (AutoCompleteTextView) findViewById(R.id.otp);
        tvEmail = (AutoCompleteTextView) findViewById(R.id.email);
        tvPassword = (EditText) findViewById(R.id.password);
        tvConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        btnSignUp = findViewById(R.id.btnWorker);
        btnOtp = findViewById(R.id.btnotp);
        llOtp = findViewById(R.id.signup_otp);
        llSignUp = findViewById(R.id.signup_menu);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCancelable(false);
        btnSignUp.setOnClickListener(this);
        btnOtp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp) {
//            Intent intentHome = new Intent(SignupActivity.this, MainActivity.class);
//            startActivity(intentHome);
//            finish();
            signUp();
        } else if (v == btnOtp) {
//            getValuesForOtp();
        }
    }

    private void signUp() {
        String name = tvFirstName.getText().toString();
        String mob = tvMobileNo.getText().toString();
        String email = tvEmail.getText().toString().trim();
        String password = tvPassword.getText().toString().trim();
        String cPassword = tvConfirmPassword.getText().toString();

        if (!cPassword.equals(password)) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Password miss match").setTitle("Password Error");
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("new_user", "1");
        map.put("user_id", email);
        map.put("user_password", password);
        map.put("username", name);
        map.put("mob", mob);
        map.put("user_type", loginType);

        String url = SubUrls.urlBuilder(map);
        new SignupExecuteTask().execute("user.php",url);
    }


    class SignupExecuteTask extends AsyncTask<String, Integer, String> {
        private final ProgressDialog i_pdDialog;

        SignupExecuteTask() {
            this.i_pdDialog = new ProgressDialog(SignupActivity.this, ProgressDialog.THEME_HOLO_DARK);
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
                    Intent i = new Intent(SignupActivity.this,MainActivity.class);
                    startActivity(i);
                }else{
                     String msg = jsonObj.getString("msg");
                    Toast.makeText(SignupActivity.this,msg,Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SignupActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }
}
