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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private Context context;
    private List<RVItem> RVItems;
    private ClickInterface clickInterface;

    public RVAdapter(Context context, List<RVItem> RVItems, ClickInterface clickInterface){
        this.context = context;
        this.RVItems = RVItems;
        this.clickInterface = clickInterface;
    }
    public RVAdapter(Context context, List<RVItem> RVItems){
        this.context = context;
        this.RVItems = RVItems;
        this.clickInterface = null;
    }


    @NonNull
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv, parent, false);
        return new RVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyViewHolder holder, int position){
        holder.links.setText(RVItems.get(position).getName());
        holder.rechts.setText(RVItems.get(position).getWert());
        if(clickInterface!=null){
            holder.links.setOnClickListener(x-> clickInterface.onClick());
            holder.rechts.setOnClickListener(x-> clickInterface.onClick());
        }
    }

    @Override
    public int getItemCount(){
        return RVItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView links;
        TextView rechts;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            links = itemView.findViewById(R.id.textLinks);
            rechts = itemView.findViewById(R.id.textRechts);
        }
    }

    public void setRecyclerViewItems(List<RVItem> RVItems){
        this.RVItems = RVItems;
        notifyDataSetChanged();
    }

}
