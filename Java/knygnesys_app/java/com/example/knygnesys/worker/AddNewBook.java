package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class AddNewBook implements Runnable{
    CountDownLatch cdl;
    private final String title;
    private final String author;
    private final int branch;
    int result;

    public AddNewBook(CountDownLatch latch, String u, String n, int b) {
        cdl = latch;
        title = u;
        author = n;
        branch = b;
    }

    @Override
    public void run() {
        String query = "Insert into books(title,author,fk_Branch,is_available) " + "VALUES(?, ?, ?, 1);";
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            // add variables for inputing
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, branch);
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                result = 0;
            }
        }
        catch (Exception ex) {
            result = 1;
            System.out.println("Error: " + ex.getMessage());
        }
        cdl.countDown();
    }
}