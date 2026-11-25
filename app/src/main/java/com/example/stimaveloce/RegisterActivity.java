package com.example.stimaveloce;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference identificativi_prova_gratuita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("AndoridID", androidId);
        identificativi_prova_gratuita = db.collection("dispositivi_prova").document(androidId);
        identificativi_prova_gratuita.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                AlertDialog alert_prova;
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Dispositivo già registrato")
                        .setMessage("Su questo dispositivo è già stata utilizzata una prova gratuita.\nPuoi comunque effettuare l'accesso se possiedi un account attivo.")
                        .setCancelable(false)
                        .setPositiveButton("Vai al Login", (dialog, which) -> {
                            finish(); // Torna al login
                        });
                alert_prova = builder.create();
                alert_prova.show();
                Button positive = alert_prova.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
            }
        });
        etNome = findViewById(R.id.etNomeReg);
        etEmail = findViewById(R.id.etEmailreg);
        etPassword = findViewById(R.id.etPasswordreg);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> eseguiRegistrazione());
    }

    private void eseguiRegistrazione() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Creazione utente su Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {

                            // 2. INVIO EMAIL DI VERIFICA
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(verifyTask -> {
                                        if (verifyTask.isSuccessful()) {
                                            // L'email di verifica è stata inviata, ora salviamo i dati bloccati su Firestore
                                            salvaUtenteBloccatoSuFirestore(firebaseUser, nome, email);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Errore nell'invio dell'email di verifica: " + verifyTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "Questa email è già associata a un account esistente.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Errore di registrazione: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
        private void salvaUtenteBloccatoSuFirestore(FirebaseUser firebaseUser, String nome, String email) {
            String userId = firebaseUser.getUid();
            long currentTime = System.currentTimeMillis();
            long trialExpirationTime = currentTime + (30L * 24L * 60L * 60L * 1000L);
            // 3. Preparazione dei dati: PROVA RIMOSSA. L'utente nasce DISABILITATO e SCADUTO finché non paga.
            Map<String, Object> userData = new HashMap<>();
            userData.put("Nome", nome);
            userData.put("email", email);
            userData.put("abilitato", false); // Cambiato a false
            userData.put("stato_licenza", "scaduto"); // Cambiato a scaduto (niente più prova da 7 giorni)
            userData.put("rinnovo", false);
            userData.put("data_registrazione", currentTime);
            userData.put("data_scadenza_prova", trialExpirationTime);
            userData.put("data_scadenza_abbonamento", currentTime);

            // 4. Salvataggio sulla collezione "whitelist"
            db.collection("whitelist").document(userId)
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {

                        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        Map<String, String> dispositivoData = new HashMap<>();
                        dispositivoData.put("ID", androidId);
                        dispositivoData.put("email", email); // Utile per sapere quale email ha registrato questo telefono

                        db.collection("dispositivi_prova").document(androidId)
                                .set(dispositivoData)
                                .addOnSuccessListener(unused -> Log.i("Firebase", "Dispositivo salvato in dispositivi_prova"))
                                .addOnFailureListener(e -> Log.e("Firebase", "Errore salvataggio dispositivi_prova: " + e.getMessage()));
                        // Forziamo il logout immediato, così l'utente non risulta loggato a metà
                        mAuth.signOut();

                        // Avviso esplicito per la verifica dell'email
                        AlertDialog alert;
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Verifica il tuo Account")
                                .setMessage("Registrazione completata!\n\nTi abbiamo inviato un link di conferma alla tua email.\nPer favore, clicca sul link prima di effettuare l'accesso.")
                                .setCancelable(false)
                                .setPositiveButton("Ho capito", (dialog, which) -> {
                                    // Lo rimandiamo alla LoginActivity (o alla schermata iniziale)
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // Assicurati che si chiami LoginActivity
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                });
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
                            positive.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                            positive.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.teal_700));
                            positive.setAllCaps(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegisterActivity.this, "Errore salvataggio dati su Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        }
    }