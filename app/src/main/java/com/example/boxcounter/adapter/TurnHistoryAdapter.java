package com.example.boxcounter.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxcounter.R;
import com.example.boxcounter.model.entity.Turn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TurnHistoryAdapter extends RecyclerView.Adapter<TurnHistoryAdapter.TurnViewHolder> {

    private List<Turn> turnList = new ArrayList<>();

    public TurnHistoryAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTurns(List<Turn> turns){
        this.turnList = turns;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TurnViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turn, parent, false);

        return new TurnViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TurnViewHolder holder, int position) {
        Turn turn = turnList.get(position);

        SimpleDateFormat format =
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.tvStart.setText(
                "Inicio: " + format.format(new Date(turn.getStartTime())));

        if (turn.getEndTime() != null) {
            holder.tvEnd.setText(
                "Fin: " + format.format(new Date(turn.getEndTime())));
        } else {
            holder.tvEnd.setText("En curso");
        }

//        holder.tvEnd.setText(
//                "Fin: " + format.format(new Date(turn.getEndTime())));

        holder.tvQuantity.setText(
                "Cantidad: " + turn.getQuantity());

        holder.tvActive.setText(
                "Activo: " + turn.getActive());
    }

    @Override
    public int getItemCount() {
        return turnList.size();
    }

    static class TurnViewHolder extends RecyclerView.ViewHolder {

        TextView tvStart;
        TextView tvEnd;
        TextView tvQuantity;
        TextView tvActive;

        public TurnViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvActive = itemView.findViewById(R.id.tvActive);
        }
    }
}
