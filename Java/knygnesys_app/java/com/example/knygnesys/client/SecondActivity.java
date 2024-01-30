package com.example.knygnesys.client;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.knygnesys.R;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/// rodo search rezultatus pagal padalini ir search stringa
public class SecondActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout order, booksearch, profile,logout;
    SharedPreferences pref;

    private ListView listView;
    private Handler handler;
    private Executor executor;

    ArrayList<String> cartItems; // Declare cartItems

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Set<String> cartItemsSet = preferences.getStringSet("cartItems", null);
        cartItems = new ArrayList<>(); // Initialize cartItems

        if (cartItemsSet != null) {
            cartItems.addAll(cartItemsSet);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        drawerLayout= findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        profile = findViewById(R.id.profile);
        order = findViewById(R.id.orders);
        booksearch = findViewById(R.id.booksearch);
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
                redirectActivity(SecondActivity.this, UserProfile.class);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SecondActivity.this, OrderActivity.class);
            }
        });
        booksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SecondActivity.this, FirstActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(SecondActivity.this, LoginActivity.class);
                finish();
            }
        });

        String search = getIntent().getStringExtra("searchString");
        String branch = getIntent().getStringExtra("selectedBranch");
        int branchid = getIntent().getIntExtra("selectedBranchId",0);
        listView = findViewById(R.id.list_view);
        handler = new SecondActivity.UIHandler();
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new SearchTask(search,branch));
        Button cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent to open the cart activity
                Intent intent = new Intent(SecondActivity.this, CartActivity.class);
                // Add the cart items to the intent as an extra
                intent.putStringArrayListExtra("cartItems", (ArrayList<String>) cartItems);
                // Start the cart activity
                startActivity(intent);
            }
        });


    }
    public static void OpenDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void CloseDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
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
    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    List<String> rows = (List<String>) msg.obj;
                    // Set up adapter to display rows in a list view
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivity.this, R.layout.list_item, R.id.list_item_text, rows) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            Button addButton = view.findViewById(R.id.list_item_button);
                            addButton.setTag(rows.get(position));
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String itemId = (String) v.getTag();
                                    cartItems.add(itemId);
                                    System.out.println("added to list, size = ");
                                    System.out.println(cartItems.size());
                                    view.setVisibility(View.GONE); // hide the item view after adding to cart

                                    SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putStringSet("cartItems", new HashSet<>(cartItems));
                                    editor.apply();


                                }
                            });
                            return view;
                        }
                    };

                    listView.setAdapter(adapter);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    private class SearchTask implements Runnable {
        private String searchText;
        private String selectedBranch;

        public SearchTask(String st, String sb) {

            this.searchText = st;
            this.selectedBranch =sb;
        }

        @Override
        public void run() {
            List<String> rows = new ArrayList<>();
            try {
                Connection conn = DBConnect.getConnection();
                // Execute a query to retrieve data
                Statement statement = conn.createStatement();
                String query = "SELECT id FROM branches WHERE name = '" + selectedBranch + "'";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int  id = resultSet.getInt(1);
                    SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("branchId", id);
                    editor.commit();
                    System.out.println("executed " + query);
                    System.out.println(id);

                    Statement statement2 = conn.createStatement();
                    // TODO and is_available=1
                    String sql = "SELECT * FROM books WHERE (title LIKE '%" + searchText+ "%' OR author LIKE '%" + searchText + "%') AND fk_Branch = '" + id+ "'";

                    ResultSet resultSet2 = statement2.executeQuery(sql);
                    System.out.println("executed " + sql);
                    // Convert result set to list of rows
                    while (resultSet2.next()) {
                        String row = resultSet2.getString("title") + ", " + resultSet2.getString("author")+ ", "+ resultSet2.getString("id");;
                        rows.add(row);
                        System.out.println(row);
                    }

                }
                conn.close();
                // send rows to UI thread for display
                Message message = handler.obtainMessage(0, rows);
                message.sendToTarget();
            } catch (SQLException e) {
                //e.printStackTrace();
                //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}