package com.example.knygnesys.admin;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class EditUser implements Runnable{
    CountDownLatch cdl;
    private final int role;
    private final int branch;
    private final int id;
    int result;

    public EditUser(CountDownLatch latch, int r, int b, int id) {
        cdl = latch;
        this.role = r;
        this.branch = b;
        this.id = id;
    }

    @Override
    public void run() {
        long id; // user id if we'll need for session or smth
        String query = "UPDATE users set fk_Role = ?, branch_id = ? where id = ?";
        try (Connection conn = DBConnect.getConnection();){
            // if title doesn't exist
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            // add variables for inputing
            pstmt.setInt(1, this.role);
            pstmt.setInt(2, this.branch);
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