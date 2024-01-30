package com.example.knygnesys.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.knygnesys.R;
import com.example.knygnesys.admin.EditUserActivity;
import com.example.knygnesys.admin.RemoveUser;
import com.example.knygnesys.worker.RemoveOrder;
import com.example.knygnesys.worker.ViewOrderActivity;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserListAdapter extends ArrayAdapter<User> {
    private static final String TAG = "UserListAdapter";
    private Context mContext;
    int mResourse;

    /**
     * Default constructor for the WorkerOrderListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public UserListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourse = resource;
    }

    static class ViewHolder{
        TextView id;
        TextView name;
        TextView role;
        Button edit;
        Button remove;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get User info
        int id = getItem(position).getId();
        String name = getItem(position).getUsername();
        String email = getItem(position).getEmail();
        int role = getItem(position).getFkRole();

        final View result;
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResourse, parent, false);

            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.role = (TextView) convertView.findViewById(R.id.role);
            holder.edit = (Button) convertView.findViewById(R.id.edit);
            holder.remove = (Button) convertView.findViewById(R.id.delete);
            result = convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.id.setText("Naudotojas: {" + id + "} " + name);
        holder.name.setText("Paštas: " + email);
        String temp = "";
        if (role == 2){
            temp = "Klientas";
        }
        else if (role == 3){
            temp = "Kurjeris";
        }
        else {
            temp = "Darbuotojas";
        }
        holder.role.setText("Rolė: " + temp);

        holder.edit.setTag(id);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userID = (int) v.getTag();
                redirectActivity(mContext, EditUserActivity.class, userID);
            }
        });

        holder.remove.setTag(id);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userID = (int) v.getTag();
                RemoveUser ru = new RemoveUser(userID);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(ru);
                result.setVisibility(View.GONE); // hide the order view after updating the order
            }
        });

        return convertView;
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity, int ID) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userID", ID);
        activity.startActivity(intent);
    }
}
