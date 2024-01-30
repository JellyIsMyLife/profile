package com.example.knygnesys.client;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.knygnesys.R;
import com.example.knygnesys.utils.DBConnect;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientMainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private ListView listView;
    private Handler handler;
    private Executor executor;
    List<String> cartItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);
        //mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        GoogleMap mMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private class FetchDataTask implements Runnable {
        @Override
        public void run() {
            List<String> rows = new ArrayList<>();
            try {
                Connection conn = DBConnect.getConnection();
                // Execute a query to retrieve data
                Statement statement = conn.createStatement();
                String sql = "SELECT * FROM books WHERE is_available=1";
                ResultSet resultSet = statement.executeQuery(sql);

                // Convert result set to list of rows
                while (resultSet.next()) {
                    String row = resultSet.getString("title") + ", " + resultSet.getString("author")+ ", " + resultSet.getString("id");
                    System.out.println(row);
                    rows.add(row);
                }
                conn.close();
                // send rows to UI thread for display
                Message message = handler.obtainMessage(0, rows);
                message.sendToTarget();
            } catch (SQLException e) {
                //e.printStackTrace();
                //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    List<String> rows = (List<String>) msg.obj;
                    // Set up adapter to display rows in a list view
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClientMainActivity.this, R.layout.list_item, R.id.list_item_text, rows) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            Button addButton = view.findViewById(R.id.list_item_button);
                            addButton.setTag(rows.get(position));
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String itemId = (String) v.getTag();
                                    cartItems.add(itemId);
                                    System.out.println("added to list, size = ");
                                    System.out.println(cartItems.size());
                                    view.setVisibility(View.GONE); // hide the item view after adding to cart
                                    // TODO is_vailable = 0 cia pakeist, kad ne displayintu paieskoj?
                                }
                            });
                            return view;
                        }
                    };

                    listView.setAdapter(adapter);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    private class SearchTask implements Runnable {
        private String searchText;

        public SearchTask(String searchText) {
            this.searchText = searchText;
        }

        @Override
        public void run() {
            List<String> rows = new ArrayList<>();
            try {
                Connection conn = DBConnect.getConnection();
                // Execute a query to retrieve data
                Statement statement = conn.createStatement();
                String sql = "SELECT * FROM books WHERE title LIKE '%" + searchText + "%' OR author LIKE '%" + searchText + "%'";
                ResultSet resultSet = statement.executeQuery(sql);

                // Convert result set to list of rows
                while (resultSet.next()) {
                    String row = resultSet.getString("title") + " " + resultSet.getString("author");
                    rows.add(row);
                }
                conn.close();
                // send rows to UI thread for display
                Message message = handler.obtainMessage(0, rows);
                message.sendToTarget();
            } catch (SQLException e) {
                //e.printStackTrace();
                //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
            }
        }
    }

}