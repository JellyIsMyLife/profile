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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.knygnesys.R;
import com.example.knygnesys.courier.Books;
import com.example.knygnesys.courier.ViewOrdersActivity;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/// rodo search rezultatus pagal padalini ir search stringa
public class OrderActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    ImageView menu;
    private ListView listView;
    private Handler handler;
    private Executor executor;
    List<String> items = new ArrayList<>();
    LinearLayout order, booksearch, profile, logout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        order = findViewById(R.id.orders);
        booksearch = findViewById(R.id.booksearch);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer(drawerLayout);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        booksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OrderActivity.this, FirstActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OrderActivity.this, UserProfile.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(OrderActivity.this, LoginActivity.class);
                finish();
            }
        });
        listView = findViewById(R.id.list_view);
        handler = new OrderActivity.UIHandler();
        executor = Executors.newSingleThreadExecutor();

        // Start the search task in a separate thread
        executor.execute(new SearchTask(this));
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.order_list_item, R.id.list_item_text, rows) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            Button addButton = view.findViewById(R.id.list_item_button); // gautas orderis  mygtukas klientui
                            addButton.setTag(rows.get(position));
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String item = (String) v.getTag();
                                    System.out.println("addButton " + item);
                                    Toast.makeText(OrderActivity.this, "Užsakymas gautas", Toast.LENGTH_SHORT).show();
                                    executor.execute(new UpdateOrder(item));
                                    executor.execute(new SearchTask(OrderActivity.this));
                                }
                            });
                            Button booksButton = view.findViewById(R.id.list_item_button_books);
                            booksButton.setTag(rows.get(position));
                            booksButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String itemId = (String) v.getTag();
                                    System.out.println("books button " + itemId);

                                    Intent intent = new Intent(OrderActivity.this, ViewBooksActivity.class);
                                    intent.putExtra("Order", itemId);
                                    startActivity(intent);
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
        Context context;
        String state;
        public SearchTask(Context  c){context=c;}

        @Override
        public void run() {
            List<String> rows = new ArrayList<>();
            try {
                Connection conn = DBConnect.getConnection();
                SharedPreferences shared = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE); // Use the context reference to retrieve shared preferences
                int userId = (shared.getInt("userId", 100));
                Statement statement2 = conn.createStatement();
                String sql = "SELECT * FROM orders WHERE  fk_User = '" + userId+ "' AND fk_State IN (1, 2, 3, 5);";
                ResultSet resultSet2 = statement2.executeQuery(sql);
                System.out.println("executed " + sql);
                // Convert result set to list of rows
                while (resultSet2.next()) {
                    int stateId = resultSet2.getInt("fk_State");
                    Date getDate = resultSet2.getDate("get_date");
                    long daysRemaining = 1000;
                    if(getDate != null && stateId == 4) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(getDate);
                        calendar.add(Calendar.DAY_OF_MONTH, 30);
                        Date currentDate = new Date();
                        long millisecondsPerDay = 24 * 60 * 60 * 1000; // Number of milliseconds in a day
                        daysRemaining = (calendar.getTimeInMillis() - currentDate.getTime()) / millisecondsPerDay;
                    }

                    System.out.println("Days remaining: " + daysRemaining);
                    String days = "-";
                    if (daysRemaining < 999) {
                        days = Long.toString(daysRemaining);
                        days+=" d.";
                    }
                    if (daysRemaining < 0) {
                        days = "grąžinimo terminas praėjo";
                    }

                    Statement statement3 = conn.createStatement();
                    String sql3 = "SELECT * FROM states WHERE  id = '" + stateId+ "'";
                    ResultSet resultSet3 = statement3.executeQuery(sql3);
                    System.out.println("executed " + sql3);
                    // Convert result set to list of rows
                    while (resultSet3.next()) {
                        state = resultSet3.getString("name");
                    }

                    String row = "ID: " + resultSet2.getInt("id") + "\nKnygų skaičius: " + resultSet2.getInt("book_amount")+ "\nBūsena: "+state+ "\nUžsakymo data: " +resultSet2.getDate("created")
                            + "\nIki grąžinimo termino liko: " +days;;
                    rows.add(row);
                    System.out.println(row);
                }
                conn.close();
                // send rows to UI thread for display
                Message message = handler.obtainMessage(0, rows);
                message.sendToTarget();
                } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


        }

    }
    private class UpdateOrder implements Runnable {

        String item;
        public UpdateOrder (String it) {item=it;}
        @Override
        public void run()
        {
            String[] parts = item.split("\n");
            String idString = parts[0];
            System.out.println(idString);
            String[] parts2 = idString.split(" ");
            String id = parts2[1];
            // Connect to the database and update the attribute of the order using the itemId
            try {
                Connection conn = DBConnect.getConnection();
                Statement statement = conn.createStatement();
                String sql = "UPDATE orders SET fk_State = 4 WHERE id = '" + id + "'";
                statement.executeUpdate(sql);

                Statement statement2 = conn.createStatement();
                String sql2 = "SELECT * FROM orders WHERE id = '" + id + "'";
                ResultSet resultSet2 = statement2.executeQuery(sql2);
                // Convert result set to list of rows
                while (resultSet2.next()) {
                    int courier_id = resultSet2.getInt("courier_id");
                    Statement statement3 = conn.createStatement();
                    String sql3 = "UPDATE users SET points = points + 1 WHERE id = '" + courier_id + "'";
                    statement3.executeUpdate(sql3);
                }

                conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}