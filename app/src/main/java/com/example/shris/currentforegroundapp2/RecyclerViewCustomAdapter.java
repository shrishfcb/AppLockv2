package com.example.shris.currentforegroundapp2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by shris on 3/21/2018.
 */

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.CustomViewHolder> {
    String[] apps;
    Context context;
    PackageManager packageManager;
    public RecyclerViewCustomAdapter(Context context,String[] apps) {
        this.apps = apps;
        this.context = context;
        packageManager = context.getPackageManager();
    }

    public int getItemCount() {
        return apps.length;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Switch aSwitch;
        ImageView imageView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            aSwitch = itemView.findViewById(R.id.on);
            imageView = itemView.findViewById(R.id.icon);
        }
    }

    public CustomViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    public void onBindViewHolder (final CustomViewHolder holder, final int position) {
        try {
            final String name = packageManager.getApplicationLabel(packageManager.getApplicationInfo(apps[position], 0)).toString();
            final DatabaseAdapter adapter = new DatabaseAdapter(context);
            holder.textView.setText(name);
            holder.aSwitch.setChecked(false);
            holder.imageView.setImageDrawable(packageManager.getApplicationIcon(apps[position]));
            if (adapter.isPresent(apps[position])) {
                holder.aSwitch.setChecked(true);
            }
            holder.aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.aSwitch.isChecked()) {
                        adapter.insertApp(name, apps[position]);
                        Log.d("Inserted", apps[position]);
                        Log.d("Inserted", name);
                    }
                    else {
                        adapter.deleteApp(apps[position]);
                        Log.d("Deleted", apps[position]);
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
