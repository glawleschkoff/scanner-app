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
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;

public class CardRVAdapter extends RecyclerView.Adapter<CardRVAdapter.MyViewHolder> {

    private List<CardRVItem> cardRVItemList;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView fixedRecyclerView;
        public RecyclerView hiddenRecyclerView;
        public CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            fixedRecyclerView = itemView.findViewById(R.id.rvfixed);
            hiddenRecyclerView = itemView.findViewById(R.id.rvhidden);
            cardView = itemView.findViewById(R.id.card);
        }
    }

    public CardRVAdapter(List<CardRVItem> cardRVItemList, Context context){
        this.cardRVItemList = cardRVItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardrv,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return cardRVItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClickInterface clickInterface = s -> {
            if(holder.hiddenRecyclerView.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                holder.hiddenRecyclerView.setVisibility(View.GONE);
            } else {
                TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                holder.hiddenRecyclerView.setVisibility(View.VISIBLE);
            }
        };
        CardRVItem currentItem = cardRVItemList.get(position);
        RecyclerView.LayoutManager layoutManagerFixed = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.fixedRecyclerView.setLayoutManager(layoutManagerFixed);
        holder.fixedRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerHidden = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.hiddenRecyclerView.setLayoutManager(layoutManagerHidden);
        holder.hiddenRecyclerView.setHasFixedSize(true);

        RVAdapter rvAdapterFixed = new RVAdapter(holder.fixedRecyclerView.getContext(),
                cardRVItemList.get(position).getFixedList().stream()
                        .map(x -> new RVItem(x.getName(), x.getWert()))
                        .collect(Collectors.toList()), clickInterface);
        RVAdapter rvAdapterHidden = new RVAdapter(holder.hiddenRecyclerView.getContext(),
                cardRVItemList.get(position).getHiddenList().stream()
                        .map(x -> new RVItem(x.getName(),x.getWert()))
                        .collect(Collectors.toList()), clickInterface);

        holder.fixedRecyclerView.setAdapter(rvAdapterFixed);
        holder.hiddenRecyclerView.setAdapter(rvAdapterHidden);
    }

    public void setCardRVItemList(List<CardRVItem> cardRVItemList) {
        this.cardRVItemList = cardRVItemList;
        notifyDataSetChanged();
    }
}
