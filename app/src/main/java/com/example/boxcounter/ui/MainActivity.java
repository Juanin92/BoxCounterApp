package com.example.boxcounter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.boxcounter.R;
import com.example.boxcounter.ui.auth.BiometricManagerHelper;
import com.example.boxcounter.viewModel.TurnViewModel;

public class MainActivity extends AppCompatActivity {

    private TurnViewModel viewModel;
    private TextView tvQuantity;
    private Button btnPlus, btnMinus, btnFinish;
    private ImageButton btnHistory;
    private BiometricManagerHelper biometricManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        biometricManagerHelper = new BiometricManagerHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvQuantity = findViewById(R.id.tvQuantity);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus =  findViewById(R.id.btnMinus);
        btnFinish =  findViewById(R.id.btnFinish);
        btnHistory =  findViewById(R.id.btnHistory);

        viewModel = new ViewModelProvider(this).get(TurnViewModel.class);
        viewModel.getActiveTurn().observe(this, turn -> {
            if (turn != null){
                tvQuantity.setText(String.valueOf(turn.getQuantity()));
            }
        });

        btnPlus.setOnClickListener(v -> {viewModel.increment();});
        btnPlus.setOnLongClickListener(v -> {
            showAddDialog();
            return true;
        });

        btnMinus.setOnClickListener(v -> {viewModel.decrement();});
        btnFinish.setOnClickListener(v -> {
            biometricManagerHelper.authenticate(() -> viewModel.finish());
        });

        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });

        tvQuantity.setOnLongClickListener(v -> {
            showManualAddDialog();
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showManualAddDialog() {
        View view =  getLayoutInflater().inflate(R.layout.dialog_and_manualquantity, null);
        EditText et = view.findViewById(R.id.etQuantity);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Agregar Cantidad Manual")
                .setView(view)
                .setPositiveButton("Confirmar", null)
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();

        Button btnConfirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnConfirm.setOnClickListener(v -> {
            String text = et.getText().toString();
            if (text.isEmpty()){
                et.setError("Ingrese un monto: ");
                return;
            }

            int value =  Integer.parseInt(text);
            biometricManagerHelper.authenticate(() -> {
                viewModel.updateManuallyQuantity(value);
                dialog.dismiss();
            });
        });
    }

    private void showAddDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_and_quantity, null);
        EditText et = view.findViewById(R.id.etQuantity);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Cantidad")
                .setView(view)
                .setPositiveButton("Confirmar", (d,w) ->{
                    String text = et.getText().toString();
                    if (!text.isEmpty()){
                        int value =  Integer.parseInt(text);
                        viewModel.updateIncrementManuallyQuantity(value);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}