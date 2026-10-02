package com.example.boxcounter.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.boxcounter.R;
import com.example.boxcounter.ui.auth.BiometricManagerHelper;
import com.example.boxcounter.ui.dialogs.AddQuantityDialog;
import com.example.boxcounter.ui.dialogs.EditNameDialog;
import com.example.boxcounter.ui.dialogs.ManualAddDialog;
import com.example.boxcounter.utils.NotificationHelper;
import com.example.boxcounter.utils.UserPreferences;
import com.example.boxcounter.viewModel.ShiftViewModel;

public class MainActivity extends AppCompatActivity {

    private ShiftViewModel viewModel;
    private TextView tvQuantity;
    private TextView tvUserName;
    private ImageView btnEditName;
    private BiometricManagerHelper biometricManagerHelper;
    private UserPreferences userPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        biometricManagerHelper = new BiometricManagerHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvQuantity = findViewById(R.id.tvQuantity);
        tvUserName = findViewById(R.id.tvUserName);
        btnEditName = findViewById(R.id.btnEditName);
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
            AddQuantityDialog dialog = AddQuantityDialog.newInstance(value ->
                    biometricManagerHelper.authenticate(() ->
                    viewModel.updateIncrementManuallyQuantity(value)));
            dialog.show(getSupportFragmentManager(), "AddQuantityDialog");
            return true;
        });

        btnMinus.setOnClickListener(v -> viewModel.decrement());
        btnFinish.setOnClickListener(v -> biometricManagerHelper.authenticate(() -> {
            viewModel.finish();
            NotificationHelper.stopService(this);

            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }));

        ImageButton btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> startActivity(
                new Intent(this, HistoryActivity.class)));

        tvQuantity.setOnLongClickListener(v -> {
            ManualAddDialog dialog = ManualAddDialog.newInstance(value ->
                    biometricManagerHelper.authenticate(() ->
                            viewModel.updateManuallyQuantity(value)));
            dialog.show(getSupportFragmentManager(), "ManualAddDialog");
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPreferences = new UserPreferences(this);
        checkAndPromptUserName();

        btnEditName.setOnClickListener(v -> showEditNameDialog());
        tvUserName.setOnClickListener(v -> triggerFireWorksAnimation());
    }

    private void checkAndPromptUserName(){
        if (!userPreferences.hasUserName()) {
            showEditNameDialog();
        }else {
            updateUserNameUI();
        }
    }

    private void showEditNameDialog(){
        boolean isCancelable = userPreferences.hasUserName();
        String currentName = userPreferences.getUserName();

        EditNameDialog dialog = EditNameDialog.newInstance(currentName, isCancelable, newName -> {
            userPreferences.saveUserName(newName);
            updateUserNameUI();
        });

        dialog.show(getSupportFragmentManager(), "EditNameDialog");
    }

    private void updateUserNameUI() {
        if (tvUserName != null && userPreferences.hasUserName()) {
            tvUserName.setText(userPreferences.getUserName());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        if (action == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (viewModel != null){
                        viewModel.increment();
                    }
                    return true;

                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (viewModel != null){
                        viewModel.decrement();
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void triggerFireWorksAnimation(){
    }
}