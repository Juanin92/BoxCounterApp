package com.example.boxcounter.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxcounter.R;
import com.example.boxcounter.model.entity.Shift;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShiftHistoryAdapter extends RecyclerView.Adapter<ShiftHistoryAdapter.TurnViewHolder> {

    private List<Shift> shiftList = new ArrayList<>();

    public ShiftHistoryAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTurns(List<Shift> shifts){
        this.shiftList = shifts;
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
        Shift shift = shiftList.get(position);

        SimpleDateFormat format =
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.tvStart.setText(
                "Inicio: " + format.format(new Date(shift.getStartTime())));

        if (shift.getEndTime() != null) {
            holder.tvEnd.setText(
                "Fin: " + format.format(new Date(shift.getEndTime())));
        } else {
            holder.tvEnd.setText("En curso");
        }

//        holder.tvEnd.setText(
//                "Fin: " + format.format(new Date(turn.getEndTime())));

        holder.tvQuantity.setText(
                "Cantidad: " + shift.getQuantity());

        holder.tvActive.setText(
                "Activo: " + shift.getActive());
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
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
