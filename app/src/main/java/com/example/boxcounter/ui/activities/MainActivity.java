package com.example.boxcounter.ui.activities;

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
import com.example.boxcounter.ui.dialogs.ManualAddDialog;
import com.example.boxcounter.viewModel.ShiftViewModel;

public class MainActivity extends AppCompatActivity {

    private ShiftViewModel viewModel;
    private TextView tvQuantity;
    private BiometricManagerHelper biometricManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        biometricManagerHelper = new BiometricManagerHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvQuantity = findViewById(R.id.tvQuantity);
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnFinish = findViewById(R.id.btnFinish);

        viewModel = new ViewModelProvider(this).get(ShiftViewModel.class);
        viewModel.getActiveShift().observe(this, shift -> {
            if (shift != null){
                tvQuantity.setText(String.valueOf(shift.getQuantity()));
            }
        });

        btnPlus.setOnClickListener(v -> viewModel.increment());
        btnPlus.setOnLongClickListener(v -> {
            showAddDialog();
            return true;
        });

        btnMinus.setOnClickListener(v -> viewModel.decrement());
        btnFinish.setOnClickListener(v -> biometricManagerHelper.authenticate(() -> {
            viewModel.finish();

            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }));

        ImageButton btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> startActivity(
                new Intent(this, HistoryActivity.class)));

        tvQuantity.setOnLongClickListener(v -> {
            ManualAddDialog dialog = ManualAddDialog.newInstance(value -> {
                biometricManagerHelper.authenticate(() -> viewModel.updateManuallyQuantity(value));
            });
            dialog.show(getSupportFragmentManager(), "ManualAddDialog");
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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