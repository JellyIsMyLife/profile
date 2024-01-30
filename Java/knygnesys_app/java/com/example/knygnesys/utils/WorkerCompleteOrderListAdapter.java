package com.example.knygnesys.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.knygnesys.R;
import com.example.knygnesys.worker.RemoveOrder;
import com.example.knygnesys.worker.UpdateState;
import com.example.knygnesys.worker.UseOrder;
import com.example.knygnesys.worker.ViewOrderActivity;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkerCompleteOrderListAdapter extends ArrayAdapter<Order> {
    private static final String TAG = "WorkerCompleteOrderListAdapter";
    private Context mContext;
    int mResourse;

    /**
     * Default constructor for the WorkerOrderListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public WorkerCompleteOrderListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourse = resource;
    }

    static class ViewHolder{
        TextView id;
        TextView user;
        TextView date;
        TextView time;
        TextView state;
        Button info;
        Button use;
        Button deny;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get Order info
        int branch = getItem(position).getFk_Branch();
        int id = getItem(position).getId();
        int user = getItem(position).getFk_User();
        int state = getItem(position).getFk_State();
        String date = getItem(position).getCreated();
        int courier = getItem(position).getCourier_id();

        final View result;
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResourse, parent, false);

            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.list_order_text_id);
            holder.user = (TextView) convertView.findViewById(R.id.list_order_text_name);
            holder.date = (TextView) convertView.findViewById(R.id.list_order_text_date);
            holder.time = (TextView) convertView.findViewById(R.id.list_order_text_time);
            holder.state = (TextView) convertView.findViewById(R.id.list_order_text_state);
            holder.info = (Button) convertView.findViewById(R.id.list_order_info);
            holder.use = (Button) convertView.findViewById(R.id.list_order_use);
            holder.deny = (Button) convertView.findViewById(R.id.list_order_deny);
            result = convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.id.setText("Užsakymo numeris: " + id);
        holder.user.setText("Užsakovas: " + user);
        holder.date.setText("Sukurta: " + date);
        String temp = "";
        if (state == 5){
            temp = "Statusas: Atliktas";
        }
        else if (state == 6){
            temp = "Statusas: Panaudotas kurjerio";
        }
        else {
            temp = "Statusas: Atšauktas";
        }
        holder.state.setText(temp);

        holder.info.setTag(id);
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderId = (int) v.getTag();
                redirectActivity(mContext, ViewOrderActivity.class, orderId, branch);
            }
        });

        if (state == 5) {
            holder.use.setTag(id);
            holder.use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int orderId = (int) v.getTag();
                    useOrder(orderId, courier);
                    holder.use.setEnabled(false);
                    holder.use.setBackgroundResource(R.drawable.rounded_input);
                    holder.state.setText("Statusas: Panaudotas kurjerio");
                }
            });
        }
        else {
            holder.use.setEnabled(false);
            holder.use.setBackgroundResource(R.drawable.rounded_input);
        }

        holder.deny.setTag(id);
        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderId = (int) v.getTag();
                RemoveOrder co = new RemoveOrder(orderId);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(co);
                result.setVisibility(View.GONE); // hide the order view after updating the order
            }
        });

        return convertView;
    }

    private void useOrder(int orderID, int courier){
        Executor executor = Executors.newSingleThreadExecutor();
        UpdateState us = new UpdateState(orderID, 6);
        executor.execute(us);
        UseOrder uo = new UseOrder(orderID, courier);
        executor.execute(uo);
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity, int ID, int br) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("OrderID", ID);
        intent.putExtra("Branch", br);
        activity.startActivity(intent);
    }
}