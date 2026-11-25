package com.example.stimaveloce;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> effettuaLogin());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void effettuaLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // FONDAMENTALE: Forza il ricaricamento dei dati dal server di Firebase
                            // per vedere se l'utente ha cliccato sul link mail.
                            user.reload().addOnCompleteListener(reloadTask -> {

                                if (user.isEmailVerified()) {
                                    // 1. L'EMAIL È VERIFICATA -> Può entrare
                                    Toast.makeText(LoginActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // 2. L'EMAIL NON È VERIFICATA -> Lo buttiamo fuori all'istante
                                    mAuth.signOut();
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Email non verificata")
                                            .setMessage("Non puoi accedere. Controlla la tua posta e clicca sul link di conferma inviato durante la registrazione.")
                                            .setPositiveButton("Ok", null);
                                    alert = builder.create();
                                    alert.show();
                                    Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                    if (positive!=null){
                                        LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams_positive_button.setMargins(5, 10, 10, 10);
                                        layoutParams_positive_button.weight = 1.0f;
                                        positive.setLayoutParams(layoutParams_positive_button);
                                        positive.setPadding(2,2,2,2);
                                        positive.setTextSize(18f);
                                        positive.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
                                        positive.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.teal_700));
                                        positive.setAllCaps(false);
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Errore di login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                });
    }
}