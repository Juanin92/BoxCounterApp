package com.example.boxcounter.ui.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
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

import com.airbnb.lottie.LottieAnimationView;
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
    private final ArgbEvaluator colorEvaluator = new ArgbEvaluator();
    private LottieAnimationView lottieCelebration;
    private LottieAnimationView lottieNameClick;
    private boolean milestone100Reached =  false;


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
        lottieCelebration = findViewById(R.id.lottieCelebration);
        lottieNameClick = findViewById(R.id.lottieCelebrationName);
        View containerUser = findViewById(R.id.containerUser);

        viewModel = new ViewModelProvider(this).get(ShiftViewModel.class);
        viewModel.getActiveShift().observe(this, shift -> {
            if (shift != null){
                int quantity = shift.getQuantity();
                tvQuantity.setText(String.valueOf(quantity));

                updateQuantityColor(quantity);

                checkMilestoneCelebration(quantity);
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

        containerUser.setOnClickListener(this::triggerNameAnimation);
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

    private void updateQuantityColor(int count){
        if (tvQuantity == null) return;

        int finalColor;
        int colorWhite = Color.parseColor("#F8FAFC");
        int colorYellow = Color.parseColor("#F59E0B");
        int colorGreen = Color.parseColor("#22C55E");
        int colorGold = Color.parseColor("#00E5FF");

        if (count < 50){
            float fraction = count / 50.0f;
            finalColor = (int) colorEvaluator.evaluate(fraction, colorWhite, colorYellow);
        } else if (count < 100) {
            float fraction = (count - 50) / 50.0f;
            finalColor = (int) colorEvaluator.evaluate(fraction, colorYellow, colorGreen);
        } else if (count < 150) {
            float fraction = (count - 100) / 50.0f;
            finalColor = (int) colorEvaluator.evaluate(fraction, colorGreen, colorGold);
        } else {
            finalColor = colorGold;
        }

        tvQuantity.setTextColor(finalColor);
    }

    private void checkMilestoneCelebration(int count){
        if (count >= 100 && !milestone100Reached){
            milestone100Reached = true;
            triggerFireWorksAnimation();
        } else if (count < 100) {
            milestone100Reached = false;
        }
    }

    private void triggerFireWorksAnimation(){
        if (lottieCelebration != null){
            lottieCelebration.setVisibility(View.VISIBLE);
            lottieCelebration.setSpeed(0.4f);
            lottieCelebration.playAnimation();

            lottieCelebration.addAnimatorListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation){
                    lottieCelebration.setVisibility(View.GONE);
                }
            });
        }
    }

    private void triggerNameAnimation(View view){
        if (view != null) {
            view.animate()
                    .scaleX(1.08f)
                    .scaleY(1.08f)
                    .setDuration(100)
                    .withEndAction(() -> view.animate().scaleX(1.0f).scaleY(1.0f)
                            .setDuration(100).start())
                    .start();
        }

        if (lottieNameClick != null) {
            lottieNameClick.setVisibility(View.VISIBLE);
            lottieNameClick.setSpeed(1.2f);
            lottieNameClick.playAnimation();

            lottieNameClick.removeAllAnimatorListeners();
            lottieNameClick.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    lottieNameClick.setVisibility(View.GONE);
                }
            });
        }
    }
}