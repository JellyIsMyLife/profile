package com.example.knygnesys.client;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;

import org.w3c.dom.Text;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserProfile extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context;
    private ImageView menu;
    LinearLayout order, booksearch, profile, logout;
    private EditText username1, name1, surname1, email1, address1, readersID1, password1;
    boolean isEditModeEnabled;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        username1 = findViewById(R.id.Profileusername);
        name1 = findViewById(R.id.Profilename);
        surname1 = findViewById(R.id.Profilesurname);
        email1 = findViewById(R.id.Profileemail);
        address1 = findViewById(R.id.Profileadress);
        readersID1 = findViewById(R.id.Profilereaders_id);
        password1 = findViewById(R.id.Profilepassword);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        order = findViewById(R.id.orders);
        booksearch = findViewById(R.id.booksearch);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);
        TextView editButton = (TextView) findViewById(R.id.editProfile);


        editButton.setOnClickListener(View -> {
            if (isEditModeEnabled) {
                editProfile(username1.getText().toString(), name1.getText().toString(), surname1.getText().toString(), email1.getText().toString(), address1.getText().toString(), readersID1.getText().toString(), password1.getText().toString(), editButton);

            }
            if (!isEditModeEnabled) {
                enableEdit(editButton);
            }
        });

//        TextView linkEditPage = findViewById(R.id.editProfile);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer(drawerLayout);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(UserProfile.this, OrderActivity.class);
            }
        });
        booksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(UserProfile.this, FirstActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               recreate();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(UserProfile.this, LoginActivity.class);
                finish();
            }
        });
        retrieveUserData();

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

    private void editProfile(String username, String name, String surname, String email, String address, String readersID, String password, TextView editButton) {
        CountDownLatch latch = new CountDownLatch(1);
        ProfileEdit profileEdit = new ProfileEdit(latch, username, name, surname, email, address, password, this);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(profileEdit);

        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", profileEdit.email);
        editor.putString("userPassword", profileEdit.password);
        editor.putString("userName", profileEdit.name);
        editor.putString("userSurname", profileEdit.surname);
        editor.putString("userAddress", profileEdit.address);
        editor.putString("userUsername", profileEdit.userName);
        editor.putString("userPassword", profileEdit.password);
        editor.commit();
        editButton.setText("Keisti duomenis");
        isEditModeEnabled = false;

        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void OpenDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void CloseDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CloseDrawer(drawerLayout);
    }

    private void retrieveUserData() {
        SharedPreferences shared = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
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
    }

}
