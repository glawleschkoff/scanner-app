package de.glawleschkoff.scannerapp.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.glawleschkoff.scannerapp.R;

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
            case 2:
                LayoutInflater inflater3 = LayoutInflater.from(context);
                View view3 = inflater3.inflate(R.layout.item_smallrv, parent, false);
                return new BigRVAdapter.MyViewHolder3(view3);
            default:
                return null;
        }
    }

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

                if(RVItems.get(position).getName().equals("Länge") || RVItems.get(position).getName().equals("Breite") || RVItems.get(position).getName().equals("Material\nKurzzeichen")){
                    wert.setSpan(new UnderlineSpan(), 0, wert.length(), 0);
                }
                String name = RVItems.get(position).getName();
                switch(name){
                    case "Material\nKurzzeichen1":
                        name = "Material\nKurzzeichen";
                        break;
                    case "Länge1":
                        name = "Länge";
                        break;
                    case "Breite1":
                        name = "Breite";
                        break;
                    case "Lagerplatz1":
                        name = "Lagerplatz";
                        break;
                }
                myViewHolder1.links.setText(name);
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
            case 2:
                MyViewHolder3 myViewHolder3 = (MyViewHolder3) holder;
                myViewHolder3.links.setText(RVItems.get(position).getName());
                myViewHolder3.rechts.setText(RVItems.get(position).getWert());
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int i = 0;
        if(RVItems.get(position).getName().equals("Lagerplatz")){
            i = 1;
        } else if(RVItems.get(position).getName().equals("WINKEL A") ||
                RVItems.get(position).getName().equals("WINKEL B") ||
                RVItems.get(position).getName().equals("WINKEL C") ||
                RVItems.get(position).getName().equals("WINKEL D") ||
                RVItems.get(position).getName().equals("SÄGEN A") ||
                RVItems.get(position).getName().equals("SÄGEN B") ||
                RVItems.get(position).getName().equals("SÄGEN C") ||
                RVItems.get(position).getName().equals("SÄGEN D") ||
                RVItems.get(position).getName().equals("SÄGEN Ecke")){
            i = 2;
        }
        return i;
    }

    @Override
    public int getItemCount(){
        return RVItems.size();
    }

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

    public static class MyViewHolder3 extends RecyclerView.ViewHolder{
        TextView links;
        TextView rechts;

        public MyViewHolder3(@NonNull View itemView){
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
