package com.example.knygnesys.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.DBConnect;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/// pirmas puslapis kuri atidaro klientui su search ir padalinio pasirinkimu, search mygtuku.
public class FirstActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private final LatLng BIBLIOTEKA1 = new LatLng(54.91985981397765, 24.056798008781037); //done
    private final LatLng BIBLIOTEKA2 = new LatLng(54.93584587178002, 23.982398511373432); //done
    private final LatLng BIBLIOTEKA3 = new LatLng(54.898456314715425, 23.893366084656147);//done
    private final LatLng BIBLIOTEKA4 = new LatLng(54.88465263407161, 23.895242830687707); //done
    private final LatLng BIBLIOTEKA5 = new LatLng(54.90484424184995, 23.925271626984227);//done
    private final LatLng BIBLIOTEKA6 = new LatLng(54.91674799620538, 23.981632); //done
    private final LatLng BIBLIOTEKA7 = new LatLng(54.925740226861606, 23.963770592593043); //done
    private final LatLng BIBLIOTEKA8 = new LatLng(54.930828485050675, 23.935523); //done
    private final LatLng BIBLIOTEKA9 = new LatLng(54.90715945494128, 23.975078746031553); //done
    private final LatLng BIBLIOTEKA10 = new LatLng(54.91557134038954, 23.9443051693123); //done
    private final LatLng BIBLIOTEKA11 = new LatLng(54.9137103580693, 23.89261591534385);//doen
    private final LatLng BIBLIOTEKA12 = new LatLng(54.863796017466996, 23.96416089947006);//done
    private final LatLng BIBLIOTEKA13 = new LatLng(54.92680523365658, 23.94794825396845);//done
    private final LatLng BIBLIOTEKA14 = new LatLng(54.9267312532323, 23.94794825396845);//done
    private final LatLng BIBLIOTEKA15 = new LatLng(54.92137361265956, 23.828121169312293);//done
    private final LatLng BIBLIOTEKA16 = new LatLng(54.871381760091126, 23.94363525396845);//done
    private final LatLng BIBLIOTEKA17 = new LatLng(54.92726010214184, 23.883558652510484);//done
    private final LatLng BIBLIOTEKA18 = new LatLng(54.86166245186181, 23.868612507936895);//done
    private final LatLng BIBLIOTEKA19 = new LatLng(54.84038834103119, 23.942244015873793);//done
    private final LatLng BIBLIOTEKA20 = new LatLng(54.90329748133889, 23.906106423280747);//done

    private Marker markerBiblioteka1;
    private Marker markerBiblioteka2;
    private Marker markerBiblioteka3;
    private Marker markerBiblioteka4;
    private Marker markerBiblioteka5;
    private Marker markerBiblioteka6;
    private Marker markerBiblioteka7;
    private Marker markerBiblioteka8;
    private Marker markerBiblioteka9;
    private Marker markerBiblioteka10;
    private Marker markerBiblioteka11;
    private Marker markerBiblioteka12;
    private Marker markerBiblioteka13;
    private Marker markerBiblioteka14;
    private Marker markerBiblioteka15;
    private Marker markerBiblioteka16;
    private Marker markerBiblioteka17;
    private Marker markerBiblioteka18;
    private Marker markerBiblioteka19;
    private Marker markerBiblioteka20;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText searchEditText;
    private Spinner branchSpinner;
    private Button searchButton;
    private Executor executor;

    ImageView  menu;
    LinearLayout profile, bookSearch, orders, logout;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);
        mapFragment.getMapAsync(this);

        drawerLayout= findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        bookSearch = findViewById(R.id.booksearch);
        orders = findViewById(R.id.orders);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer(drawerLayout);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(FirstActivity.this, UserProfile.class);
            }
        });
        bookSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               recreate();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(FirstActivity.this, OrderActivity.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(FirstActivity.this, LoginActivity.class);
                finish();
            }
        });

        // Initialize UI elements
        searchEditText = findViewById(R.id.search_text);
        branchSpinner = findViewById(R.id.spinner_branch);
        searchButton = findViewById(R.id.search_button);

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new BranchesRunnable());

        // Set OnClickListener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the search string and branch selected
                String searchString = searchEditText.getText().toString();
                String selectedBranch = branchSpinner.getSelectedItem().toString();

                BranchesId getid = new BranchesId(selectedBranch);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(getid);




                // Pass the search string and branch selected to the second activity
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                intent.putExtra("searchString", searchString);
                intent.putExtra("selectedBranch", selectedBranch);
                intent.putExtra("selectedBranchId", getid.id);
                System.out.println("perduoodamas id = ");
                System.out.println(getid.id);
                startActivity(intent);
            }
        });

    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
// Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        float zoomLevel = 10.0f; //This goes up to 21
        LatLng sydney = new LatLng(54.901025627420665, 23.913060503512643);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Vartotojų aptarnavimo skyrius"));
        markerBiblioteka1 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA1)
                .title("Palemono padalinys"));
        markerBiblioteka1.setTag(0);
        markerBiblioteka2 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA2)
                .title("Vaikų literatūros skyrius"));
        markerBiblioteka2.setTag(0);
        markerBiblioteka3 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA3)
                .title("Meno ir muzikos skyrius"));
        markerBiblioteka3.setTag(0);
        markerBiblioteka4 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA4)
                .title("Aleksoto padalinys"));
        markerBiblioteka4.setTag(0);
        markerBiblioteka5 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA5)
                .title("„Aušros“ padalinys"));
        markerBiblioteka5.setTag(0);
        markerBiblioteka6 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA6)
                .title("„Berželio“ padalinys"));
        markerBiblioteka6.setTag(0);
        markerBiblioteka7 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA7)
                .title("Dainavos padalinys"));
        markerBiblioteka7.setTag(0);
        markerBiblioteka8 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA8)
                .title("Eigulių padalinys"));
        markerBiblioteka8.setTag(0);
        markerBiblioteka9 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA9)
                .title("Girstupio padalinys"));
        markerBiblioteka9.setTag(0);
        markerBiblioteka10 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA10)
                .title("Kalniečių padalinys"));
        markerBiblioteka10.setTag(0);
        markerBiblioteka11 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA11)
                .title(
                        "Neries padalinys"));
        markerBiblioteka11.setTag(0);
        markerBiblioteka12 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA12)
                .title("Panemunės padalinys"));
        markerBiblioteka12.setTag(0);
        markerBiblioteka13 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA13)
                .title("Parko padalinys"));
        markerBiblioteka13.setTag(0);
        markerBiblioteka14 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA14)
                .title("Petrašiūnų padalinys"));
        markerBiblioteka14.setTag(0);
        markerBiblioteka15 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA15)
                .title("„Šaltinio“ padalinys"));
        markerBiblioteka15.setTag(0);
        markerBiblioteka16 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA16)
                .title("Šančių padalinys"));
        markerBiblioteka16.setTag(0);
        markerBiblioteka17 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA17)
                .title("Šilainių padalinys"));
        markerBiblioteka17.setTag(0);
        markerBiblioteka18 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA18)
                .title("Tirkiliškių padalinys"));
        markerBiblioteka18.setTag(0);
        markerBiblioteka19 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA19)
                .title("Vingytės padalinys"));
        markerBiblioteka19.setTag(0);
        markerBiblioteka20 = googleMap.addMarker(new MarkerOptions()
                .position(BIBLIOTEKA20)
                .title("Z. Kuzmickio padalinys"));
        markerBiblioteka20.setTag(0);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
    }

    public static void OpenDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void CloseDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CloseDrawer(drawerLayout);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }



    public class BranchesRunnable implements Runnable {
        private List<String> branches;

        @Override
        public void run() {
            Connection connection = null;
            try {
                connection = DBConnect.getConnection();
                if (connection != null) {
                    String query = "SELECT name FROM branches";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    branches = DBConnect.resultSetToList(rs);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Populate the spinner with branches
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    FirstActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    branches
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            branchSpinner.setAdapter(adapter);
                        }
                    });
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }

    public class BranchesId implements Runnable {
        int id;
        String title;

        public BranchesId(String t)
        {
            title = t;
        }

        @Override
        public void run() {
            Connection connection = null;
            try {
                connection = DBConnect.getConnection();
                if (connection != null) {
                    String query = "SELECT id FROM branches where name = " + title + "'";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    System.out.println("ivykdytas   query ");
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                    System.out.println("==============gautas id = ");
                    System.out.println(id);
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
}
