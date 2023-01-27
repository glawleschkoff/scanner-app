package de.glawleschkoff.scannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewTopAdapter extends RecyclerView.Adapter<RecyclerViewTopAdapter.MyViewHolder>{
    private Context context;
    private List<RecyclerViewTopItem> recyclerViewTopItems;

    public RecyclerViewTopAdapter(Context context, List<RecyclerViewTopItem> recyclerViewTopItems){
        this.context = context;
        this.recyclerViewTopItems = recyclerViewTopItems;
    }

    @NonNull
    @Override
    public RecyclerViewTopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv_top, parent, false);
        return new RecyclerViewTopAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTopAdapter.MyViewHolder holder, int position){
        holder.textView.setText(recyclerViewTopItems.get(position).getText());
    }

    @Override
    public int getItemCount(){
        return recyclerViewTopItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }

    public void setRecyclerViewTopItems(List<RecyclerViewTopItem> recyclerViewTopItems){
        this.recyclerViewTopItems = recyclerViewTopItems;
        notifyDataSetChanged();
    }
}
