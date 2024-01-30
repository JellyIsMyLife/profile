package com.example.knygnesys.login;

import android.widget.TextView;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class CheckLogin implements Runnable{
    int result;
    CountDownLatch cdl;
    private final TextView email;
    private final TextView password;
    int role;
    int id;
    String name;
    String surname;
    String username;
    String address;
    String readersID;
    String password1;
    String email1;
    int branch;

    public CheckLogin(CountDownLatch latch, TextView e, TextView p)
    {
        cdl = latch;
        email = e;
        password = p;
    }

    @Override
    public void run() {
        Connection connection;
        try {
            connection = DBConnect.getConnection();
            if (connection != null){
                String query = "Select password, fk_Role, id, username, name, surname, address, email, readers_id, branch_id From users where email = '" + email.getText().toString() + "';";
                Statement st= connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next())
                {
                    String pass = rs.getString(1);
                    // TODO password encryption
                    if (pass.equals(password.getText().toString())){
                        result = 0;
                        role = rs.getInt(2);
                        id = rs.getInt(3);
                        username = rs.getString(4);
                        name = rs.getString(5);
                        surname = rs.getString(6);
                        address = rs.getString(7);
                        email1 = rs.getString(8);
                        readersID = rs.getString(9);
                        password1 = rs.getString(1);
                        branch = rs.getInt(10);
                    }
                    else{
                        // Wrong password
                        result = 1;
                    }
                }
                else {
                    // User with such email does not exist
                    result = 2;
                }
            }
            else{
                // No connection
                result = 3;
            }
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        cdl.countDown();
    }
}
