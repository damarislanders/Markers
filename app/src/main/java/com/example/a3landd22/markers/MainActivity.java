package com.example.a3landd22.markers;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.preference.PreferenceManager;
import android.widget.Toast;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class MainActivity extends AppCompatActivity {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        mv = (MapView) findViewById(R.id.map1);

        mv.getController().setZoom(14);
        mv.getController().setCenter(new GeoPoint(53.4113, -2.1601));

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), null);

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("poi.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] components = line.split(",");
                if (components.length == 5) {
                    OverlayItem poi = new OverlayItem(components[0], components[1], components[2], components[3], components[4]);
                    items.addItem(poi);
                }
            }

        } catch (IOException e)
        {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).
                    setMessage("ERROR: " + e).show();
        }

        OverlayItem myHouse = new OverlayItem("My House", "My house by the Goyt river", new GeoPoint(53.4181, -2.1406));
        OverlayItem pearMill = new OverlayItem("Pear Mill", "A converted mill, now used for activities and shops", new GeoPoint(53.4139, -2.1340));

        items.addItem(myHouse);
        items.addItem(pearMill);

        mv.getOverlays().add(items);
    }
}
