package de.glawleschkoff.scannerapp.util;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.glawleschkoff.scannerapp.R;

public class RTEBCardRVAdapter extends RecyclerView.Adapter<RTEBCardRVAdapter.MyViewHolder> {

    private List<RVItem> rvItemList;
    private Context context;
    private ClickInterface clickInterfaceExtern;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView innerRecyclerView;

        public MyViewHolder(View itemView){
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.rvinside);
        }
    }

    public RTEBCardRVAdapter(List<RVItem> rvItemList, Context context, ClickInterface clickInterfaceExtern){
        this.rvItemList = rvItemList;
        this.context = context;
        this.clickInterfaceExtern = clickInterfaceExtern;
    }

    @NonNull
    @Override
    public RTEBCardRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rtebcardrv,parent,false);
        return new RTEBCardRVAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClickInterface clickInterface = s -> {
            clickInterfaceExtern.onClick(s);
        };
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        holder.innerRecyclerView.setLayoutManager(layoutManager);
        holder.innerRecyclerView.setHasFixedSize(true);

        BigRVAdapter rvAdapter = new BigRVAdapter(holder.innerRecyclerView.getContext(),
                rvItemList,clickInterface);
        holder.innerRecyclerView.setAdapter(rvAdapter);
    }

    public void setRvItemList(List<RVItem> rvItemList) {
        this.rvItemList = rvItemList;
        notifyDataSetChanged();
    }
}
