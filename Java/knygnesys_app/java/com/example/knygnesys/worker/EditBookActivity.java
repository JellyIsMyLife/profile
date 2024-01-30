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
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.Book;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditBookActivity extends AppCompatActivity {

    private int bookID;
    private int branchID;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        Bundle extras = getIntent().getExtras();
        bookID = extras.getInt("bookID");
        branchID = extras.getInt("branchID");
        Button cancel = findViewById(R.id.back);
        cancel.setOnClickListener(view -> close());
        Book book = getBook(bookID);
        EditText title = findViewById(R.id.name);
        EditText author = findViewById(R.id.author);
        title.append(book.getTitle());
        author.append(book.getAuthor());
        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> edit(title, author));

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
                redirectActivity(EditBookActivity.this, LoginActivity.class);
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

    private Book getBook(int bID){
        Book temp = null;
        CountDownLatch latch = new CountDownLatch(1);
        GetBookInfo gbi = new GetBookInfo(latch, bID);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gbi);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (gbi.result == 1) {
            temp = gbi.book;
        }
        else if (gbi.result == 2){
            Toast.makeText(EditBookActivity.this, "Knyga nebeegzistuoja", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(EditBookActivity.this, "Patikrinkite savo interneto ryšį", Toast.LENGTH_SHORT).show();
        }
        return temp;
    }

    private void edit(TextView title, TextView author){
        if(title.getText().toString().length() == 0){
            title.setError("Nepalikite tuščio pavadinimo");
        }
        else if(author.getText().toString().length() == 0){
            author.setError("Nepalikite tuščio autoriaus");
        }
        else {
            CountDownLatch latch = new CountDownLatch(1);
            TryToEdit tte = new TryToEdit(latch, title.getText().toString(),
                    author.getText().toString(), bookID);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(tte);
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (tte.result == 0) {
                Toast.makeText(EditBookActivity.this, "Knygos informacija pakeista", Toast.LENGTH_SHORT).show();
                this.close();
            }
            else if (tte.result == 1){
                title.setError("Tokia knyga nebeegzistuoja");
            }
            else {
                Toast.makeText(EditBookActivity.this, "Patikrinkite savo interneto ryšį", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void close(){
        this.finish();
    }
}