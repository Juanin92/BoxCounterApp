package com.example.boxcounter.ui.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.boxcounter.R;

public class ManualAddDialog extends DialogFragment {

    public interface OnQuantityAddedListener{
        void onQuantityAdded(int value);
    }

    private OnQuantityAddedListener listener;

    public static ManualAddDialog newInstance(OnQuantityAddedListener listener){
        ManualAddDialog fragment = new ManualAddDialog();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_and_manualquantity, container,
                false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        EditText et = view.findViewById(R.id.etQuantity);
        Button btnConfirm = view.findViewById(R.id.btnConfirmManual);
        Button btnCancel = view.findViewById(R.id.btnCancelManual);

        btnConfirm.setOnClickListener(v -> {
            String text = et.getText().toString();
            if (text.isEmpty()){
                et.setError("Campo requerido");
            } else {
                if (listener != null){
                    listener.onQuantityAdded(Integer.parseInt(text));
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
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
