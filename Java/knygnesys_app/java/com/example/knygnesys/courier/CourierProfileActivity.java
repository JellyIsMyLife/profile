package com.example.knygnesys.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.knygnesys.R;
import com.example.knygnesys.client.ProfileEdit;
import com.example.knygnesys.client.UserProfile;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourierProfileActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context;
    private ImageView menu;
    LinearLayout availableorders, mainpage, logout, profile;
    private EditText username1, name1, surname1, email1, address1, readersID1, password1;

    boolean isEditModeEnabled;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_profile);

        username1 = findViewById(R.id.Courierusername);
        name1 = findViewById(R.id.Couriername);
        surname1 = findViewById(R.id.Couriersurname);
        email1 = findViewById(R.id.Courieremail);
        address1 = findViewById(R.id.Courieradress);
        readersID1 = findViewById(R.id.Courierreaders_id);
        password1 = findViewById(R.id.Courierpassword);

        drawerLayout = findViewById(R.id.courier_drawer_layout);
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
                redirectActivity(CourierProfileActivity.this, CourierProfileActivity.class);
            }
        });

        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CourierProfileActivity.this, DoingOrders.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(CourierProfileActivity.this, LoginActivity.class);
                finish();
            }
        });

        TextView editButton = findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditModeEnabled) {
                    CourierProfileEdit(username1.getText().toString(), name1.getText().toString(), surname1.getText().toString(), email1.getText().toString(), address1.getText().toString(), readersID1.getText().toString(), password1.getText().toString(), editButton);
                } else {
                    enableEdit(editButton);
                }
            }
        });

        retrieveUserData();
    }

    public static void OpenDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void CloseDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CloseDrawer(drawerLayout);
    }

    private void enableEdit(TextView editButton) {
        username1.setEnabled(true);
        name1.setEnabled(true);
        surname1.setEnabled(true);
        email1.setEnabled(true);
        address1.setEnabled(true);
        readersID1.setEnabled(true);
        password1.setEnabled(true);

        isEditModeEnabled = true;

        editButton.setText("Išsaugoti");
    }

    private void CourierProfileEdit(String username, String name, String surname, String email, String address, String readersID, String password, TextView editButton) {
        CountDownLatch latch = new CountDownLatch(1);
        CourierProfileEdit profileEdit = new CourierProfileEdit(latch, username, name, surname, email, address, password, this);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(profileEdit);

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", profileEdit.email);
        editor.putString("userPassword", profileEdit.password);
        editor.putString("userName", profileEdit.name);
        editor.putString("userSurname", profileEdit.surname);
        editor.putString("userAddress", profileEdit.address);
        editor.putString("userUsername", profileEdit.userName);
        editor.commit();

        editButton.setText("Keisti duomenis");
        isEditModeEnabled = false;

        Intent intent = new Intent(this, CourierProfileActivity.class);
        startActivity(intent);
    }

    private void retrieveUserData() {
        SharedPreferences shared = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int userId = shared.getInt("userId", 0); // Retrieve the logged-in user ID
        String username = shared.getString("userUsername", "");
        String name = shared.getString("userName", "");
        String surname = shared.getString("userSurname", "");
        String email = shared.getString("userEmail", "");
        String address = shared.getString("userAddress", "");
        String readersID = shared.getString("userReadersID", "");
        String password = shared.getString("userPassword", "");

        username1.setText(username);
        name1.setText(name);
        surname1.setText(surname);
        email1.setText(email);
        address1.setText(address);
        readersID1.setText(readersID);
        password1.setText(password);

        TextView pointsTextView = findViewById(R.id.CourierPoints);

        Users users = new Users(userId, pointsTextView, CourierProfileActivity.this);
        Thread thread = new Thread(users);
        thread.start();
    }

}

