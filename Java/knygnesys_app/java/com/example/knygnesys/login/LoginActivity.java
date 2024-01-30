package com.example.knygnesys.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knygnesys.R;
import com.example.knygnesys.admin.AdminControlActivity;
import com.example.knygnesys.client.FirstActivity;
import com.example.knygnesys.courier.DoingOrders;
import com.example.knygnesys.register.RegisterActivity;
import com.example.knygnesys.worker.ViewBooksListActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    TextView email;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginbtn = findViewById(R.id.loginbutton);
        TextView linkreg = findViewById(R.id.linkregister);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // check if user is already logged in
        if (isLoggedIn()) {
            redirectToMainPage();
        }

        // check login info
        loginbtn.setOnClickListener(view -> login(email,password));

        // go to register
        linkreg.setOnClickListener(view -> changeView(RegisterActivity.class));
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPrefs.contains("userId");
    }

    private void redirectToMainPage() {
        SharedPreferences sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int role = sharedPrefs.getInt("userRole", 0);

        Intent intent;
        if (role == 1) {
            intent = new Intent(LoginActivity.this, AdminControlActivity.class);
        } else if (role == 2) {
            intent = new Intent(LoginActivity.this, FirstActivity.class);
        } else if (role == 3) {
            intent = new Intent(LoginActivity.this, DoingOrders.class);
        } else {
            intent = new Intent(LoginActivity.this, ViewBooksListActivity.class);
        }

        startActivity(intent);
        finish(); // finish LoginActivity to prevent navigating back to it
    }

    private void login(TextView email, TextView password){
        {
            if(email.getText().toString().length() < 1 && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                email.setError("Email is not valid");
            }
            else if(password.getText().toString().length() < 5){
                password.setError("Password length needs to be at least 5 symbols");
            }
            else {
                CountDownLatch latch = new CountDownLatch(1);
                CheckLogin cl = new CheckLogin(latch, email, password);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(cl);
                try {
                    latch.await();
                } catch (Exception e) {
                    System.out.println("idk");
                }
                if (cl.result == 0) {
                    SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("userId", cl.id);
                    editor.putInt("userRole", cl.role);
                    editor.putString("userEmail", cl.email1);
                    editor.putString("userPassword", cl.password1);
                    editor.putString("userName", cl.name);
                    editor.putString("userSurname", cl.surname);
                    editor.putString("userAddress", cl.address);
                    editor.putString("userUsername", cl.username);
                    editor.putString("userReadersID", cl.readersID);
                    editor.putString("userPassword", cl.password1);
                    editor.putInt("userBranchID", cl.branch);
                    editor.commit();

                    redirectToMainPage();
                }
                else if (cl.result == 1){
                    password.setError("Wrong password");
                }
                else if (cl.result == 2){
                    email.setError("No user by such email");
                }
                else {
                    Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void changeView(final Class<? extends Activity> to){
        Intent intent = new Intent(LoginActivity.this, to);
        startActivity(intent);
    }
}