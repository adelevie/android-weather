package com.example.AsyncWeather;

import java.io.InputStream;

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
            InputStream is = this.getResources().openRawResource(R.raw.jsonweather);
            byte [] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer);
            JSONObject weather = new JSONObject(jsontext);
            JSONObject data = weather.getJSONObject("data");
            JSONArray currentCondition = new JSONArray(data.getString("current_condition"));
            
        
            String currentWeatherDescription = currentCondition.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            String currentTemperature = currentCondition.getJSONObject(0).getString("temp_F");
            
            x = currentTemperature + " degrees Farenheit\n\n";
            x += currentWeatherDescription;

            /*
            int i;
            for (i=0;i<entries.length();i++)
            {
                JSONObject post = entries.getJSONObject(i);
                x += "------------\n";
                x += "Date:" + post.getString("created_at") + "\n";
                x += "Post:" + post.getString("text") + "\n\n";
            }
            */
            tvData.setText(x);
        }
        catch (Exception je)
        {
            tvData.setText("Error w/file: " + je.getMessage());
        }
    }
}