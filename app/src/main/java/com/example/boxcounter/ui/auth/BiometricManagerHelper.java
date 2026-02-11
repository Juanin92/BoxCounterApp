package com.example.boxcounter.ui.auth;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricManagerHelper {

    private final Activity activity;

    public BiometricManagerHelper(Activity activity) {
        this.activity = activity;
    }

    public void authenticate(Runnable onSuccess) {

        Executor executor =
                ContextCompat.getMainExecutor(activity);

        BiometricPrompt biometricPrompt =
                new BiometricPrompt((FragmentActivity) activity, executor,
                        new BiometricPrompt.AuthenticationCallback() {

                            @Override
                            public void onAuthenticationSucceeded(
                                    @NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);

                                onSuccess.run();
                            }

                            @Override
                            public void onAuthenticationError(
                                    int errorCode,
                                    @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);

                                Toast.makeText(activity,
                                        "Autenticación cancelada",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Confirmar acción")
                        .setSubtitle("Autenticación requerida")
                        .setDeviceCredentialAllowed(true)
                        .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
