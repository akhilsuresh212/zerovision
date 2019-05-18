package org.tensorflow.tensorflowdemo;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shafnaz on 07/02/2017.
 */

public class SubUrls {

//    public static final String BASE_URL ="http://zerovision.bluemooninfotech.com/";

    public static final String BASE_URL ="http://192.168.43.12/zerovision/";

    public static String urlBuilder(HashMap<String,String> map)
    {
        String params = "";
        for (Map.Entry<String,String> entry : map.entrySet()) {
            try {
                String key = entry.getKey();
                String value = entry.getValue();
                if(params.equals("")){
                    params +=  URLEncoder.encode(key, "UTF-8")  +"="+URLEncoder.encode(value, "UTF-8");
                }else{
                    params += "&"+ URLEncoder.encode(key, "UTF-8")  +"="+URLEncoder.encode(value, "UTF-8");
                }
            }catch (Exception e){
                Log.e("Error" , e.toString());
            }
        }
        Log.d("param :",params);
        return params;
    }

    public static String PostData(String page,String data) {
        String result = "";
        try
        {
            // Defined URL  where to send data
            URL url = new URL(SubUrls.BASE_URL+page);
            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        }
        catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
        return result;
    }

}
