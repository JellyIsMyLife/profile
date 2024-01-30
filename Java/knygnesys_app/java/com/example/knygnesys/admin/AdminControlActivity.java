package com.example.knygnesys.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;

public class AdminControlActivity extends AppCompatActivity {

    private int userID;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userID = pref.getInt("userId", 0);
        setContentView(R.layout.activity_admin_control);
        Button goEdit = findViewById(R.id.go_edit_users);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> logoutUser());
        goEdit.setOnClickListener(view -> redirectActivity(this, AdminControlUsersActivity.class));
    }

    private void logoutUser(){
        pref.edit().clear().commit();
        redirectActivity(this, LoginActivity.class);
        this.finish();
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("UserID", userID);
        activity.startActivity(intent);
    }
}