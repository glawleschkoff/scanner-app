package de.glawleschkoff.scannerapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.glawleschkoff.scannerapp.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<RecyclerViewItem> recyclerViewItems;

    public RecyclerViewAdapter(Context context, List<RecyclerViewItem> recyclerViewItems){
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position){
        holder.textView1.setText(recyclerViewItems.get(position).getLeftText());
        holder.textView2.setText(recyclerViewItems.get(position).getRightText());
    }

    @Override
    public int getItemCount(){
        return recyclerViewItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView5);
            textView2 = itemView.findViewById(R.id.textView6);
        }
    }

    public void setRecyclerViewItems(List<RecyclerViewItem> recyclerViewItems){
        this.recyclerViewItems = recyclerViewItems;
        notifyDataSetChanged();
    }

}
