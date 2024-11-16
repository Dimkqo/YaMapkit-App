package com.example.myfirstapp.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.Rect;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.Animation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.myfirstapp.R;
import com.yandex.mapkit.places.PlacesFactory;
import com.yandex.mapkit.places.panorama.PanoramaService;
import com.yandex.mapkit.places.panorama.PanoramaView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

public class MapActivity extends AppCompatActivity {
    private boolean usedMap = true;
    private boolean activityStarted = false;
    private PanoramaView panview;
    private MapView mapview;
    private String str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar bar = getSupportActionBar();
        Bitmap marker;
        Intent intent = getIntent();
        String title  = null;
        String panId = null;
        Point location;

        mapview = findViewById(R.id.mapview);
        panview = findViewById(R.id.panview);

        if (intent.hasExtra("title") && bar != null) {
            title = intent.getStringExtra("title");
            if (title != null) {
                bar.setTitle(title);
            }
            bar.setDisplayHomeAsUpEnabled(true);
        }

        if (title == null)
            title = "Париж";

        switch (title) {
            default:
            case "Париж":
                location = new Point(48.87337007528175, 2.296682413444507);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_red);
                break;
            case "Казахстан":
                location = new Point(51.10179722805115, 70.50521879800344);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_pink);
                break;
            case "Ирландия":
                location = new Point(53.30181896693394, -6.329452533013497);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_orange);
                break;
            case "Храм Христа Спасителя":
                location = new Point(55.744667668427915, 37.60545098258569);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_yellow);
                break;
            case "Сочи":
                location = new Point(43.420434, 39.918018);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_green);
                panId = "1310710132_786004029_23_1619784726";
                usedMap = false;
                break;
            case "Ж/Д музей в СПб":
                location = new Point(59.905344, 30.29756295893);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_violet);
                panId = "1254473691_626738750_23_1603090625";
                usedMap = false;
                break;
            case "Озеро Белё":
                location = new Point(55.021065, 82.893028);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_blue);
                panId = "1611907372_684486207_23_1625999307";
                usedMap = false;
                break;
            case "Башня святого Олафа":
                location = new Point(60.715699714050814, 28.72873258490629);
                marker = getBitmapFromVectorDrawable(this, R.drawable.ic_pin_cyan);
                panId = "1245123064_616998532_23_1652877954";
                usedMap = false;
                break;
        }

        if (usedMap) {
            mapview.onStart();

            PlacemarkMapObject mark = mapview.getMap().getMapObjects().addPlacemark(location);
            mark.setIcon(ImageProvider.fromBitmap(marker), new IconStyle(
                    new PointF(0.5f, 1.0f),
                    RotationType.NO_ROTATION, 0.0f, true, true,
                    1.0f, new Rect(new PointF(0, 0), new PointF(marker.getWidth(), marker.getHeight()))));

            mapview.bringToFront();

            if (savedInstanceState != null && savedInstanceState.containsKey("activityStarted")) {
                activityStarted = savedInstanceState.getBoolean("activityStarted");
                mapview.getMap().move(new CameraPosition(location, 5.0f, 0.0f, 0.0f));
            }else {
                mapview.getMap().move(
                        new CameraPosition(location, 5.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 3f), null);
            }
        }
        else {
            panview.onStart();

            if (panId == null || panId.length() < 20) {
                Toast.makeText(this, "Панорама не найдена", Toast.LENGTH_SHORT).show();
                finish();
                onStop();
            } else {
                panview.bringToFront();
                panview.getPlayer().openPanorama(panId);
                panview.getPlayer().enableMarkers();
                panview.getPlayer().enableRotation();
                panview.getPlayer().enableZoom();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        mapview.onStop();
        panview.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable == null)
            return Bitmap.createBitmap(0,0, Bitmap.Config.HARDWARE);
        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("activityStarted", activityStarted);
        super.onSaveInstanceState(outState);
    }
}
