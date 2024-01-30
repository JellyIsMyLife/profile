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
import com.example.knygnesys.worker.EditBookActivity;
import com.example.knygnesys.worker.RemoveBook;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BookListAdapter extends ArrayAdapter<Book> {
    private static final String TAG = "BookListAdapter";
    private Context mContext;
    int mResourse;

    /**
     * Default constructor for the WorkerOrderListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public BookListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourse = resource;
    }

    static class ViewHolder{
        TextView title;
        TextView author;
        TextView amount;
        Button edit;
        Button delete;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get Book info
        int id = getItem(position).getId();
        int branch = getItem(position).getFk_Branch();
        String title = getItem(position).getTitle();
        String author = getItem(position).getAuthor();

        final View result;
        BookListAdapter.ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResourse, parent, false);

            holder = new BookListAdapter.ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.book_name);
            holder.author = (TextView) convertView.findViewById(R.id.book_author);
            holder.edit = (Button) convertView.findViewById(R.id.book_edit);
            holder.delete = (Button) convertView.findViewById(R.id.book_delete);
            result = convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (BookListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.title.setText("Pavadinimas: " + title);
        holder.author.setText("Autorius: " + author);

        holder.edit.setTag(id);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bookID = (int) v.getTag();
                redirectActivity(mContext, EditBookActivity.class, bookID, branch);
            }
        });

        holder.delete.setTag(id);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bookID = (int) v.getTag();
                RemoveBook rb = new RemoveBook(bookID);
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(rb);
                result.setVisibility(View.GONE); // hide the order view after updating the order
            }
        });

        return convertView;
    }

    private void redirectActivity(Context activity, final Class<? extends Activity> secondActivity, int ID, int branch) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("bookID", ID);
        intent.putExtra("branchID", branch);
        activity.startActivity(intent);
    }
}