package com.example.boxcounter.ui.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.boxcounter.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditNameDialog extends DialogFragment {

    public interface OnNameSaveListener{
        void onNameSave(String newName);
    }

    private static final String ARG_CURRENT_NAME = "current_name";
    private static final String ARG_CANCELABLE = "is_cancelable";

    private OnNameSaveListener listener;

    public static EditNameDialog newInstance(String currentName, boolean isCancelable,
                                             OnNameSaveListener listener){
        EditNameDialog dialog = new EditNameDialog();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_NAME, currentName);
        args.putBoolean(ARG_CANCELABLE, isCancelable);
        dialog.setArguments(args);
        dialog.listener = listener;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.dialog_edit_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextView tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        TextInputEditText etUserName = view.findViewById(R.id.etUserName);
        Button btnSaveName = view.findViewById(R.id.btnSaveName);
        Button btnCancelName = view.findViewById(R.id.btnCancelName);

        String currentName = getArguments() != null ? getArguments()
                .getString(ARG_CURRENT_NAME, "") : "";
        boolean isCancelable = getArguments() == null || getArguments()
                .getBoolean(ARG_CANCELABLE, true);

        setCancelable(isCancelable);

        if (!currentName.isEmpty()){
            etUserName.setText(currentName);
            tvDialogTitle.setText("Editar Nombre");
            btnCancelName.setVisibility(View.VISIBLE);
        }else {
            tvDialogTitle.setText("!Bienvenido!");
            btnCancelName.setVisibility(View.GONE);
        }

        btnSaveName.setOnClickListener(v -> {
            String newName = etUserName.getText() != null ? etUserName.getText().toString().trim() : "";
            if(!newName.isEmpty()){
                if (listener != null){
                    listener.onNameSave(newName);
                }
                dismiss();
            }else {
                etUserName.setError("Ingresa un nombre válido");
            }
        });

        btnCancelName.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart(){
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
