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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewOrderActivity extends AppCompatActivity {

    private int OrderID;
    private int Branch;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO check for logged in, role.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle extras = getIntent().getExtras();
        listView = findViewById(R.id.list_books);
        OrderID = extras.getInt("OrderID");
        Branch = extras.getInt("Branch");
        listBooks();
        Button back = findViewById(R.id.back_button);
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
                redirectActivity(ViewOrderActivity.this, LoginActivity.class);
                finish();
            }
        });
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

    public void listBooks() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        GetBooks gb = new GetBooks(latch, OrderID);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gb);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        List<String> rows = gb.result;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewOrderActivity.this, R.layout.list_string_row, R.id.list_string_text, rows);
        listView.setAdapter(adapter);
    }
}