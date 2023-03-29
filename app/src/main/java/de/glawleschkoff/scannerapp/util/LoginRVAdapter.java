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

public class LoginRVAdapter extends RecyclerView.Adapter<LoginRVAdapter.MyViewHolder> {

    private Context context;
    private List<LoginRVItem> loginRVItems;

    public LoginRVAdapter(Context context, List<LoginRVItem> loginRVItems){
        this.context = context;
        this.loginRVItems = loginRVItems;
    }

    @NonNull
    @Override
    public LoginRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_loginrv, parent, false);
        return new LoginRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginRVAdapter.MyViewHolder holder, int position){
        holder.textView.setText(loginRVItems.get(position).getText());
    }

    @Override
    public int getItemCount(){
        return loginRVItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textView8);
        }
    }

    public void setRecyclerViewItems(List<LoginRVItem> loginRVItems){
        this.loginRVItems = loginRVItems;
        notifyDataSetChanged();
    }

}
