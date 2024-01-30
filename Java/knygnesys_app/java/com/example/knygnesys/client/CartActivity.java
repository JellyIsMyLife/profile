package com.example.knygnesys.client;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.knygnesys.R;
import com.example.knygnesys.login.CheckLogin;
import com.example.knygnesys.login.LoginActivity;
import com.example.knygnesys.utils.DBConnect;
import com.example.knygnesys.worker.EditBookActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {
    Spinner spinner;
    private ListView listView;
    private EditText dateField;
    String date = "";
    private EditText addressEditText;

    private DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, bookSearch, cart, orders, logout;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cart_list_view);
        Button updateButton = findViewById(R.id.update_button);
        spinner = findViewById(R.id.spinner_field);
        String[] items = {"Rytas", "Diena", "Vakaras"};
        ArrayAdapter<String> sadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sadapter);
        drawerLayout= findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        bookSearch = findViewById(R.id.booksearch);
        orders =findViewById(R.id.orders);
        logout=findViewById(R.id.logout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer(drawerLayout);
            }
        });
        bookSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CartActivity.this, FirstActivity.class);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CartActivity.this, OrderActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
                pref.edit().clear().commit();
                redirectActivity(CartActivity.this, LoginActivity.class);
                finish();
            }
        });
        List<String> cartItems = getIntent().getStringArrayListExtra("cartItems");

        // Set up adapter to display cart items in a list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cartItems);

        listView.setAdapter(adapter);
        addressEditText=findViewById(R.id.address_field);
        dateField = findViewById(R.id.date_field);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        updateButton.setOnClickListener(View -> update(cartItems));

        TextView cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("cartItems");
                editor.apply();
                redirectActivity(CartActivity.this, FirstActivity.class);
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
    private void update(List<String> cartItems){
        String[] parts = cartItems.get(0).split(",");
        String firstItemId = parts[2];
        int branchId = getBranchIdFromDatabase(firstItemId);

        // Check if all cart items have the same branch ID
        boolean sameBranch = true;
        for (String item : cartItems) {
            parts = item.split(",");
            String itemId = parts[2];
            int itemBranchId = getBranchIdFromDatabase(itemId);
            if (itemBranchId != branchId) {
                sameBranch = false;
                break;
            }
        }
        String addressString = addressEditText.getText().toString();
        String selectedTime = spinner.getSelectedItem().toString();
        if (!sameBranch) {
            System.out.println("sKIRTINGI PADALINIAI");
            Toast.makeText(CartActivity.this, "Negalima užsakyti iš skirtingų padalinių", Toast.LENGTH_SHORT).show();
        } else if (addressString.equals("") || selectedTime.equals("") || date.equals("")) {
            Toast.makeText(CartActivity.this, "Trūksta informacijos", Toast.LENGTH_SHORT).show();
        }

        else {

            System.out.println("selectedTime");
            System.out.println(selectedTime);
            CountDownLatch latch = new CountDownLatch(1);
            CreateOrder co = new CreateOrder(latch, cartItems,  addressString, date, selectedTime, this);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(co);
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println("exception");
            }
            if (co.result == 0) {
                System.out.println("order       id");
                System.out.println(co.orderId);
                SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("cartItems");
                editor.apply();
                // Create an Intent to navigate to the next activity
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                // Pass any necessary data to the next activity using extras
                intent.putExtra("orderId", co.orderId);
                // Start the next activity
                startActivity(intent);
                // Finish the current activity if needed
                finish();
            }
        }

    }

    private int getBranchIdFromDatabase(String itemId) {
        GetBranchIdRunnable runnable = new GetBranchIdRunnable(itemId);
        Thread thread = new Thread(runnable);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return runnable.getBranchId();
    }





    private class CartAdapter extends ArrayAdapter<String> {

        public CartAdapter(Context context, List<String> items) {
            super(context, android.R.layout.simple_list_item_1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                // You can update the EditText field with the selected date
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                String zero = ((month +1)>10)?"":"0";
                selectedDate= year + "-"+ (month+1)+"-"+dayOfMonth;
                dateField.setText(selectedDate);
                date=selectedDate;
            }
        }, year, month, day);

        datePickerDialog.show();
    }

}



