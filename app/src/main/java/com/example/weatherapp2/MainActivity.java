package com.example.weatherapp2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<WeatherData> weatherList = new ArrayList<>();
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        listView = findViewById(R.id.listView);
        adapter = new CustomListAdapter(this, weatherList);
        listView.setAdapter(adapter);

        new FetchWeather().execute();
    }

    private class FetchWeather extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String apiKey = "your_api_key_here";
                String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=colombo,lk&cnt=20&appid=" + apiKey + "&units=metric";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                return builder.toString();

            } catch (Exception e) {
                Log.e("FetchWeather", "Error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray list = jsonObject.getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {
                    JSONObject obj = list.getJSONObject(i);
                    String dateTime = obj.getString("dt_txt");

                    JSONObject main = obj.getJSONObject("main");
                    String temp = main.getString("temp") + "°C";

                    JSONArray weatherArr = obj.getJSONArray("weather");
                    String icon = weatherArr.getJSONObject(0).getString("icon");

                    weatherList.add(new WeatherData(dateTime, temp, icon));
                }

                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                Log.e("onPostExecute", "Parsing error", e);
            }
        }
    }

    // Model class
    public class WeatherData {
        public String dateTime;
        public String temperature;
        public String iconCode;

        public WeatherData(String dateTime, String temperature, String iconCode) {
            this.dateTime = dateTime;
            this.temperature = temperature;
            this.iconCode = iconCode;
        }
    }

    // Custom adapter class
    public class CustomListAdapter extends ArrayAdapter<WeatherData> {

        public CustomListAdapter(Context context, ArrayList<WeatherData> dataList) {
            super(context, 0, dataList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WeatherData data = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_my_list, parent, false);
            }

            TextView txtDate = convertView.findViewById(R.id.txt_date);
            TextView txtTemp = convertView.findViewById(R.id.txt_temp);
            ImageView icon = convertView.findViewById(R.id.icon);

            txtDate.setText(data.dateTime);
            txtTemp.setText(data.temperature);

            String imageUrl = "https://openweathermap.org/img/wn/" + data.iconCode + "@2x.png";
            Picasso.get().load(imageUrl).into(icon);

            return convertView;
        }
    }
}
