package com.example.boxcounter.ui.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.boxcounter.R;

public class ActiveShiftDialog extends DialogFragment {

    public interface OnActiveShiftActionListener{
        void onContinue();
        void onNewShift();
    }

    private final OnActiveShiftActionListener listener;
    private final String startTime;
    private final int quantity;

    public ActiveShiftDialog(String startTime, int quantity, OnActiveShiftActionListener listener){
        this.startTime = startTime;
        this.quantity = quantity;
        this.listener = listener;
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_active_shift, container,
                false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        TextView tvShiftDate = view.findViewById(R.id.tvShiftDate);
        TextView tvShiftQuantity = view.findViewById(R.id.tvShiftQuantity);
        Button btnContinueShift = view.findViewById(R.id.btnContinueShift);
        Button btnNewShift = view.findViewById(R.id.btnNewShift);

        tvShiftDate.setText(startTime);
        tvShiftQuantity.setText(String.valueOf(quantity));

        btnContinueShift.setOnClickListener(v -> {
            dismiss();
            if (listener != null) listener.onContinue();
        });

        btnNewShift.setOnClickListener(v -> {
            dismiss();
            if (listener != null) listener.onNewShift();
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
