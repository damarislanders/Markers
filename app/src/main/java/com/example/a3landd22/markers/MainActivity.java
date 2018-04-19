package com.example.a3landd22.markers;

import android.os.Environment;
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
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        mv = (MapView) findViewById(R.id.map1);

        mv.getController().setZoom(14);
        //mv.getController().setCenter(new GeoPoint(53.4113, -2.1601));
        mv.getController().setCenter(new GeoPoint(50.911, -1.40448));

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath()+"/poi.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] components = line.split(",");
                if (components.length == 5) {
                    OverlayItem poi = new OverlayItem(components[0], components[1], components[2], new GeoPoint( Double.parseDouble(components[4]), Double.parseDouble(components[3]) ) );
                    if ( components[1].equals("pub") )
                    {
                        poi.setMarker(getResources().getDrawable(R.drawable.pub));
                    }
                    else
                    {
                        poi.setMarker(getResources().getDrawable(R.drawable.restaurant));
                    }
                    items.addItem(poi);
                }
            }

        } catch (IOException e)
        {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).
                    setMessage("ERROR: " + e).show();
        }

        /*
        OverlayItem myHouse = new OverlayItem("My House", "My house by the Goyt river", new GeoPoint(53.4181, -2.1406));
        OverlayItem pearMill = new OverlayItem("Pear Mill", "A converted mill, now used for activities and shops", new GeoPoint(53.4139, -2.1340));

        items.addItem(myHouse);
        items.addItem(pearMill);
        */

        mv.getOverlays().add(items);
    }
}
