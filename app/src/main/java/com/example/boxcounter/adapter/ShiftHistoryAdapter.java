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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShiftHistoryAdapter extends RecyclerView.Adapter<ShiftHistoryAdapter.TurnViewHolder> {

    private List<Shift> shiftList = new ArrayList<>();
    private boolean isAscending = false;
    private String currentColumn = "DATE";

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
                .inflate(R.layout.item_shift, parent, false);

        return new TurnViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TurnViewHolder holder, int position) {
        Shift shift = shiftList.get(position);

        SimpleDateFormat format =
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.tvStart.setText(format.format(new Date(shift.getStartTime())));

        if (shift.getEndTime() != null) {
            holder.tvEnd.setText(format.format(new Date(shift.getEndTime())));
        } else {
            holder.tvEnd.setText("En Curso");
        }

        holder.tvQuantity.setText(String.valueOf(shift.getQuantity()));

        if (shift.isActive()){
            holder.tvActive.setText(
                    "Activo");
        } else {
            holder.tvActive.setText(
                    "Terminado");
        }
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

    public Shift getShiftAt(int position) {
        return shiftList.get(position);
    }

    public void sortBy(String column) {
        if (shiftList == null || shiftList.isEmpty()) return;

        if (currentColumn.equals(column)) {
            isAscending = !isAscending;
        } else {
            currentColumn = column;
            isAscending = false;
        }

        if ("QUANTITY".equals(column)) {
            shiftList.sort((s1, s2) -> isAscending ?
                    Integer.compare(s1.getQuantity(), s2.getQuantity()) :
                    Integer.compare(s2.getQuantity(), s1.getQuantity()));
        } else {
            shiftList.sort((s1, s2) -> isAscending ?
                    Long.compare(s1.getStartTime(), s2.getStartTime()) :
                    Long.compare(s2.getStartTime(), s1.getStartTime()));
        }

        notifyDataSetChanged();
    }

    public boolean isAscending() {
        return isAscending;
    }
}
