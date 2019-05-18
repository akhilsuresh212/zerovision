package org.tensorflow.tensorflowdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.tensorflow.demo.R;

public class AppsListActivity extends AppCompatActivity {

    PackageManager manager;
    List<AppDetail> apps;

    ListView list;

    private TextToSpeech myTTS;
    int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        loadApps();
        loadListView();
        addClickListener();

        initializeTextToSpeech();
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(AppsListActivity.this,"There is no TTS Engine on your Device",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    speak("Menu");
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            i++;
            if(i >= apps.size()){
                i = 0;
            }
            speak(apps.get(i).label.toString());
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            Intent intent = manager.getLaunchIntentForPackage(apps.get(i).name.toString());
            AppsListActivity.this.startActivity(intent);
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

    private void loadApps()
    {
        manager = getPackageManager();
        apps= new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN,null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> avaliableActivity = manager.queryIntentActivities(i,0);

        for(ResolveInfo ri :avaliableActivity){
            AppDetail app = new AppDetail();
            app.label= ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }

    }


    private void loadListView()
    {
        list = (ListView) findViewById(R.id.apps_list);
        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this,R.layout.list_item,apps){
            @Override
            public View getView(int position,View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item,null);
                }
                ImageView appIcon= (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);
                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                TextView appName = (TextView) convertView.findViewById(R.id.item_app_name);
                appName.setText(apps.get(position).name);

                return convertView;
            }
        };
        list.setAdapter(adapter);
    }

    private void addClickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                speak(apps.get(position).label.toString() + " opening");

                Intent intent = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                AppsListActivity.this.startActivity(intent);
            }
        });
    }
}
