package com.example.knygnesys.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.knygnesys.R;
import com.example.knygnesys.utils.Order;
import com.example.knygnesys.utils.User;
import com.example.knygnesys.utils.UserListAdapter;
import com.example.knygnesys.utils.WorkerCompleteOrderListAdapter;
import com.example.knygnesys.worker.GetCompletedOrders;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminControlUsersActivity extends AppCompatActivity {

    private int userID;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_users);
        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
        listView = findViewById(R.id.list_users);
        listUsers();
        Button back = findViewById(R.id.back);
        back.setOnClickListener(view -> close());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listUsers();
    }

    public void close(){
        this.finish();
    }

    private void listUsers() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);
        GetUsers gu = new GetUsers(latch);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gu);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        ArrayList<User> userlist = gu.result;
        UserListAdapter adapter = new UserListAdapter(this, R.layout.list_users, userlist);
        listView.setAdapter(adapter);
    }
}