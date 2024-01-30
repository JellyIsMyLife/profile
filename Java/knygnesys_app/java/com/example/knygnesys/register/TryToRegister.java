package com.example.knygnesys.register;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class TryToRegister implements Runnable{
    CountDownLatch cdl;
    private final String username;
    private final String name;
    private final String secondname;
    private final String password;
    private final String address;
    private final String email;
    private final int role;
    private final String readers_id;
    int result;

    public TryToRegister(CountDownLatch latch, String u, String n, String s, String p, String a, String e, int r, String rid) {
        cdl = latch;
        username = u;
        name = n;
        secondname = s;
        password = p;
        address = a;
        email = e;
        role = r;
        readers_id =rid;
    }

    @Override
    public void run() {
        long id; // user id if we'll need for session or smth
        String checkuser = "Select email from users where email = '" + email + "';";
        String query = "Insert into users(username,name,surname,password,address,email,fk_role,readers_id) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = DBConnect.getConnection();
             ResultSet checkResult = conn.createStatement().executeQuery(checkuser)){
            // if user doesn't exist (checks by email)
            if (!checkResult.next()){
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                // add variables for inputing
                pstmt.setString(1, username);
                pstmt.setString(2, name);
                pstmt.setString(3, secondname);
                // TODO password encryption
                pstmt.setString(4, password);
                pstmt.setString(5, address);
                pstmt.setString(6, email);
                pstmt.setString(7, Integer.toString(role)); // 1 - admin, 2 - client, 3 - courier, 4 - employee
                pstmt.setString(8, readers_id);
                int affectedRows = pstmt.executeUpdate();
                // check the affected rows
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        result = 0;
                        if (rs.next()) {
                            id = rs.getLong(1);
                            System.out.println(id);
                        }
                    } catch (Exception ex) {
                        result = 2;
                        System.out.println(ex.getMessage());
                    }
                }
            }
            // if user exists
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
