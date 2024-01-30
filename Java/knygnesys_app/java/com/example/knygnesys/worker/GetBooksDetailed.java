package com.example.knygnesys.worker;

import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GetBooksDetailed implements Runnable {

    private int branchID;
    private String search;
    public CountDownLatch cdl;
    public ArrayList<Book> result;

    public GetBooksDetailed(CountDownLatch latch, int id, String search)
    {
        cdl = latch;
        branchID = id;
        this.search = search;
    }

    @Override
    public void run() {
        ArrayList<Book> rows = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM books WHERE fk_Branch = " + branchID + " AND title LIKE '%" + search + "%' ORDER BY title ASC";

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of rows
            while (resultSet2.next()) {
                Book book = new Book(
                        resultSet2.getInt("id"),
                        resultSet2.getString("title"),
                        resultSet2.getString("author"),
                        resultSet2.getInt("is_available"),
                        resultSet2.getInt("fk_Branch"),
                        resultSet2.getInt("fk_Order")
                );
                rows.add(book);
                System.out.println(book.getTitle());
            }
            conn.close();
            result = rows;
            cdl.countDown();
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}
