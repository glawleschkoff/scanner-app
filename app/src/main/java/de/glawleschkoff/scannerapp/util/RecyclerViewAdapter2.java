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

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder> {

    private Context context;
    private List<RecyclerViewItem2> recyclerViewItem2s;

    public RecyclerViewAdapter2(Context context, List<RecyclerViewItem2> recyclerViewItem2s){
        this.context = context;
        this.recyclerViewItem2s = recyclerViewItem2s;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv2, parent, false);
        return new RecyclerViewAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.MyViewHolder holder, int position){
        holder.textView.setText(recyclerViewItem2s.get(position).getText());
    }

    @Override
    public int getItemCount(){
        return recyclerViewItem2s.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textView8);
        }
    }

    public void setRecyclerViewItems(List<RecyclerViewItem2> recyclerViewItem2s){
        this.recyclerViewItem2s = recyclerViewItem2s;
        notifyDataSetChanged();
    }

}
