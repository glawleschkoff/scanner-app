package de.glawleschkoff.scannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewBottomAdapter extends RecyclerView.Adapter<RecyclerViewBottomAdapter.MyViewHolder>{
    private Context context;
    private List<RecyclerViewBottomItem> recyclerViewBottomItems;

    public RecyclerViewBottomAdapter(Context context, List<RecyclerViewBottomItem> recyclerViewBottomItems){
        this.context = context;
        this.recyclerViewBottomItems = recyclerViewBottomItems;
    }

    @NonNull
    @Override
    public RecyclerViewBottomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv_bottom, parent, false);
        return new RecyclerViewBottomAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBottomAdapter.MyViewHolder holder, int position){
        holder.textView.setText(recyclerViewBottomItems.get(position).getText());
    }

    @Override
    public int getItemCount(){
        return recyclerViewBottomItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }

    public void setRecyclerViewBottomItems(List<RecyclerViewBottomItem> recyclerViewBottomItems){
        this.recyclerViewBottomItems = recyclerViewBottomItems;
        notifyDataSetChanged();
    }
}
