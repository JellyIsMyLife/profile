package com.example.knygnesys.utils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.knygnesys.R;
import com.example.knygnesys.courier.Orders;
import com.example.knygnesys.courier.States;
import com.example.knygnesys.courier.States2;
import com.example.knygnesys.courier.UpdateCourierId;
import com.example.knygnesys.courier.ViewOrdersActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourierAdapter extends ArrayAdapter<Order> {
    private Context mContext;
    private int mResource;
    private boolean showAcceptButton; // Flag to determine button visibility
    private int orderState; // Order state variable

    public CourierAdapter(Context context, int resource, ArrayList<Order> objects, int orderState) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.showAcceptButton = true; // Initially set to true to show the button
        this.orderState = orderState; // Set the order state
    }

    static class ViewHolder {
        TextView id;
        TextView user;
        TextView address;
        TextView number;
        Button add;
        Button info;
        Button deny;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int branch = getItem(position).getFk_Branch();
        int id = getItem(position).getId();
        int user = getItem(position).getFk_User();
        String date = getItem(position).getGet_date();
        String time = getItem(position).getGet_time();
        int booksint = getItem(position).getBook_amount();
        String address = getItem(position).getAddress();
        View result;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.id = convertView.findViewById(R.id.list_order_text_id);
            holder.user = convertView.findViewById(R.id.list_order_text_name);
            holder.address = convertView.findViewById(R.id.list_order_text_date);
            holder.number = convertView.findViewById(R.id.list_order_text_time);
            holder.add = convertView.findViewById(R.id.list_order_accept);
            holder.info = convertView.findViewById(R.id.list_order_info);
            holder.deny = convertView.findViewById(R.id.list_order_deny);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.id.setText("Užsakymo numeris: " + id);
        holder.user.setText("Užsakovas: " + user);
        holder.address.setText("Adresas: " + address);
        holder.number.setText("Knygų skaičius: " + booksint);
// Inside the getView method


        if (showAcceptButton) {
            holder.add.setVisibility(View.VISIBLE);
            if (orderState == 2) {
                holder.add.setText("Baigti");
            } else {
                holder.add.setText("Vykdyti"); // Default button text
            }
        } else {
            holder.add.setVisibility(View.GONE);
        }

        holder.add.setTag(id);
        // Inside the onClick method of "Baigti" button in CourierAdapter class
        // Inside the onClick method of "Vykdyti" button in CourierAdapter class
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderId = (int) v.getTag();

                if (orderState == 2) {
                    States2 us2 = new States2(orderId); // Use States2 class instead of States
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(us2);
                } else {
                    States us = new States(orderId);
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(us);
                }

                int loggedInUserId = getLoggedInUserId(); // Retrieve the logged-in user ID
                UpdateCourierId updateCourierId = new UpdateCourierId(orderId, loggedInUserId);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(updateCourierId);

                // Hide the entire order item
                ViewGroup parentView = (ViewGroup) v.getParent().getParent();
                parentView.setVisibility(View.GONE);
            }
        });



        holder.info.setTag(id);
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderId = (int) v.getTag();
                Intent intent = new Intent(mContext, ViewOrdersActivity.class);
                intent.putExtra("OrderID", orderId);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    public void updateButtonVisibility(boolean showAcceptButton) {
        this.showAcceptButton = showAcceptButton;
        notifyDataSetChanged();
    }

    public void updateOrderState(int newState) {
        orderState = newState;
        notifyDataSetChanged();
    }
    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0); // Retrieve the logged-in user ID
    }

    private void updateCourierId(int orderId, int loggedInUserId) {
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE orders SET courier_id = ? WHERE id = ?");
            statement.setInt(1, loggedInUserId);
            statement.setInt(2, orderId);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



