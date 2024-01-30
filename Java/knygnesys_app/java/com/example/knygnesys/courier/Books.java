package com.example.knygnesys.courier;

import android.widget.Toast;

import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Books implements Runnable {
    private int orderID;
    public CountDownLatch cdl;
    public List<Book> result;

    public Books(CountDownLatch latch, int id) {
        cdl = latch;
        orderID = id;
    }

    @Override
    public void run() {
        List<Book> books = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM books WHERE fk_Order = " + orderID;

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of books
            while (resultSet2.next()) {
                Book book = new Book(
                        resultSet2.getInt("id"),
                        resultSet2.getString("title"),
                        resultSet2.getString("author"),
                        resultSet2.getInt("is_available"),
                        resultSet2.getInt("fk_Branch"),
                        resultSet2.getInt("fk_Order")
                );
                books.add(book);
                System.out.println(book.getTitle());
            }
            conn.close();
            result = books;
            cdl.countDown();
        } catch (SQLException e) {
            // Handle the exception appropriately
        }
    }
}
