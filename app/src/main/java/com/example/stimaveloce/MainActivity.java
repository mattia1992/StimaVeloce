package com.example.stimaveloce;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import Helpers.ExcelManager;
import Helpers.Compute;
import Helpers.Compute.specie;
import Helpers.PreferencesHelper;
public class MainActivity extends AppCompatActivity {
private ImageButton nuovo_file_bottone;
private ImageButton apri;
private ImageButton condividi;
private Button larice;
private Button abete;
private Button faggio;
private Button abeteb;
private Button pinos;
private Button pinoc;
private Button pinon;
private EditText diametro;
private EditText altezza;
private Spinner tariffa_larice;
private Spinner tariffa_abete;
private Spinner tariffa_faggio;
private Spinner tariffa_abeteb;
private Spinner tariffa_pinos;
private Spinner tariffa_pinoc;
private Spinner tariffa_pinon;
private TextView mc_larice;
private TextView mc_abete;
private TextView mc_abeteb;
private TextView mc_faggio;
private TextView mc_pinos;
private TextView mc_pinoc;
private TextView mc_pinon;
private TextView mc_totale;
private TextView n_larice;
private TextView n_abete;
private TextView n_abeteb;
private TextView n_faggio;
private TextView n_pinos;
private TextView n_pinoc;
private TextView n_pinon;
private TextView n_totale;
private AlertDialog nuovo_;
private AlertDialog share;
private AlertDialog open;
private  View dialogView_nuovo;
private CheckBox larice_check;
private CheckBox abete_check;
private CheckBox faggio_check;
private CheckBox abeteb_check;
private CheckBox pinos_check;
private CheckBox pinoc_check;
private CheckBox pinon_check;

private LinearLayout linear_larice_abete;
private LinearLayout linear_faggio_abeteb;
private LinearLayout linear_pinos_pinc;
private LinearLayout linear_pinon_button;
private LinearLayout linear_larice;
private LinearLayout linear_abete;
private LinearLayout linear_abeteb;
private LinearLayout linear_faggio;
private LinearLayout linear_pinos;
private LinearLayout linear_pinoc;
private LinearLayout linear_pinon;
private LinearLayout linear_totali;

int tariffa;
ProgressBar progressBar;
private HashMap<String, Integer> mappa_tariffe = new LinkedHashMap<>();
private HashMap<String, View> mappa_widget = new LinkedHashMap<>();
private PreferencesHelper stimaVeloce_pref;
private ExcelManager excelManager;
final String[] PERMISSIONS = {
        android.Manifest.permission.INTERNET,
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            // 3. Nasconde sia i bottoni sotto che l'ora/batteria sopra
            controller.hide(WindowInsetsCompat.Type.systemBars());

            // 4. Fa sì che le barre riappaiano solo con uno swipe e siano temporanee
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
        setContentView(R.layout.activity_main);

        File field_dir = new File(this.getFilesDir(), "piedilista");
        if (!field_dir.exists()) {
            field_dir.mkdir();
        }
//        HttpHelper httpHelper = new HttpHelper(this);
//        if (httpHelper.isNetworkConnected()) {
//            if (httpHelper.isLicenceActive()) {
//                int days = httpHelper.day_to_exiperation();
//                Toast.makeText(this, "Validità residua " + String.valueOf(days) + " giorni.", Toast.LENGTH_LONG).show();
//            } else {
//                finish();
//                System.exit(0);
//            }
//        } else {
//            Toast.makeText(this, "Attivare internet!", Toast.LENGTH_LONG).show();
//            finish();
//            System.exit(0);
//        }
        LayoutInflater inflater = getLayoutInflater();
        dialogView_nuovo = inflater.inflate(R.layout.alert_frame_nuovo, null);
        stimaVeloce_pref = new PreferencesHelper(this);
        excelManager = new ExcelManager();
        tariffa = 0;
        mappa_tariffe.put("I", 1);
        mappa_tariffe.put("II", 2);
        mappa_tariffe.put("III", 3);
        mappa_tariffe.put("IV", 4);
        mappa_tariffe.put("V", 5);
        mappa_tariffe.put("VI", 6);
        mappa_tariffe.put("VII", 7);
        mappa_tariffe.put("VIII", 8);
        mappa_tariffe.put("IX", 9);

        progressBar = findViewById(R.id.progressBar);
        nuovo_file_bottone = (ImageButton) findViewById(R.id.nuovo);
        apri = findViewById(R.id.apri);
        condividi = (ImageButton) findViewById(R.id.condividi);
        larice = (Button) findViewById(R.id.larice);
        abete = (Button) findViewById(R.id.abete);
        faggio = (Button) findViewById(R.id.faggio);
        abeteb = (Button) findViewById(R.id.abeteb);
        pinos = (Button) findViewById(R.id.pinos);
        pinoc = (Button) findViewById(R.id.pinoc);
        pinon = (Button) findViewById(R.id.pinon);
//        diametro = (EditText) findViewById(R.id.diametro);
//        altezza = (EditText) findViewById(R.id.altezza);
        tariffa_larice = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_larice);
        tariffa_abete = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_abete);
        tariffa_faggio = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_faggio);
        tariffa_abeteb = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_abeteb);
        tariffa_pinoc = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_pinoc);
        tariffa_pinon = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_pinon);
        tariffa_pinos = (Spinner) dialogView_nuovo.findViewById(R.id.tariffa_pinos);
        larice_check = dialogView_nuovo.findViewById(R.id.checklarice);
        abete_check = dialogView_nuovo.findViewById(R.id.checkabete);
        faggio_check = dialogView_nuovo.findViewById(R.id.checkfaggio);
        abeteb_check = dialogView_nuovo.findViewById(R.id.checkabetebianco);
        pinos_check = dialogView_nuovo.findViewById(R.id.checkpinosilvestre);
        pinoc_check = dialogView_nuovo.findViewById(R.id.checkpinocembro);
        pinon_check = dialogView_nuovo.findViewById(R.id.checkpinonero);

        linear_larice_abete = findViewById(R.id.linear_abete_larice);
        linear_faggio_abeteb = findViewById(R.id.linear_faggio_abeteb);
        linear_pinos_pinc = findViewById(R.id.linear_pinos_pinoc);
        linear_pinon_button = findViewById(R.id.linear_pinon_button);

        linear_larice = findViewById(R.id.linear_larice);
        linear_abete = findViewById(R.id.linear_abete);
        linear_abeteb = findViewById(R.id.linear_abeteb);
        linear_faggio = findViewById(R.id.linear_faggio);
        linear_pinos = findViewById(R.id.linear_pinos);
        linear_pinoc = findViewById(R.id.linear_pinoc);
        linear_pinon = findViewById(R.id.linear_pinon);
        linear_totali = findViewById(R.id.linear_totali);

        mappa_widget.put("larice_1", linear_larice);
        mappa_widget.put("larice_2", larice);
        mappa_widget.put("larice_3", linear_larice_abete);
        mappa_widget.put("l", tariffa_larice);

        mappa_widget.put("abete_1", linear_abete);
        mappa_widget.put("abete_2", abete);
        mappa_widget.put("abete_3", linear_larice_abete);
        mappa_widget.put("a", tariffa_abete);

        mappa_widget.put("faggio_1", linear_faggio);
        mappa_widget.put("faggio_2", faggio);
        mappa_widget.put("faggio_3", linear_faggio_abeteb);
        mappa_widget.put("f", tariffa_faggio);

        mappa_widget.put("abeteb_1", linear_abeteb);
        mappa_widget.put("abeteb_2", abeteb);
        mappa_widget.put("abeteb_3", linear_faggio_abeteb);
        mappa_widget.put("ab", tariffa_abeteb);

        mappa_widget.put("pinos_1", linear_pinos);
        mappa_widget.put("pinos_2", pinos);
        mappa_widget.put("pinos_3", linear_pinos_pinc);
        mappa_widget.put("ps", tariffa_pinos);

        mappa_widget.put("pinoc_1", linear_pinoc);
        mappa_widget.put("pinoc_2", pinoc);
        mappa_widget.put("pinoc_3", linear_pinos_pinc);
        mappa_widget.put("pc", tariffa_pinoc);

        mappa_widget.put("pinon_1", linear_pinon);
        mappa_widget.put("pinon_2", pinon);
        mappa_widget.put("pinon_3", linear_pinon_button);
        mappa_widget.put("pn", tariffa_pinon);

        String[] tariffe = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tariffe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String[] tariffe_faggio = {"III", "IV", "V", "VI", "VII", "VIII", "IX"};
        ArrayAdapter<String> adapter_faggio = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tariffe_faggio);
        adapter_faggio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String[] tariffe_pinoc = {"V", "VI", "VII", "VIII", "IX"};
        ArrayAdapter<String> adapter_pinoc = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tariffe_pinoc);
        adapter_faggio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tariffa_larice.setAdapter(adapter);
        tariffa_abete.setAdapter(adapter);
        tariffa_faggio.setAdapter(adapter_faggio);
        tariffa_pinoc.setAdapter(adapter_pinoc);
        tariffa_pinos.setAdapter(adapter);
        tariffa_pinon.setAdapter(adapter);
        tariffa_abeteb.setAdapter(adapter);
        mc_larice = (TextView) findViewById(R.id.mc_larice);
        mc_abete = (TextView) findViewById(R.id.mc_abete);
        mc_abeteb = (TextView) findViewById(R.id.mc_abeteb);
        mc_faggio = (TextView) findViewById(R.id.mc_faggio);
        mc_pinos = (TextView) findViewById(R.id.mc_pinos);
        mc_pinoc = (TextView) findViewById(R.id.mc_pinoc);
        mc_pinon = (TextView) findViewById(R.id.mc_pinon);
        mc_totale = (TextView) findViewById(R.id.mc_totale);
        List<TextView> tutte_mc = Arrays.asList(mc_larice, mc_abete, mc_abeteb, mc_faggio, mc_pinos, mc_pinoc, mc_pinon);
        n_larice = (TextView) findViewById(R.id.n_larice);
        n_abete = (TextView) findViewById(R.id.n_abete);
        n_abeteb = (TextView) findViewById(R.id.n_abeteb);
        n_faggio = (TextView) findViewById(R.id.n_faggio);
        n_pinos = (TextView) findViewById(R.id.n_pinos);
        n_pinoc = (TextView) findViewById(R.id.n_pinoc);
        n_pinon = (TextView) findViewById(R.id.n_pinon);
        n_totale = (TextView) findViewById(R.id.n_totale);
        List<TextView> tutte_n = Arrays.asList(n_larice, n_abete, n_abeteb, n_faggio, n_pinos, n_pinoc, n_pinon);
        collegaGruppoATotale(tutte_mc, mc_totale, false);
        collegaGruppoATotale(tutte_n, n_totale, true);
        Compute compute = new Compute(this);

        nuovo_file_bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tariffa = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                EditText edit = dialogView_nuovo.findViewById(R.id.nome_file);
                TextView titolo = dialogView_nuovo.findViewById(R.id.title_alert_nuovo);
                titolo.setText("Nuovo rilievo");
                if (dialogView_nuovo.getParent()!=null){
                    ((ViewGroup) dialogView_nuovo.getParent()).removeView(dialogView_nuovo);
                }
                File dir = new File(MainActivity.this.getFilesDir()+"/piedilista");
                File [] lista_campi_file = dir.listFiles();
                ArrayList<String> lista_campi = new ArrayList<>();
                if (lista_campi_file!=null) {
                    for (File folder : lista_campi_file
                    ) {
                        if (!lista_campi.contains(folder.getName().split("\\.")[0])) {
                            lista_campi.add(folder.getName().split("\\.")[0]);
                        }
                    }
                }
                Log.i("Lista Campi", lista_campi.toString());
                builder
                        .setView(dialogView_nuovo)
                        .setCancelable(false)
                        .setPositiveButton("Ok", null)
                                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        nuovo_.dismiss();
                                    }
                                });

                nuovo_ = builder.create();

                nuovo_.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positive = nuovo_.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative = nuovo_.getButton(AlertDialog.BUTTON_NEGATIVE);
                        if (positive!=null){
                            LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams_positive_button.setMargins(5, 10, 10, 10);
                            layoutParams_positive_button.weight = 1.0f;
                            positive.setLayoutParams(layoutParams_positive_button);
                            positive.setPadding(2,2,2,2);
                            positive.setTextSize(18f);
                            positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                            positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                            positive.setAllCaps(false);
                        }
                        if (negative!=null){
                            LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams_negative_button.setMargins(5, 10, 10, 10);
                            layoutParams_negative_button.weight = 1.0f;
                            negative.setLayoutParams(layoutParams_negative_button);
                            negative.setPadding(2,2,2,2);
                            negative.setTextSize(18f);
                            negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                            negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                            negative.setAllCaps(false);
                        }
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!edit.getText().toString().isEmpty()) {
                                    Log.i("nuovo", lista_campi.toString());
                                    if(!lista_campi.contains(edit.getText().toString())) {
                                        Log.i("nuovo", "dentro");
                                        stimaVeloce_pref.setStringPref(PreferencesHelper.PREF.nome_file, edit.getText().toString());
                                        mc_larice.setText("");
                                        mc_abete.setText("");
                                        mc_faggio.setText("");
                                        mc_abeteb.setText("");
                                        mc_pinos.setText("");
                                        mc_pinoc.setText("");
                                        mc_pinon.setText("");
                                        mc_totale.setText("");
                                        n_larice.setText("");
                                        n_abete.setText("");
                                        n_faggio.setText("");
                                        n_abeteb.setText("");
                                        n_pinos.setText("");
                                        n_pinoc.setText("");
                                        n_pinon.setText("");
                                        n_totale.setText("");
                                        if (linear_totali.getVisibility()==View.VISIBLE) {
                                            linear_totali.setVisibility(View.GONE);
                                        }
                                        if (!abete_check.isChecked() && abete.getVisibility() == View.VISIBLE) {
                                            abete.setVisibility(View.GONE);
                                            linear_abete.setVisibility(View.GONE);
                                            if (larice.getVisibility() == View.GONE) {
                                                linear_larice_abete.setVisibility(View.GONE);
                                            }
                                        } else if (abete_check.isChecked() && abete.getVisibility() == View.GONE) {
                                            if (linear_larice_abete.getVisibility() == View.GONE) {
                                                linear_larice_abete.setVisibility(View.VISIBLE);
                                            }
                                            abete.setVisibility(View.VISIBLE);
                                            linear_abete.setVisibility(View.VISIBLE);
                                        }
                                        if (!larice_check.isChecked() && larice.getVisibility() == View.VISIBLE) {
                                            larice.setVisibility(View.GONE);
                                            linear_larice.setVisibility(View.GONE);
                                            if (abete.getVisibility() == View.GONE) {
                                                linear_larice_abete.setVisibility(View.GONE);
                                            }
                                        } else if (larice_check.isChecked() && larice.getVisibility() == View.GONE) {
                                            if (linear_larice_abete.getVisibility() == View.GONE) {
                                                linear_larice_abete.setVisibility(View.VISIBLE);
                                            }
                                            larice.setVisibility(View.VISIBLE);
                                            linear_larice.setVisibility(View.VISIBLE);
                                        }
                                        if (!faggio_check.isChecked() && faggio.getVisibility() == View.VISIBLE) {
                                            faggio.setVisibility(View.GONE);
                                            linear_faggio.setVisibility(View.GONE);
                                            if (abeteb.getVisibility() == View.GONE) {
                                                linear_faggio_abeteb.setVisibility(View.GONE);
                                            }
                                        } else if (faggio_check.isChecked() && faggio.getVisibility() == View.GONE) {
                                            if (linear_faggio_abeteb.getVisibility() == View.GONE) {
                                                linear_faggio_abeteb.setVisibility(View.VISIBLE);
                                            }
                                            faggio.setVisibility(View.VISIBLE);
                                            linear_faggio.setVisibility(View.VISIBLE);
                                        }
                                        if (!abeteb_check.isChecked() && abeteb.getVisibility() == View.VISIBLE) {
                                            abeteb.setVisibility(View.GONE);
                                            linear_abeteb.setVisibility(View.GONE);
                                            if (faggio.getVisibility() == View.GONE) {
                                                linear_faggio_abeteb.setVisibility(View.GONE);
                                            }
                                        } else if (abeteb_check.isChecked() && abeteb.getVisibility() == View.GONE) {
                                            if (linear_faggio_abeteb.getVisibility() == View.GONE) {
                                                linear_faggio_abeteb.setVisibility(View.VISIBLE);
                                            }
                                            abeteb.setVisibility(View.VISIBLE);
                                            linear_abeteb.setVisibility(View.VISIBLE);
                                        }
                                        if (!pinos_check.isChecked() && pinos.getVisibility() == View.VISIBLE) {
                                            pinos.setVisibility(View.GONE);
                                            linear_pinos.setVisibility(View.GONE);
                                            if (pinoc.getVisibility() == View.GONE) {
                                                linear_pinos_pinc.setVisibility(View.GONE);
                                            }
                                        } else if (pinos_check.isChecked() && pinos.getVisibility() == View.GONE) {
                                            if (linear_pinos_pinc.getVisibility() == View.GONE) {
                                                linear_pinos_pinc.setVisibility(View.VISIBLE);
                                            }
                                            pinos.setVisibility(View.VISIBLE);
                                            linear_pinos.setVisibility(View.VISIBLE);
                                        }
                                        if (!pinoc_check.isChecked() && pinoc.getVisibility() == View.VISIBLE) {
                                            pinoc.setVisibility(View.GONE);
                                            linear_pinoc.setVisibility(View.GONE);
                                            if (pinos.getVisibility() == View.GONE) {
                                                linear_pinos_pinc.setVisibility(View.GONE);
                                            }
                                        } else if (pinoc_check.isChecked() && pinoc.getVisibility() == View.GONE) {
                                            if (linear_pinos_pinc.getVisibility() == View.GONE) {
                                                linear_pinos_pinc.setVisibility(View.VISIBLE);
                                            }
                                            pinoc.setVisibility(View.VISIBLE);
                                            linear_pinoc.setVisibility(View.VISIBLE);
                                        }
                                        if (!pinon_check.isChecked() && pinon.getVisibility() == View.VISIBLE) {
                                            linear_pinon_button.setVisibility(View.GONE);
                                            pinon.setVisibility(View.GONE);
                                            linear_pinon.setVisibility(View.GONE);
                                        } else if (pinon_check.isChecked() && pinon.getVisibility() == View.GONE) {
                                            linear_pinon_button.setVisibility(View.VISIBLE);
                                            pinon.setVisibility(View.VISIBLE);
                                            linear_pinon.setVisibility(View.VISIBLE);
                                        }
                                        linear_totali.setVisibility(View.VISIBLE);
                                        nuovo_.dismiss();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Nome File Esistente", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });
                nuovo_.show();

            }
        });

        apri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_open, null);
                Spinner spinner = dialogView.findViewById(R.id.open_spinner);
                File dir = new File(MainActivity.this.getFilesDir()+"/piedilista");
                File [] lista_campi_file = dir.listFiles();
                ArrayList<String> lista_campi = new ArrayList<>();
                for (File folder: lista_campi_file
                ) {
                    Log.i("lista rilievi", folder.getName());
                    String nomeBase = folder.getName().split("\\.")[0].trim();
                    if (!nomeBase.isEmpty() && !lista_campi.contains(nomeBase)) {
                        lista_campi.add(folder.getName().split("\\.")[0]);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, lista_campi);
                spinner.setAdapter(adapter);
                builder.setView(dialogView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (spinner.getSelectedItem()!=null) {
                                    String nome_f = spinner.getSelectedItem().toString();
                                    stimaVeloce_pref.setStringPref(PreferencesHelper.PREF.nome_file, nome_f);
                                    File json_file = new File(getFilesDir() + "/piedilista/" + nome_f + ".json");
                                    Log.i("json_key_nome_file", nome_f);
                                    if (json_file.exists()) {
                                        Log.i("json_key_file_esiste", "il file esiste");
                                        mc_larice.setText("");
                                        mc_abete.setText("");
                                        mc_faggio.setText("");
                                        mc_abeteb.setText("");
                                        mc_pinos.setText("");
                                        mc_pinoc.setText("");
                                        mc_pinon.setText("");
                                        mc_totale.setText("");
                                        n_larice.setText("");
                                        n_abete.setText("");
                                        n_faggio.setText("");
                                        n_abeteb.setText("");
                                        n_pinos.setText("");
                                        n_pinoc.setText("");
                                        n_pinon.setText("");
                                        n_totale.setText("");
                                        if (linear_larice.getVisibility()==View.VISIBLE){
                                            linear_larice.setVisibility(View.GONE);
                                        }
                                        if (linear_abete.getVisibility()==View.VISIBLE){
                                            linear_abete.setVisibility(View.GONE);
                                        }
                                        if (linear_faggio.getVisibility()==View.VISIBLE){
                                            linear_faggio.setVisibility(View.GONE);
                                        }
                                        if (linear_abeteb.getVisibility()==View.VISIBLE){
                                            linear_abeteb.setVisibility(View.GONE);
                                        }
                                        if (linear_pinos.getVisibility()==View.VISIBLE){
                                            linear_pinos.setVisibility(View.GONE);
                                        }
                                        if (linear_pinoc.getVisibility()==View.VISIBLE){
                                            linear_pinoc.setVisibility(View.GONE);
                                        }
                                        if (linear_pinon.getVisibility()==View.VISIBLE){
                                            linear_pinon.setVisibility(View.GONE);
                                        }
                                        if (linear_larice_abete.getVisibility()==View.VISIBLE){
                                            linear_larice_abete.setVisibility(View.GONE);
                                        }
                                        if (larice.getVisibility()==View.VISIBLE){
                                            larice.setVisibility(View.GONE);
                                        }
                                        if (abete.getVisibility()==View.VISIBLE){
                                            abete.setVisibility(View.GONE);
                                        }
                                        if (linear_faggio_abeteb.getVisibility()==View.VISIBLE){
                                            linear_faggio_abeteb.setVisibility(View.GONE);
                                        }
                                        if (faggio.getVisibility()==View.VISIBLE){
                                            faggio.setVisibility(View.GONE);
                                        }
                                        if (abeteb.getVisibility()==View.VISIBLE){
                                            abeteb.setVisibility(View.GONE);
                                        }
                                        if (linear_pinos_pinc.getVisibility()==View.VISIBLE){
                                            linear_pinos_pinc.setVisibility(View.GONE);
                                        }
                                        if (pinos.getVisibility()==View.VISIBLE){
                                            pinos.setVisibility(View.GONE);
                                        }
                                        if (pinoc.getVisibility()==View.VISIBLE){
                                            pinoc.setVisibility(View.GONE);
                                        }
                                        if (linear_pinon_button.getVisibility()==View.VISIBLE){
                                            linear_pinon_button.setVisibility(View.GONE);
                                        }
                                        if (pinon.getVisibility()==View.VISIBLE){
                                            pinon.setVisibility(View.GONE);
                                        }
                                        if (linear_totali.getVisibility() == View.VISIBLE){
                                            linear_totali.setVisibility(View.GONE);
                                        }
                                        try {
                                            JSONObject json = readFromFile(MainActivity.this, nome_f);
                                            Log.i("json_key_names", json.names().toString());
                                            ArrayList<String> specie_univoci = new ArrayList<>();
                                            JSONArray names = json.names();
                                            for (int a=0; a<names.length(); a++){
                                                String name = names.getString(a).split("_")[1];
                                                if (!specie_univoci.contains(name)){
                                                    specie_univoci.add(name);
                                                }
                                            }
                                            Log.i("json_key_univoci", specie_univoci.toString());
                                            for (String name: specie_univoci
                                                 ) {
                                                if (name.length()>2){
                                                    Log.i("json_key_name", name);
                                                    for (int s=1; s<4; s++) {
                                                        View widget = mappa_widget.get(name+"_"+String.valueOf(s));

                                                        if (widget.getVisibility()==View.GONE){
                                                            Log.i("json_key_numero", name+"_"+String.valueOf(s));
                                                            widget.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }else {
                                                    Spinner widget = (Spinner) mappa_widget.get(name);
                                                    String tariffa_ = json.getString("tariffa_" + name);
                                                    if (name.equals("f")){
                                                        widget.setSelection(mappa_tariffe.get(tariffa_)-3);
                                                    }else if (name.equals("pc")){
                                                        widget.setSelection(mappa_tariffe.get(tariffa_)-5);
                                                    }else {
                                                        widget.setSelection(mappa_tariffe.get(tariffa_)-1);
                                                    }
                                                }
                                                if (name.equals("faggio")) {
                                                    String tariffa_ = json.getString("tariffa_f");
                                                    JSONArray diametro_faggio = (JSONArray) json.get("diametro_faggio");
                                                    float[] mc_n = compute.compute_mc_n(specie.Faggio, diametro_faggio, mappa_tariffe.get(tariffa_)-2);
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_faggio.setText(mc);
                                                    n_faggio.setText(n);
                                                }else if (name.equals("abete")){
                                                    String tariffa_ = json.getString("tariffa_a");
                                                    JSONArray diametro_abete = (JSONArray) json.get("diametro_abete");
                                                    float[] mc_n = compute.compute_mc_n(specie.Abeter, diametro_abete, mappa_tariffe.get(tariffa_));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_abete.setText(mc);
                                                    n_abete.setText(n);
                                                } else if (name.equals("larice")) {
                                                    String tariffa_ = json.getString("tariffa_l");
                                                    JSONArray diametro_larice = (JSONArray) json.get("diametro_larice");
                                                    Log.i("json_key_tariffa", String.valueOf(mappa_tariffe.get(tariffa_)));
                                                    float[] mc_n = compute.compute_mc_n(specie.Larice, diametro_larice, mappa_tariffe.get(tariffa_));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_larice.setText(mc);
                                                    n_larice.setText(n);
                                                }else if (name.equals("abeteb")) {
                                                    String tariffa_ = json.getString("tariffa_ab");
                                                    JSONArray diametro_abeteb = (JSONArray) json.get("diametro_abeteb");
                                                    Log.i("json_key_tariffa", String.valueOf(mappa_tariffe.get(tariffa_)));
                                                    float[] mc_n = compute.compute_mc_n(specie.Abeteb, diametro_abeteb, mappa_tariffe.get(tariffa_));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_abeteb.setText(mc);
                                                    n_abeteb.setText(n);
                                                }else if (name.equals("pinos")) {
                                                    String tariffa_ = json.getString("tariffa_ps");
                                                    JSONArray diametro_pinos = (JSONArray) json.get("diametro_pinos");
                                                    Log.i("json_key_tariffa", String.valueOf(mappa_tariffe.get(tariffa_)));
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinos, diametro_pinos, mappa_tariffe.get(tariffa_));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinos.setText(mc);
                                                    n_pinos.setText(n);
                                                }else if (name.equals("pinoc")) {
                                                    String tariffa_ = json.getString("tariffa_pc");
                                                    JSONArray diametro_pinoc = (JSONArray) json.get("diametro_pinoc");
                                                    Log.i("json_key_tariffa", String.valueOf(mappa_tariffe.get(tariffa_)));
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinoc, diametro_pinoc, mappa_tariffe.get(tariffa_)-4);
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinoc.setText(mc);
                                                    n_pinoc.setText(n);
                                                }else if (name.equals("pinon")) {
                                                    String tariffa_ = json.getString("tariffa_pn");
                                                    JSONArray diametro_pinon = (JSONArray) json.get("diametro_pinon");
                                                    Log.i("json_key_tariffa", String.valueOf(mappa_tariffe.get(tariffa_)));
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinon, diametro_pinon, mappa_tariffe.get(tariffa_));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinon.setText(mc);
                                                    n_pinon.setText(n);
                                                }
                                            }
                                            linear_totali.setVisibility(View.VISIBLE);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                }

                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                open.dismiss();
                            }
                        })
                        .create();
                open = builder.show();
                Button positive = open.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = open.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }
            }
        });

        condividi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_codividi, null);
                Spinner spinner = dialogView.findViewById(R.id.spinner_condividi);
                EditText diametro_minimo = dialogView.findViewById(R.id.diametro_minimo);
                EditText intervallo = dialogView.findViewById(R.id.intervallo_classi);
                File dir = new File(MainActivity.this.getFilesDir()+"/piedilista");
                File [] lista_campi_file = dir.listFiles();
                ArrayList<String> lista_campi = new ArrayList<>();
                for (File folder: lista_campi_file
                ) {
                    String nomeBase = folder.getName().split("\\.")[0].trim();
                    if (!nomeBase.isEmpty() && !lista_campi.contains(nomeBase)) {
                        lista_campi.add(folder.getName().split("\\.")[0]);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, lista_campi);
                spinner.setAdapter(adapter);
                builder.setView(dialogView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                share.dismiss();
                                progressBar.setVisibility(View.VISIBLE);
                                int d_min =0;
                                int interval = 0;
                                if (!diametro_minimo.getText().toString().isEmpty()){
                                    if (Integer.parseInt(diametro_minimo.getText().toString())>15){
                                        d_min = Integer.parseInt(diametro_minimo.getText().toString());
                                    }else{
                                        d_min = 15;
                                    }
                                }else{
                                    d_min = 15;
                                }

                                if (!intervallo.getText().toString().isEmpty()){
                                    if (Integer.parseInt(intervallo.getText().toString())>5){
                                        interval = Integer.parseInt(intervallo.getText().toString());
                                    }else{
                                        interval = 5;
                                    }
                                }else{
                                    interval = 5;
                                }

                                excelManager.creaFileExcel(MainActivity.this, spinner.getSelectedItem().toString(), d_min, interval);
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, spinner.getSelectedItem().toString());
                                ArrayList<Uri> uri_list = new ArrayList<>();
                                File file = new File(MainActivity.this.getFilesDir()+"/piedilista/"+spinner.getSelectedItem().toString()+".xlsx");
                                uri_list.add(FileProvider.getUriForFile(MainActivity.this, getPackageName()+".provider",file));


                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri_list);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                share.cancel();
                            }
                        })
                        .create();
                share = builder.show();
                Button positive = share.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = share.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }
            }


        });

        larice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir()+"/piedilista/"+nome_file+".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText("Larice");
                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_larice;
                                        JSONArray altezza_larice;
                                        if (json.has("diametro_larice")) {
                                            diametro_larice = (JSONArray) json.get("diametro_larice");
                                            altezza_larice = (JSONArray) json.get("altezza_larice");
                                        }else{
                                            diametro_larice = new JSONArray();
                                            altezza_larice = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_larice.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_larice.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_larice.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_larice.getSelectedItem().toString());
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Larice, diametro_larice, tariffa);
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_larice.setText(mc);
                                                    n_larice.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_larice", altezza_larice);
                                        json.put("diametro_larice", diametro_larice);
                                        json.put("tariffa_l", tariffa_larice.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_larice = new JSONArray();
                                    JSONArray altezza_larice = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_larice.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_larice.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_larice.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_larice.getSelectedItem().toString());
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Larice, diametro_larice, tariffa);
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_larice.setText(mc);
                                                n_larice.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_larice", altezza_larice);
                                        json.put("diametro_larice", diametro_larice);
                                        json.put("tariffa_l", tariffa_larice.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }


            }
        });

        abete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText("Abete rosso");

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_abete;
                                        JSONArray altezza_abete;
                                        if (json.has("diametro_abete")) {
                                            diametro_abete = (JSONArray) json.get("diametro_abete");
                                            altezza_abete = (JSONArray) json.get("altezza_abete");
                                        }else{
                                            diametro_abete = new JSONArray();
                                            altezza_abete = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_abete.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_abete.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_abete.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_abete.getSelectedItem().toString());
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Abeter, diametro_abete, tariffa);
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_abete.setText(mc);
                                                    n_abete.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_abete", altezza_abete);
                                        json.put("diametro_abete", diametro_abete);
                                        json.put("tariffa_a", tariffa_abete.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_abete= new JSONArray();
                                    JSONArray altezza_abete = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_abete.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_abete.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_abete.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_abete.getSelectedItem().toString());
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Abeter, diametro_abete, tariffa);
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_abete.setText(mc);
                                                n_abete.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_abete", altezza_abete);
                                        json.put("diametro_abete", diametro_abete);
                                        json.put("tariffa_a", tariffa_abete.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });

        faggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText("Faggio");

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_faggio;
                                        JSONArray altezza_faggio;
                                        if (json.has("diametro_faggio")) {
                                            diametro_faggio = (JSONArray) json.get("diametro_faggio");
                                            altezza_faggio = (JSONArray) json.get("altezza_faggio");
                                        }else{
                                            diametro_faggio = new JSONArray();
                                            altezza_faggio = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_faggio.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_faggio.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_faggio.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_faggio.getSelectedItem().toString())-2;
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Faggio, diametro_faggio, tariffa);
                                                    Log.i("valori faggio n", String.valueOf(mc_n[1]));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_faggio.setText(mc);
                                                    n_faggio.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_faggio", altezza_faggio);
                                        json.put("diametro_faggio", diametro_faggio);
                                        json.put("tariffa_f", tariffa_faggio.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_faggio= new JSONArray();
                                    JSONArray altezza_faggio = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_faggio.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_faggio.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_faggio.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_faggio.getSelectedItem().toString())-2;
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Faggio, diametro_faggio, tariffa);
                                                Log.i("valori faggio n", String.valueOf(mc_n[1]));
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_faggio.setText(mc);
                                                n_faggio.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_faggio", altezza_faggio);
                                        json.put("diametro_faggio", diametro_faggio);
                                        json.put("tariffa_f", tariffa_faggio.getSelectedItem().toString());
                                        Log.i("tariffa", tariffa_faggio.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });

        abeteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText(R.string.checkabeteb);

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_abeteb;
                                        JSONArray altezza_abeteb;
                                        if (json.has("diametro_abeteb")) {
                                            diametro_abeteb = (JSONArray) json.get("diametro_abeteb");
                                            altezza_abeteb = (JSONArray) json.get("altezza_abeteb");
                                        }else{
                                            diametro_abeteb = new JSONArray();
                                            altezza_abeteb = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_abeteb.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_abeteb.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_abeteb.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_abeteb.getSelectedItem().toString());
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Abeteb, diametro_abeteb, tariffa);
                                                    Log.i("valori abeteb n", String.valueOf(mc_n[1]));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_abeteb.setText(mc);
                                                    n_abeteb.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_abeteb", altezza_abeteb);
                                        json.put("diametro_abeteb", diametro_abeteb);
                                        json.put("tariffa_ab", tariffa_abeteb.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_abeteb= new JSONArray();
                                    JSONArray altezza_abeteb = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_abeteb.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_abeteb.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_abeteb.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_abeteb.getSelectedItem().toString());
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Abeteb, diametro_abeteb, tariffa);
                                                Log.i("valori faggio n", String.valueOf(mc_n[1]));
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_abeteb.setText(mc);
                                                n_abeteb.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_abeteb", altezza_abeteb);
                                        json.put("diametro_abeteb", diametro_abeteb);
                                        json.put("tariffa_ab", tariffa_abeteb.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });

        pinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText(R.string.checkpinos);

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_pinos;
                                        JSONArray altezza_pinos;
                                        if (json.has("diametro_pinos")) {
                                            diametro_pinos = (JSONArray) json.get("diametro_pinos");
                                            altezza_pinos = (JSONArray) json.get("altezza_pinos");
                                        }else{
                                            diametro_pinos = new JSONArray();
                                            altezza_pinos = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_pinos.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_pinos.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_pinos.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_pinos.getSelectedItem().toString());
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinos, diametro_pinos, tariffa);
                                                    Log.i("valori pinos n", String.valueOf(mc_n[1]));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinos.setText(mc);
                                                    n_pinos.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_pinos", altezza_pinos);
                                        json.put("diametro_pinos", diametro_pinos);
                                        json.put("tariffa_ps", tariffa_pinos.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_pinos= new JSONArray();
                                    JSONArray altezza_pinos = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_pinos.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_pinos.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_pinos.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_pinos.getSelectedItem().toString());
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Pinos, diametro_pinos, tariffa);
                                                Log.i("valori pinos n", String.valueOf(mc_n[1]));
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_pinos.setText(mc);
                                                n_pinos.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_pinos", altezza_pinos);
                                        json.put("diametro_pinos", diametro_pinos);
                                        json.put("tariffa_ps", tariffa_pinos.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });

        pinoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText(R.string.checkpinoc);

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_pinoc;
                                        JSONArray altezza_pinoc;
                                        if (json.has("diametro_pinoc")) {
                                            diametro_pinoc = (JSONArray) json.get("diametro_pinoc");
                                            altezza_pinoc = (JSONArray) json.get("altezza_pinoc");
                                        }else{
                                            diametro_pinoc = new JSONArray();
                                            altezza_pinoc = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_pinoc.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_pinoc.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_pinoc.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_pinoc.getSelectedItem().toString())-4;
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinoc, diametro_pinoc, tariffa);
                                                    Log.i("valori pinoc n", String.valueOf(mc_n[1]));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinoc.setText(mc);
                                                    n_pinoc.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_pinoc", altezza_pinoc);
                                        json.put("diametro_pinoc", diametro_pinoc);
                                        json.put("tariffa_pc", tariffa_pinoc.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_pinoc= new JSONArray();
                                    JSONArray altezza_pinoc = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_pinoc.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_pinoc.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_pinoc.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_pinoc.getSelectedItem().toString())-4;
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Pinoc, diametro_pinoc, tariffa);
                                                Log.i("valori pinoc n", String.valueOf(mc_n[1]));
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_pinoc.setText(mc);
                                                n_pinoc.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_pinoc", altezza_pinoc);
                                        json.put("diametro_pinoc", diametro_pinoc);
                                        json.put("tariffa_pc", tariffa_pinoc.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });

        pinon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_file = stimaVeloce_pref.getStringPref(PreferencesHelper.PREF.nome_file, "");
                File json_file = new File(getFilesDir() + "/piedilista/" + nome_file + ".json");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_frame_mc_n, null);
                diametro = dialogView.findViewById(R.id.diametro_pianta);
                altezza = dialogView.findViewById(R.id.altezza_pianta);
                TextView titolo = dialogView.findViewById(R.id.title_alert);
                titolo.setText(R.string.checkpinon);

                // 3. Costruisci l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(json_file.exists()){
                                    try {
                                        JSONObject json = readFromFile(MainActivity.this, nome_file);
                                        JSONArray diametro_pinon;
                                        JSONArray altezza_pinon;
                                        if (json.has("diametro_pinon")) {
                                            diametro_pinon = (JSONArray) json.get("diametro_pinon");
                                            altezza_pinon = (JSONArray) json.get("altezza_pinon");
                                        }else{
                                            diametro_pinon = new JSONArray();
                                            altezza_pinon = new JSONArray();
                                        }
                                        if(!diametro.getText().toString().isEmpty()) {
                                            try {
                                                diametro_pinon.put(Integer.valueOf(diametro.getText().toString()));
                                                if (!altezza.getText().toString().isEmpty()) {
                                                    altezza_pinon.put(Integer.valueOf(altezza.getText().toString()));
                                                } else {
                                                    altezza_pinon.put(0);
                                                }
                                                tariffa = mappa_tariffe.get(tariffa_pinon.getSelectedItem().toString());
                                                if (tariffa != 0) {
                                                    float[] mc_n = compute.compute_mc_n(specie.Pinon, diametro_pinon, tariffa);
                                                    Log.i("valori pinon n", String.valueOf(mc_n[1]));
                                                    String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                    String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                    mc_pinon.setText(mc);
                                                    n_pinon.setText(n);
                                                }
                                            } catch (NumberFormatException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        json.put("altezza_pinon", altezza_pinon);
                                        json.put("diametro_pinon", diametro_pinon);
                                        json.put("tariffa_pn", tariffa_pinon.getSelectedItem().toString());

                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }else{
                                    JSONArray diametro_pinon= new JSONArray();
                                    JSONArray altezza_pinon = new JSONArray();
                                    JSONObject json = new JSONObject();
                                    if(!diametro.getText().toString().isEmpty()){
                                        try {
                                            diametro_pinon.put(Integer.valueOf(diametro.getText().toString()));
                                            if (!altezza.getText().toString().isEmpty()){
                                                altezza_pinon.put(Integer.valueOf(altezza.getText().toString()));
                                            }else{
                                                altezza_pinon.put(0);
                                            }
                                            tariffa = mappa_tariffe.get(tariffa_pinoc.getSelectedItem().toString());
                                            if (tariffa != 0) {
                                                float[] mc_n = compute.compute_mc_n(specie.Pinon, diametro_pinon, tariffa);
                                                Log.i("valori pinon n", String.valueOf(mc_n[1]));
                                                String mc = String.format(Locale.US, "%.2f", mc_n[0]);
                                                String n = String.format(Locale.US, "%d", (int) mc_n[1]);
                                                mc_pinon.setText(mc);
                                                n_pinon.setText(n);
                                            }
                                        } catch (NumberFormatException | JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        json.put("altezza_pinon", altezza_pinon);
                                        json.put("diametro_pinon", diametro_pinon);
                                        json.put("tariffa_pn", tariffa_pinon.getSelectedItem().toString());
                                        writeToFile(json, MainActivity.this, nome_file);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }



                                }
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // Chiude il dialog senza fare nulla
                            }
                        });

                // 4. Mostra il dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (positive!=null){
                    LinearLayout.LayoutParams layoutParams_positive_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_positive_button.setMargins(5, 10, 10, 10);
                    layoutParams_positive_button.weight = 1.0f;
                    positive.setLayoutParams(layoutParams_positive_button);
                    positive.setPadding(2,2,2,2);
                    positive.setTextSize(18f);
                    positive.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    positive.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    positive.setAllCaps(false);
                }
                if (negative!=null){
                    LinearLayout.LayoutParams layoutParams_negative_button = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams_negative_button.setMargins(5, 10, 10, 10);
                    layoutParams_negative_button.weight = 1.0f;
                    negative.setLayoutParams(layoutParams_negative_button);
                    negative.setPadding(2,2,2,2);
                    negative.setTextSize(18f);
                    negative.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    negative.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
                    negative.setAllCaps(false);
                }

            }
        });


    }

    private void writeToFile(@NonNull JSONObject jsonObject, Context context, String nome_file) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(context.getFilesDir()+"/piedilista/"+nome_file+".json"));
            outputStreamWriter.write(jsonObject.toString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private JSONObject readFromFile(Context context, String nome_file) throws JSONException {


        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = new FileInputStream(context.getFilesDir()+"/piedilista/"+nome_file+".json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";


                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    if(!receiveString.isEmpty()) {
                        stringBuilder.append(receiveString).append("\n");
                    }
                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        JSONObject json_ = new JSONObject(stringBuilder.toString());
        Log.i("json", json_.toString());
        return json_;
    }
    private void collegaGruppoATotale(List<TextView> gruppo, TextView tvTotale, boolean intero) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                aggiornaTotale(gruppo, tvTotale, intero);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        };

        for (TextView tv : gruppo) {
            tv.addTextChangedListener(watcher);
        }
    }
    private void aggiornaTotale(List<TextView> lista, TextView tvTotale, boolean intero) {
        double somma = 0.0;

        for (TextView tv : lista) {
            // Considera solo le TextView visibili
            if (tv.getVisibility() == View.VISIBLE) {
                String testo = tv.getText().toString().trim();
                if (!testo.isEmpty()) {
                    try {
                        somma += Double.parseDouble(testo);
                    } catch (NumberFormatException e) {
                        // Ignora se il testo non è un numero valido
                    }
                }
            }
        }
        String somma_string;
        if (intero) {
            somma_string = String.format(Locale.US, "%d", (int) somma);
        }else{
            somma_string = String.format(Locale.US, "%.2f", somma);
        }
        tvTotale.setText(somma_string);
    }
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume");
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    private boolean hasPermissions(String[] permissions) {
        Log.i("MainActivity", "hasPermissions");
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSIONS", "Permission is not granted: " + permission);
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: " + permission);
            }
            return true;
        }
        return false;
    }

}
