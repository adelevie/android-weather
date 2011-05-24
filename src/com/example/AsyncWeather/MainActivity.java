package com.example.AsyncWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

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
    
    private String DownloadText(String URL)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "";
        }
        
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];          
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                    
                //—convert the chars to a String—
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }    
        return str;        
    }
    
    private InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
    
    void examineJSONFile()
    {
        try
        {
            String x = "";
            
            String weatherUrl = "http://free.worldweatheronline.com/feed/weather.ashx?q=16803&format=json&num_of_days=5&key=586dac13bc212929112804";            
           // String jsontext = DownloadText(weatherUrl)
            
            String jsontext = getStringContent(weatherUrl);
            
            
            
            /*byte [] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer);*/
            
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