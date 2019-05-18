package org.tensorflow.tensorflowdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    public static  Session session;

    String email;
    String pass;

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
                && containsInvalidMailAddress(target);
    }

    private static boolean containsInvalidMailAddress(CharSequence target) {
        if (target == null) {
            return false;
        }
        String text = (String) target;
        try {
            text = text.split("@")[1];
            if (!text.contains(".")) {
                return false;
            }
            if (TextUtils.isDigitsOnly(text.split("\\.")[0])) {
                return false;
            }
            if (TextUtils.isDigitsOnly(text.split("\\.")[1])) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               email = mEmailView.getText().toString().trim();
               pass = mPasswordView.getText().toString().trim();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_login", "1");
                map.put("user_id", email);
                map.put("user_password", pass);

                String url = SubUrls.urlBuilder(map);
                new LoginExecuteTask().execute("user.php",url);

            }
        });
        TextView txtSignup = findViewById(R.id.signup);
        txtSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(LoginActivity.this, SignupMenuActivity.class);
                startActivity(intentSignup);
            }
        });



        session = new Session(LoginActivity.this);
        String uname = session.getusename();
        if(!session.getusename().equals(""))
        {
            if(session.getUserType().equals("Blind")){
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }

        }

    }



    class LoginExecuteTask extends AsyncTask<String, Integer, String> {
        private final ProgressDialog i_pdDialog;
        LoginExecuteTask() {
            this.i_pdDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_DARK);
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

                    String user_type = jsonObj.getString("user_type");
                    session = new Session(LoginActivity.this);
                    session.setusename(email);
                    session.setPassword(pass);
                    session.setUserType(user_type);

                    if(user_type.equals("Blind")){
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{

                    }


                }else{
                    String msg = jsonObj.getString("msg");
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }

}

