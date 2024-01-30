package com.example.knygnesys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.knygnesys.R;
import com.example.knygnesys.utils.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.knygnesys.courier.*;
import com.example.knygnesys.utils.*;



public class ViewBooksActivity extends AppCompatActivity {

    private String order;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle extras = getIntent().getExtras();
        listView = findViewById(R.id.list_books);
        order = extras.getString("Order");
        listBooks();
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(view -> close());
    }

    public void close(){
        this.finish();
    }

    public void listBooks() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(1);

        String[] parts = order.split("\n");
        String idString = parts[0];
        System.out.println(idString);
        String[] parts2 = idString.split(" ");
        String id = parts2[1];
        Books gb = new Books(latch, Integer.parseInt(id));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(gb);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        List<Book> books = gb.result;

        // Convert the list of books to a list of strings for display
        List<String> bookInfoList = new ArrayList<>();
        for (Book book : books) {
            String bookInfo = "Pavadinimas: " + book.getTitle() + "\n\nAutorius: " + book.getAuthor();
            bookInfoList.add(bookInfo);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewBooksActivity.this, R.layout.list_string_row, R.id.list_string_text, bookInfoList);
        listView.setAdapter(adapter);
    }


}


