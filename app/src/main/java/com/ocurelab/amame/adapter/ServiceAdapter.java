package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.Service;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<Service> services=new ArrayList<>();
    private Context context;

    public ServiceAdapter(List<Service> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.services,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service=services.get(position);
        holder.description.setText(service.getDescription());
        holder.phone.setText(service.getPhone());
        holder.name.setText(service.getName());
        holder.city.setText(service.getCity());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone,city,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            city=itemView.findViewById(R.id.city);
            description=itemView.findViewById(R.id.description);
        }
    }
}
