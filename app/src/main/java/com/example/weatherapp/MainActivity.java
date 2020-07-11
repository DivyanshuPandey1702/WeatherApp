package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                String result = "";
                int data = reader.read();
                while(data != -1){
                    char ch = (char) data;
                    result += ch;
                    data = reader.read();
                }
                return  result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String output = "";
                String coordinates = jsonObject.getString("coord");
                JSONObject jsonObjectcoord = new JSONObject(coordinates);
                output += "Longitude: " + jsonObjectcoord.getString("lon") + ", Latitude:"
                        + jsonObjectcoord.get("lat") + "\n";
                String weatherinfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherinfo);
                for(int i = 0; i < arr.length(); i++){
                    JSONObject jsonObjPart = arr.getJSONObject(i);
                    output += jsonObjPart.getString("main")+" : "+jsonObjPart.getString("description") + "\n";
                }
                String jmain = jsonObject.getString("main");
                JSONObject jobjmain = new JSONObject(jmain);
                output += "Temperature: "+jobjmain.getString("temp")+", "
                        + "Feels Like: "+jobjmain.getString("feels_like")
                        +"\n" + "Min Temp: "+jobjmain.getString("temp_min")
                        + ", Max Temp: " + jobjmain.getString("temp_max")
                        +"\n"+"Pressure: "+jobjmain.getString("pressure")+", "
                        +"Humidity: "+jobjmain.getString("humidity")+"\n";
                TextView textView = findViewById(R.id.textView4);
                textView.setText(output);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextView);
    }

    public void getWeather(View view){
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");
    }
}