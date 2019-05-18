package org.tensorflow.tensorflowdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;

import java.util.HashMap;

public class AddFriendActivity extends AppCompatActivity {

    Button btn_send_request;

    EditText editText_friend_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        btn_send_request = findViewById(R.id.btn_send_request);
        editText_friend_id = findViewById(R.id.editText_friend_id);

        btn_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String friend_id = editText_friend_id.getText().toString();

                if(friend_id.equals("")) {
                    Toast.makeText(AddFriendActivity.this,"Invalid ID",Toast.LENGTH_SHORT).show();
                }else {


                    String user_id = LoginActivity.session.getusename();

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("send_friends_request", "1");
                    map.put("user_id", user_id);
                    map.put("friend_id", friend_id);

                    String url = SubUrls.urlBuilder(map);
                    new SendFriendRequestExecuteTask().execute("friend.php", url);
                }
            }
        });

    }

    class SendFriendRequestExecuteTask extends AsyncTask<String, Integer, String> {

        private final ProgressDialog i_pdDialog;
        SendFriendRequestExecuteTask() {
            this.i_pdDialog = new ProgressDialog(AddFriendActivity.this, ProgressDialog.THEME_HOLO_DARK);
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

                    String msg = jsonObj.getString("msg");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
                    builder.setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    editText_friend_id.setText("");
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }else{
                    String msg = jsonObj.getString("msg");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
                    builder.setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AddFriendActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        protected void onPreExecute() {
            i_pdDialog.setMessage("Please Wait...");
            i_pdDialog.setCancelable(false);
            i_pdDialog.show();
        }
    }

}
