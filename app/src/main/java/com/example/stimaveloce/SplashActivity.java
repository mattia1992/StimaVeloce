package com.example.stimaveloce;
import Helpers.LicenseTimeManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // Nessun utente loggato -> Vai alla schermata di Login
            vaiALogin();
        } else {
            // Utente loggato -> Verifichiamo la licenza su Firestore e gestiamo il tempo
            String userId = currentUser.getUid();

            FirebaseFirestore.getInstance().collection("whitelist").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean abilitato = documentSnapshot.getBoolean("abilitato");
                            Long scadenzaAbbonamento = documentSnapshot.getLong("data_scadenza_abbonamento");
                            Long scadenzaProva = documentSnapshot.getLong("data_scadenza_prova");
                            long tempoAttuale = System.currentTimeMillis();

                            boolean provaValida = (scadenzaProva != null && tempoAttuale < scadenzaProva);

                            Log.i("Prova scaduta:", String.valueOf(scadenzaProva));
                            Log.i("Prova scaduta:", String.valueOf(provaValida));

                            if (Boolean.TRUE.equals(abilitato) && scadenzaAbbonamento != null) {

                                // Controllo di sicurezza su orologio / riavvio / internet
                                LicenseTimeManager.verificaAccesso(this, scadenzaAbbonamento, new LicenseTimeManager.TimeCheckCallback() {
                                    @Override
                                    public void onLicenseValid(int giorniRimanenti) {
                                        if (giorniRimanenti < 31) {
                                            String parolaGiorni = (giorniRimanenti == 1) ? "giorno" : "giorni";
                                            AlertDialog alert;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this)
                                                    .setTitle("Scadenza Abbonamento")
                                                    .setMessage("Il tuo abbonamento scadrà tra " + String.valueOf(giorniRimanenti) + " " + parolaGiorni + ".\nSe vuoi rinnovarlo scrivi a:\nstimaveloce@gmail.com, mettendo come oggetto:'rinnovo abbonamento'.\nRicordati che per il rinnovo ci voglioni circa 5 giorni.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                            alert = builder.create();
                                            alert.show();
                                            Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                            if (positive != null) {
                                                LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                layoutParams_positive_button.setMargins(5, 10, 10, 10);
                                                layoutParams_positive_button.weight = 1.0f;
                                                positive.setLayoutParams(layoutParams_positive_button);
                                                positive.setPadding(2, 2, 2, 2);
                                                positive.setTextSize(18f);
                                                positive.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
                                                positive.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.teal_700));
                                                positive.setAllCaps(false);
                                            }
                                        }else{
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onLicenseExpired() {
                                        mostraErroreBloccante("Licenza Scaduta", "Il tuo periodo di prova o abbonamento è scaduto. Effettua il rinnovo contattando stimaveloce@gmail.com.");
                                    }

                                    @Override
                                    public void onInternetRequired(String reason) {
                                        mostraErroreBloccante("Connessione richiesta", reason);
                                    }
                                });

                            } else if (provaValida) {
                                int scadenza_giorni = Math.max(0, (int) ((scadenzaProva - tempoAttuale) / (24L * 60 * 60 * 1000)));
                                String parolaGiorni = (scadenza_giorni == 1) ? "giorno" : "giorni";
                                AlertDialog alert;
                                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this)
                                        .setTitle("Scadenza Prova Gratuita")
                                        .setMessage("la tua prova gratuita scadrà tra " + String.valueOf(scadenza_giorni) +" "+parolaGiorni+ ".\nSe vuoi abbonarti scrivi a:\nstimaveloce@gmail.com, mettendo come oggetto:'informazione abbonamento'.")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
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
                                    positive.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
                                    positive.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.teal_700));
                                    positive.setAllCaps(false);
                                }

                            }else {
                                mostraErroreBloccante("Account non abilitato", "Il tuo account non risulta abilitato all'uso dell'applicazione scrivi una mail a stimaveloce@gmail.com se vuoi abbonarti, grazie.");
                            }
                        } else {
                            mostraErroreBloccante("Errore dati", "Profilo utente non trovato nella whitelist.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // In caso di errore di rete iniziale, proviamo a far deciderlo al manager con i dati locali se presenti,
                        // oppure chiediamo internet. Passiamo 0 come scadenza per forzare il controllo cache/offline.
                        LicenseTimeManager.verificaAccesso(this, 0, new LicenseTimeManager.TimeCheckCallback() {
                            @Override
                            public void onLicenseValid(int giorniRimanenti) {
                                if (giorniRimanenti < 31) {
                                    String parolaGiorni = (giorniRimanenti == 1) ? "giorno" : "giorni";
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this)
                                            .setTitle("Scadenza Abbonamento")
                                            .setMessage("Il tuo abbonamento scadrà tra " + String.valueOf(giorniRimanenti) + " " + parolaGiorni + ".\nSe vuoi rinnovarlo scrivi a:\nstimaveloce@gmail.com, mettendo come oggetto:'rinnovo abbonamento'.\nRicordati che per il rinnovo ci voglioni circa 5 giorni.")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    alert = builder.create();
                                    alert.show();
                                    Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                    if (positive != null) {
                                        LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams_positive_button.setMargins(5, 10, 10, 10);
                                        layoutParams_positive_button.weight = 1.0f;
                                        positive.setLayoutParams(layoutParams_positive_button);
                                        positive.setPadding(2, 2, 2, 2);
                                        positive.setTextSize(18f);
                                        positive.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
                                        positive.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.teal_700));
                                        positive.setAllCaps(false);
                                    }
                                }else{
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            @Override
                            public void onLicenseExpired() {
                                mostraErroreBloccante("Accesso negato", "Impossibile verificare la validità della licenza.");
                            }
                            @Override
                            public void onInternetRequired(String reason) {
                                mostraErroreBloccante("Connessione richiesta", "Impossibile connettersi a Firestore. " + reason);
                            }
                        });
                    });
        }
    }

    private void vaiALogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostraErroreBloccante(String titolo, String messaggio) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(titolo)
                .setMessage(messaggio)
                .setCancelable(false)
                .setPositiveButton("Riprova", (dialog, which) -> {
                    recreate(); // Ricarica la splash
                })
                .setNegativeButton("Esci", (dialog, which) -> {
                    finishAffinity();
                });
        alert = builder.create();
        alert.show();
        Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (positive!=null){
            LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams_positive_button.setMargins(5, 10, 10, 10);
            layoutParams_positive_button.weight = 1.0f;
            positive.setLayoutParams(layoutParams_positive_button);
            positive.setPadding(2,2,2,2);
            positive.setTextSize(18f);
            positive.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
            positive.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.teal_700));
            positive.setAllCaps(false);
        }
        if (negative!=null){
            LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams_negative_button.setMargins(5, 10, 10, 10);
            layoutParams_negative_button.weight = 1.0f;
            negative.setLayoutParams(layoutParams_negative_button);
            negative.setPadding(2,2,2,2);
            negative.setTextSize(18f);
            negative.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
            negative.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.teal_700));
            negative.setAllCaps(false);
        }
    }
}