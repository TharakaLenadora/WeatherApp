package com.example.weatherapp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DayViewActivity extends AppCompatActivity {

    private TextView txtDate, txtTemp, txtHumidity;
    private ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        txtDate = findViewById(R.id.txtDate2);
        txtTemp = findViewById(R.id.txtTemp2);
        txtHumidity = findViewById(R.id.txtHumidity2);
        imgIcon = findViewById(R.id.imgIcon2);

        Intent intent = getIntent();
        txtDate.setText(intent.getStringExtra("date"));
        txtTemp.setText(intent.getStringExtra("temperature"));
        txtHumidity.setText("Humidity: " + intent.getStringExtra("humidity") + "%");

        String iconCode = intent.getStringExtra("icon");
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Picasso.get().load(iconUrl).into(imgIcon);
    }
}
