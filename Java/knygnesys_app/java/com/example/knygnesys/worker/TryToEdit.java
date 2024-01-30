package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class TryToEdit implements Runnable{
    CountDownLatch cdl;
    private final String title;
    private final String author;
    private final int id;
    int result;

    public TryToEdit(CountDownLatch latch, String title, String author, int id) {
        cdl = latch;
        this.title = title;
        this.author = author;
        this.id = id;
    }

    @Override
    public void run() {
        long id; // user id if we'll need for session or smth
        String query = "UPDATE books set title = ?, author = ? where id = ?";
        try (Connection conn = DBConnect.getConnection();){
            // if title doesn't exist
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            // add variables for inputing
            pstmt.setString(1, this.title);
            pstmt.setString(2, this.author);
            pstmt.setInt(3, this.id);
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                result = 0;
            }
            else {
                result = 1;
            }
        }
        catch (Exception ex) {
            result = 2;
            System.out.println("Error: " + ex.getMessage());
        }
        cdl.countDown();
    }
}
