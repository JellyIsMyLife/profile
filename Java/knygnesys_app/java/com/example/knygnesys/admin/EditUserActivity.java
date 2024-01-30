package com.example.knygnesys.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knygnesys.R;
import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.User;
import com.example.knygnesys.worker.EditBookActivity;
import com.example.knygnesys.worker.GetBookInfo;
import com.example.knygnesys.worker.TryToEdit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] roles = {"Klientas", "Kurjeris", "Darbuotojas"};
    private int userID;
    private int pos = 0;

    private EditText branches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
        Button cancel = findViewById(R.id.back);
        cancel.setOnClickListener(view -> close());
        User user = getUser(userID);
        TextView title = findViewById(R.id.name);
        TextView author = findViewById(R.id.email);
        pos = user.getFkRole() - 2;
        title.append(user.getUsername() + " {" + userID + "}");
        author.append(user.getEmail());

        Spinner spinner = findViewById(R.id.roles);
        branches = findViewById(R.id.branches);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        branches.append("0");
        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> save(pos, Integer.parseInt(branches.getText().toString())));
    }

    private User getUser(int uID){
        User temp = null;
        CountDownLatch latch = new CountDownLatch(1);
        GetUser gu = new GetUser(latch, uID);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gu);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (gu.result == 1) {
            temp = gu.user;
        }
        else if (gu.result == 2){
            Toast.makeText(EditUserActivity.this, "Vartotojas nebeegzistuoja", Toast.LENGTH_SHORT).show();
            this.close();
        }
        else {
            Toast.makeText(EditUserActivity.this, "Patikrinkite savo interneto ryšį", Toast.LENGTH_SHORT).show();
        }
        return temp;
    }

    private void save(int r, int b){
        if(r == 4 && ( b < 1 || b > 15 )){
            branches.setError("Padaliniai nuo 1 iki 15");
        }
        else {
            if (r != 4){
                b = 0;
            }
            CountDownLatch latch = new CountDownLatch(1);
            EditUser eu = new EditUser(latch, r, b, userID);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(eu);
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (eu.result == 0) {
                Toast.makeText(EditUserActivity.this, "Vartotojo rolė pakeista", Toast.LENGTH_SHORT).show();
                this.close();
            }
            else if (eu.result == 1){
                Toast.makeText(EditUserActivity.this, "Vartotojas nebeegzistuoja", Toast.LENGTH_SHORT).show();
                this.close();
            }
            else {
                Toast.makeText(EditUserActivity.this, "Patikrinkite savo interneto ryšį", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void close(){
        this.finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        pos = position + 2;
        if(pos == 4) {
            branches.setVisibility(View.VISIBLE);
        }
        else {
            branches.setVisibility(View.INVISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }
}