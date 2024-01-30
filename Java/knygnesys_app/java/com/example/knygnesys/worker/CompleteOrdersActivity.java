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
import com.example.knygnesys.utils.WorkerCompleteOrderListAdapter;
import com.example.knygnesys.utils.WorkerOrderListAdapter;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CompleteOrdersActivity extends AppCompatActivity {

    private int Branch;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_orders);
        Bundle extras = getIntent().getExtras();
        Branch = extras.getInt("Branch");
        listView = findViewById(R.id.list_orders);
        listOrders();
        Button back = findViewById(R.id.back);
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
                redirectActivity(CompleteOrdersActivity.this, LoginActivity.class);
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

    public void close(){
        this.finish();
    }

    public void listOrders() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        GetCompletedOrders gco = new GetCompletedOrders(latch, Branch);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gco);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        ArrayList<Order> orderlist = gco.result;
        WorkerCompleteOrderListAdapter adapter = new WorkerCompleteOrderListAdapter(this, R.layout.list_complete_order, orderlist);
        listView.setAdapter(adapter);
    }
}