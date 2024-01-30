package com.example.knygnesys.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.knygnesys.R;
import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.Branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class ViewOrdersActivity extends AppCompatActivity {

    private int OrderID;
    private int Branch;
    private ListView listView;
    private String branchName;
    private String branchAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle extras = getIntent().getExtras();
        listView = findViewById(R.id.list_books);
        OrderID = extras.getInt("OrderID");
        Branch = extras.getInt("Branch");
        listBooks();
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(view -> close());

    }

    public void close(){
        this.finish();
    }

    public void listBooks() {
        // Set up adapter to display rows in a list view
        CountDownLatch latch = new CountDownLatch(2); // Increase count to match the number of threads
        Books gb = new Books(latch, OrderID);
        Branches branches = new Branches(latch, OrderID);
        Executor executor = Executors.newFixedThreadPool(2); // Use a fixed thread pool
        executor.execute(gb);
        executor.execute(branches);
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("idk");
        }
        List<Book> books = gb.result;
        List<Branch> branchList = branches.result;

        // Create a map of branch IDs to branch objects for easier lookup
        Map<Integer, Branch> branchMap = new HashMap<>();
        for (Branch branch : branchList) {
            branchMap.put(branch.getId(), branch);
        }

        // Convert the list of books to a list of strings for display
        List<String> bookInfoList = new ArrayList<>();
        for (Book book : books) {
            String bookInfo = book.getTitle() + "\n" + book.getAuthor() + ", id: " + book.getId();

            // Retrieve the branch using the branch ID
            Branch branch = branchMap.get(book.getFk_Branch());

            // Append branch name and address to the book information if branch is found
            if (branch != null) {
                bookInfo += "\nPadalinys: " + branch.getName() + ", " + branch.getAddress();
            }

            bookInfoList.add(bookInfo);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewOrdersActivity.this, R.layout.list_string_row, R.id.list_string_text, bookInfoList);
        listView.setAdapter(adapter);
    }


}


