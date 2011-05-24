package com.example.AsyncWeather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button btnXML;
    Button btnJSON;
    TextView tvData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        tvData = (TextView) findViewById(R.id.txtData);
        
        btnJSON = (Button) findViewById(R.id.btnJSON);
        btnJSON.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                examineJSONFile();
            }
        });

    }
    
    
    void examineJSONFile()
    {
        try
        {
            String x = "";
            
            String weatherUrl = "http://free.worldweatheronline.com/feed/weather.ashx?q=16803&format=json&num_of_days=5&key=586dac13bc212929112804";            
            String jsontext = getStringContent(weatherUrl);

            JSONObject weather = new JSONObject(jsontext);
            JSONObject data = weather.getJSONObject("data");
            JSONArray currentCondition = new JSONArray(data.getString("current_condition"));
            
        
            String currentWeatherDescription = currentCondition.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            String currentTemperature = currentCondition.getJSONObject(0).getString("temp_F");
            
            x = currentTemperature + " degrees Farenheit\n\n";
            x += currentWeatherDescription;

            tvData.setText(x);
        }
        catch (Exception je)
        {
            tvData.setText("Error w/file: " + je.getMessage());
        }
    }
    
    public static String getStringContent(String uri) throws Exception {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            HttpResponse response = client.execute(request);
            InputStream ips  = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));

            StringBuilder sb = new StringBuilder();
            String s;
            while(true )
            {
                s = buf.readLine();
                if(s==null || s.length()==0)
                    break;
                sb.append(s);

            }
            buf.close();
            ips.close();
            return sb.toString();

            } 
        finally {
                   // any cleanup code...
                }
            } 
}