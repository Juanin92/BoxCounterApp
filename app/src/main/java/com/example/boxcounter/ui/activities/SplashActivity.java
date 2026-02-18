package com.example.boxcounter.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.boxcounter.R;
import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.ui.auth.BiometricManagerHelper;
import com.example.boxcounter.ui.dialogs.ActiveShiftDialog;
import com.example.boxcounter.viewModel.ShiftViewModel;

import java.util.Date;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ShiftViewModel viewModel;
    private BiometricManagerHelper biometricManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ShiftViewModel.class);
        biometricManagerHelper = new BiometricManagerHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnStart = findViewById(R.id.btnStart);
        ImageButton btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        viewModel.getActiveShift().observe(this, shift -> {
            if (shift == null){
                btnStart.setText("Iniciar Turno");
                btnStart.setOnClickListener(v -> biometricManagerHelper.authenticate(() -> {
                    viewModel.startNewShift();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }));
            } else {
                showActiveShiftDialog(shift);
            }
        });
    }

    private void showActiveShiftDialog(Shift shift) {

        String message =
                "Inicio: " + new Date(shift.getStartTime()) +
                        "\nCantidad: " + shift.getQuantity() + " cajas";

        ActiveShiftDialog dialog = new ActiveShiftDialog(message,
                new ActiveShiftDialog.OnActiveShiftActionListener() {
                    @Override
                    public void onContinue() {
                        biometricManagerHelper.authenticate(() -> {
                            startActivity(new Intent(SplashActivity.this,
                                    MainActivity.class));
                            finish();
                        });
                    }

                    @Override
                    public void onFinish() {
                        biometricManagerHelper.authenticate(() -> {
                            viewModel.startNewShift();
                            startActivity(new Intent(SplashActivity.this,
                                    MainActivity.class));
                            finish();
                        });
                    }
                });
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "ActiveShiftDialog");
    }
}