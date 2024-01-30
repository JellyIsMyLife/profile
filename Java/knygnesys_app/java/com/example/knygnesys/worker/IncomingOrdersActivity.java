package com.example.knygnesys.worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.Order;
import com.example.knygnesys.utils.WorkerOrderListAdapter;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IncomingOrdersActivity extends AppCompatActivity {

    private int branch;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        branch = extras.getInt("Branch");
        setContentView(R.layout.activity_incoming_orders);
        listView = findViewById(R.id.list_orders);
        Button oldOrders = findViewById(R.id.orders_history);
        Button back = findViewById(R.id.back);
        listOrders();
        oldOrders.setOnClickListener(view -> redirectActivity(this, CompleteOrdersActivity.class, branch));
        back.setOnClickListener(view -> close());

        drawerLayout = findViewById(R.id.worker_drawer_layout);
        menu = findViewById(R.id.menu);
        logout = findViewById(R.id.logout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer(drawerLayout);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(IncomingOrdersActivity.this, LoginActivity.class);
                finish();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        listOrders();
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
    }

    private static void OpenDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity, int br) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Branch", br);
        activity.startActivity(intent);
    }

    public void close(){
        this.finish();
    }

    public void listOrders() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        GetOrders go = new GetOrders(latch, branch);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(go);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        ArrayList<Order> orderlist = go.result;
        WorkerOrderListAdapter adapter = new WorkerOrderListAdapter(this, R.layout.list_order, orderlist);
        listView.setAdapter(adapter);
        }
}