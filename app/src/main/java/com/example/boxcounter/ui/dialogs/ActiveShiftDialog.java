package com.example.boxcounter.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ActiveShiftDialog extends DialogFragment {

    public interface OnActiveShiftActionListener{
        void onContinue();
        void onFinish();
    }

    private final OnActiveShiftActionListener listener;
    private final String message;

    public ActiveShiftDialog(String message, OnActiveShiftActionListener listener){
        this.message = message;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Turno Activo Detectado")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continuar", (dialog, id) -> {
                    if (listener != null){
                        listener.onContinue();
                    }
                })
                .setNegativeButton("Nuevo Turno", (dialog, id) -> {
                    if (listener != null){
                        listener.onFinish();
                    }
                });
        return builder.create();
    }
}
