package de.glawleschkoff.scannerapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.glawleschkoff.scannerapp.R;

public class MaterialienAdapter extends RecyclerView.Adapter<MaterialienAdapter.MyViewHolder> {

    private Context context;
    private List<String> items;
    private ClickInterface clickInterface;

    public MaterialienAdapter(Context context, List<String> items, ClickInterface clickInterface){
        this.context = context;
        this.items = items;
        this.clickInterface = clickInterface;
    }
    public MaterialienAdapter(Context context, List<RVItem> RVItems){
        this.context = context;
        this.items = items;
        this.clickInterface = null;
    }


    @NonNull
    @Override
    public MaterialienAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_material, parent, false);
        return new MaterialienAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialienAdapter.MyViewHolder holder, int position){
        holder.textView.setText(items.get(position));
        if(clickInterface!=null){
            holder.textView.setOnClickListener(x-> clickInterface.onClick(items.get(position)));
        }
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    public void setRecyclerViewItems(List<String> items){
        this.items = items;
        notifyDataSetChanged();
    }

}
