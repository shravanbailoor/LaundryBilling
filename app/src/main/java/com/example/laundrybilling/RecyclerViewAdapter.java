package com.example.laundrybilling;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> listAddedItems;

    public RecyclerViewAdapter(ArrayList<String> listAddedItems) {
        this.listAddedItems = listAddedItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvAddedItem.setText(listAddedItems.get(i));
    }

    @Override
    public int getItemCount() {
        return listAddedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddedItem;

        public ViewHolder(View view) {
            super(view);

            tvAddedItem = view.findViewById(R.id.textViewAddedItem);
        }
    }

}
