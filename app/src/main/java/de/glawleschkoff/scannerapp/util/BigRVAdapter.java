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

//public class BigRVAdapter extends RecyclerView.Adapter<BigRVAdapter.MyViewHolder> {
public class BigRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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


    /*@NonNull
    @Override
    public BigRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_bigrv, parent, false);
        return new BigRVAdapter.MyViewHolder(view);
    }*/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                LayoutInflater inflater1 = LayoutInflater.from(context);
                View view1 = inflater1.inflate(R.layout.item_bigrv, parent, false);
                return new BigRVAdapter.MyViewHolder1(view1);
            case 1:
                LayoutInflater inflater2 = LayoutInflater.from(context);
                View view2 = inflater2.inflate(R.layout.item_bigrvqr, parent, false);
                return new BigRVAdapter.MyViewHolder2(view2);
            default:
                return null;
        }
    }

    /*@Override
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
    }*/

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                MyViewHolder1 myViewHolder1 = (MyViewHolder1) holder;
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
                myViewHolder1.links.setText(RVItems.get(position).getName());
                myViewHolder1.rechts.setText(wert);
                if(clickInterface!=null){
                    myViewHolder1.links.setOnClickListener(x-> clickInterface.onClick(RVItems.get(position).getName()));
                    myViewHolder1.rechts.setOnClickListener(x-> clickInterface.onClick(RVItems.get(position).getName()));
                }
                break;

            case 1:
                MyViewHolder2 myViewHolder2 = (MyViewHolder2) holder;
                myViewHolder2.links.setText(RVItems.get(position).getName());
                myViewHolder2.rechts.setText(RVItems.get(position).getWert());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int i = 0;
        if(RVItems.get(position).getName().equals("Lagerplatz")){
            i = 1;
        }
        return i;
    }

    @Override
    public int getItemCount(){
        return RVItems.size();
    }

    /*public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView links;
        TextView rechts;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            links = itemView.findViewById(R.id.textLinks);
            rechts = itemView.findViewById(R.id.textRechts);
        }
    }*/
    public static class MyViewHolder1 extends RecyclerView.ViewHolder{
        TextView links;
        TextView rechts;

        public MyViewHolder1(@NonNull View itemView){
            super(itemView);
            links = itemView.findViewById(R.id.textLinks);
            rechts = itemView.findViewById(R.id.textRechts);
        }
    }
    public static class MyViewHolder2 extends RecyclerView.ViewHolder{
        TextView links;
        TextView rechts;

        public MyViewHolder2(@NonNull View itemView){
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
