package com.example.myfirstapp.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.R;
import com.example.myfirstapp.map.MapActivity;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.places.PlacesFactory;
import com.yandex.mapkit.places.panorama.PanoramaService;
import com.yandex.runtime.Error;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private boolean mapIsInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loc1 = findViewById(R.id.location);
        Button loc2 = findViewById(R.id.location2);
        Button loc3 = findViewById(R.id.location3);
        Button loc4 = findViewById(R.id.location4);
        Button loc5 = findViewById(R.id.location5);
        Button loc6 = findViewById(R.id.location6);
        Button loc7 = findViewById(R.id.location7);
        Button loc8 = findViewById(R.id.location8);
        Context t = this;

        View.OnClickListener click = v -> {
            Intent intent = new Intent(t, MapActivity.class);
            intent.putExtra("title", ((Button)v).getText().toString());
            startActivity(intent);
        };

        for (Button button : Arrays.asList(loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8))
            button.setOnClickListener(click);

        if (savedInstanceState != null && savedInstanceState.containsKey("mapIsInited")) {
            mapIsInited = savedInstanceState.getBoolean("mapIsInited");
        }

        if (!mapIsInited) {
            MapKitFactory.setApiKey("some_API_key");
            MapKitFactory.initialize(this);
            mapIsInited = true;
        }
    }

    @Override
    public void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        if (!mapIsInited)
            MapKitFactory.getInstance().onStart();
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("mapIsInited", mapIsInited);
        super.onSaveInstanceState(outState);
    }
}
