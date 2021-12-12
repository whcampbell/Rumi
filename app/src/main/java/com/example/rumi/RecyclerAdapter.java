package com.example.rumi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private ArrayList<DashConvo> convosList;
    private RecyclerClickListenter listener;

    public RecyclerAdapter(ArrayList<DashConvo> list, RecyclerClickListenter listener) {
        this.listener = listener;
        this.convosList = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView HeadText;
        private TextView NextText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            HeadText = itemView.findViewById(R.id.textConvoHead);
            NextText = itemView.findViewById(R.id.textConvoComment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dash_convo_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String head = convosList.get(position).getConvoHead();
        holder.HeadText.setText(head);
        String next = convosList.get(position).getConvoNext();
        holder.NextText.setText(next);

    }

    @Override
    public int getItemCount() {
        return convosList.size();
    }

    public interface RecyclerClickListenter {
        void onClick(View view, int position);
    }

}
