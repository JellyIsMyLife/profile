package com.example.knygnesys.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knygnesys.R;
import com.example.knygnesys.client.FirstActivity;
import com.example.knygnesys.login.LoginActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView username = (TextView) findViewById(R.id.username);
        TextView name = (TextView) findViewById(R.id.name);
        TextView secondName = (TextView) findViewById(R.id.secondName);
        TextView address = (TextView) findViewById(R.id.address);
        TextView email = (TextView) findViewById(R.id.email);
        TextView readers_id = (TextView) findViewById(R.id.readers_id);
        TextView password = (TextView) findViewById(R.id.password);
        TextView password2 = (TextView) findViewById(R.id.password2);
        int r = 2; // 1 - admin, 2 - client, 3 - courier, 4 - employee
        //if (((Switch)findViewById(R.id.role)).isChecked()){
        //    r = 3;
        //}


        int role = getRoleFromSwitch(); // asked for final int, idk
        Button register = (Button) findViewById(R.id.registerbutton);
        TextView linklog = (TextView) findViewById(R.id.linklogin);

        // registers new account
        register.setOnClickListener(View -> register(username, name, secondName, address, email, password, password2, role, readers_id));
        // changes view to Login
        linklog.setOnClickListener(View -> changeView(LoginActivity.class));
    }

    private void register(TextView username, TextView name, TextView secondname, TextView adrs, TextView email, TextView pass, TextView pass2, int role,TextView rid){
        role = getRoleFromSwitch();
        if(username.getText().toString().length() < 5){
            username.setError("Username length needs to be at least 5 characters");
        }
        else if(name.getText().toString().length() < 1){
            name.setError("Name can not be empty");
        }
        else if(name.getText().toString().length() < 1){
            secondname.setError("Name can not be empty");
        }
        else if(email.getText().toString().length() < 1 && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Email is not valid");
        }
        else if(adrs.getText().toString().length() < 5){
            adrs.setError("Address is not valid");
        }
        else if(pass.getText().toString().length() < 5){
            pass.setError("Password length needs to be at least 5 symbols");
        }
        else if(!pass.getText().toString().equals(pass2.getText().toString())){
            pass2.setError("Passwords don't match");
        }
        else {
            CountDownLatch latch = new CountDownLatch(1);
            TryToRegister tr = new TryToRegister(latch,
                    username.getText().toString(), name.getText().toString(),
                    secondname.getText().toString(), pass.getText().toString(),
                    adrs.getText().toString(), email.getText().toString(), role, rid.getText().toString());
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(tr);
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (tr.result == 0) {
                Intent intent = new Intent(RegisterActivity.this, FirstActivity.class);
                intent.putExtra("user", username.getText().toString());
                intent.putExtra("password", pass.getText().toString());
                startActivity(intent);
            }
            else if (tr.result == 1){
                email.setError("Email already in use");
            }
            else {
                Toast.makeText(RegisterActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeView(final Class<? extends Activity> to){
        Intent intent = new Intent(RegisterActivity.this, to);
        startActivity(intent);
    }

    private int getRoleFromSwitch() {
        // Get a reference to the Switch view
        Switch mySwitch = findViewById(R.id.role);

        // Return the current value of the Switch
        return mySwitch.isChecked() ? 3 : 2;
    }

}