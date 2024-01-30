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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knygnesys.R;
import com.example.knygnesys.courier.CourierProfileActivity;
import com.example.knygnesys.login.LoginActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddBookActivity extends AppCompatActivity {

    private int branch;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Bundle extras = getIntent().getExtras();
        branch = extras.getInt("Branch");
        EditText title = findViewById(R.id.name);
        EditText author = findViewById(R.id.author);
        Button cancel = findViewById(R.id.back);
        cancel.setOnClickListener(view -> close());
        Button add = findViewById(R.id.add);
        add.setOnClickListener(view -> add(title, author, branch));

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
                redirectActivity(AddBookActivity.this, LoginActivity.class);
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

    private void add(TextView title, TextView author, int branch){
        if(title.getText().toString().length() == 0){
            title.setError("Nepalikite tuščio pavadinimo");
        }
        else if(author.getText().toString().length() == 0){
            author.setError("Nepalikite tuščio autoriaus");
        }
        else {
            CountDownLatch latch = new CountDownLatch(1);
            AddNewBook anb = new AddNewBook(latch, title.getText().toString(),
                    author.getText().toString(), branch);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(anb);
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (anb.result == 0) {
                Toast.makeText(AddBookActivity.this, "Knyga pridėta", Toast.LENGTH_SHORT).show();
                this.close();
            }
            else {
                Toast.makeText(AddBookActivity.this, "Patikrinkite savo interneto ryšį", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void close(){
        this.finish();
    }
}