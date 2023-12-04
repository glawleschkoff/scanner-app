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

public class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.MyViewHolder> {

    private Context context;
    private List<MenuRVItem> MenuRVItems;

    public MenuRVAdapter(Context context, List<MenuRVItem> MenuRVItems){
        this.context = context;
        this.MenuRVItems = MenuRVItems;
    }

    @NonNull
    @Override
    public MenuRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_rv_item, parent, false);
        return new MenuRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRVAdapter.MyViewHolder holder, int position){
        holder.text.setText(MenuRVItems.get(position).getName());
    }

    @Override
    public int getItemCount(){
        return MenuRVItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public void setRecyclerViewItems(List<MenuRVItem> MenuRVItems){
        this.MenuRVItems = MenuRVItems;
        notifyDataSetChanged();
    }
}
