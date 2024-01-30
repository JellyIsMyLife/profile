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
import android.widget.ListView;

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.BookListAdapter;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewBooksListActivity extends AppCompatActivity {

    private int userID;
    private int branch;
    private SharedPreferences pref;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userID = pref.getInt("userId", 0);
        branch = pref.getInt("userBranchID", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_list);
        Button oldOrders = findViewById(R.id.to_orders);
        Button addBook = findViewById(R.id.add_book);
        Button searchBut = findViewById(R.id.search_button);
        listView = findViewById(R.id.list_view);
        EditText search = findViewById(R.id.search);
        listBooks(search.getText().toString());
        searchBut.setOnClickListener(view -> updateList(search));
        addBook.setOnClickListener(view -> redirectActivity(this, AddBookActivity.class, branch));
        oldOrders.setOnClickListener(view -> redirectActivity(this, IncomingOrdersActivity.class, branch));

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
                redirectActivity(ViewBooksListActivity.this, LoginActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EditText search = findViewById(R.id.search);
        updateList(search);
    }

    private static void OpenDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void updateList(EditText text){
        listBooks(text.getText().toString());
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity, int br) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Branch", br);
        activity.startActivity(intent);
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
    }

    public void close(){
        pref.edit().clear().commit();
        redirectActivity(this, LoginActivity.class);
        this.finish();
    }

    public void listBooks(String title) {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        GetBooksDetailed gbd = new GetBooksDetailed(latch, branch, title);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gbd);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        ArrayList<Book> booklist = gbd.result;
        BookListAdapter adapter = new BookListAdapter(this, R.layout.list_edit_books, booklist);
        listView.setAdapter(adapter);
    }
}