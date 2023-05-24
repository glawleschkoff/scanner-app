package de.glawleschkoff.scannerapp.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.glawleschkoff.scannerapp.R;

public class BigRVAdapter extends RecyclerView.Adapter<BigRVAdapter.MyViewHolder> {

    private Context context;
    private List<RVItem> RVItems;
    private ClickInterface clickInterface;

    public BigRVAdapter(Context context, List<RVItem> RVItems, ClickInterface clickInterface){
        this.context = context;
        this.RVItems = RVItems;
        this.clickInterface = clickInterface;
    }
    public BigRVAdapter(Context context, List<RVItem> RVItems){
        this.context = context;
        this.RVItems = RVItems;
        this.clickInterface = null;
    }


    @NonNull
    @Override
    public BigRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_bigrv, parent, false);
        return new BigRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BigRVAdapter.MyViewHolder holder, int position){
        SpannableString wert;
        if(RVItems.get(position).getWert()!=null){
            wert = new SpannableString(RVItems.get(position).getWert());
        } else {
            wert = new SpannableString("");
        }

        if(RVItems.get(position).getName().equals("Länge") || RVItems.get(position).getName().equals("Breite")){
            wert.setSpan(new UnderlineSpan(), 0, wert.length(), 0);
            //holder.rechts.setTextColor(Color.parseColor("#000000"));
        }
        holder.links.setText(RVItems.get(position).getName());
        holder.rechts.setText(wert);
        if(clickInterface!=null){
            holder.links.setOnClickListener(x-> clickInterface.onClick(RVItems.get(position).getName()));
            holder.rechts.setOnClickListener(x-> clickInterface.onClick(RVItems.get(position).getName()));
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
