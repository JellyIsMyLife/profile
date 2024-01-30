package com.example.knygnesys.courier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.knygnesys.R;
import com.example.knygnesys.client.FirstActivity;
import com.example.knygnesys.client.OrderActivity;
import com.example.knygnesys.client.SecondActivity;
import com.example.knygnesys.client.UserProfile;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.CourierAdapter;
import com.example.knygnesys.utils.Order;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DoingOrders extends AppCompatActivity {

    private final int branch = 1;
    private int orderState = 1; // Default state is 1

    private ListView listView;

    private DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainpage, myorders, profile,logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: logged in check, role check, branch check.
        setContentView(R.layout.activity_free_orders);

        drawerLayout= findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        profile = findViewById(R.id.profile);
        mainpage = findViewById(R.id.mainPage);
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
                redirectActivity(DoingOrders.this, CourierProfileActivity.class);
            }
        });


        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(DoingOrders.this, DoingOrders.class);
            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(DoingOrders.this, LoginActivity.class);
                finish();
            }
        });

        listView = findViewById(R.id.list_orders);

        Button myOrdersButton = findViewById(R.id.my_orders_button);
        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderState == 1) {
                    orderState = 2; // Set the order state to 2
                    listOrders();

                    TextView textView = findViewById(R.id.editTextText);
                    textView.setText("Vykdomi Užsakymai"); // Change the text dynamically

                    myOrdersButton.setText("Atgal"); // Change the button text to "Atgal"
                } else if (orderState == 2) {
                    orderState = 1; // Set the order state back to 1
                    listOrders();

                    TextView textView = findViewById(R.id.editTextText);
                    textView.setText("Laisvi Užsakymai"); // Change the text dynamically

                    myOrdersButton.setText("Mano Užsakymai"); // Change the button text back to "Mano Užsakymai"
                }
            }
        });


        listOrders();
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

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void listOrders() {
        // Get the logged-in user's id from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int loggedInUserId = sharedPreferences.getInt("userId", 0); // Retrieve the logged-in user ID

        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        Orders go = new Orders(latch, branch, orderState, loggedInUserId, this);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(go);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        ArrayList<Order> orderlist = go.result;
        CourierAdapter adapter = new CourierAdapter(this, R.layout.list_orderss, orderlist, orderState); // Pass the orderState to the adapter constructor
        listView.setAdapter(adapter);
    }
}



