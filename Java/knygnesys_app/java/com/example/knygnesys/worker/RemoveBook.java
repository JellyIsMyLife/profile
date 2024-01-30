package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveBook implements Runnable {

    private int bookID;

    public RemoveBook(int id)
    {
        bookID = id;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to delete
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM books WHERE id = " + bookID;
            statement.execute(sql);
            System.out.println("executed " + sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}