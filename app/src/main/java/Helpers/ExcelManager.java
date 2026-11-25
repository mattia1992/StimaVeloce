package Helpers;
import android.content.Context;
import android.util.Log;

import com.example.stimaveloce.MainActivity;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import Helpers.Compute.specie;

public class ExcelManager {
    private static Context applicationcontext;
    private static final String TAG = "ExcelManager";
    private static HashMap<String, Integer> mappa_tariffe = new LinkedHashMap<>();

    /**
     * Crea un file Excel con un foglio e alcune righe/colonne di esempio
     */
    public File creaFileExcel(Context context, String nomeFile, int diametro_minimo, int intervallo) {
        applicationcontext = context;
        mappa_tariffe.put("I", 1);
        mappa_tariffe.put("II", 2);
        mappa_tariffe.put("III", 3);
        mappa_tariffe.put("IV", 4);
        mappa_tariffe.put("V", 5);
        mappa_tariffe.put("VI", 6);
        mappa_tariffe.put("VII", 7);
        mappa_tariffe.put("VIII", 8);
        mappa_tariffe.put("IX", 9);
        // 1. Crea la cartella di destinazione (es. nella memoria interna dell'app o Downloads)
        File fileExcel = new File(context.getFilesDir() + "/piedilista/", nomeFile + ".xlsx");
        Compute compute = new Compute(context);
        // 2. Crea il Workbook (il file Excel)
        Workbook workbook = new XSSFWorkbook();
        File json_file = new File(context.getFilesDir()+"/piedilista/"+nomeFile+".json");

        DataFormat format = workbook.createDataFormat();

        // --- STILE 1: Bordi sottili, centrato, testo normale ---
        CellStyle stileNormale = workbook.createCellStyle();
        stileNormale.setBorderTop(BorderStyle.THIN);
        stileNormale.setBorderBottom(BorderStyle.THIN);
        stileNormale.setBorderLeft(BorderStyle.THIN);
        stileNormale.setBorderRight(BorderStyle.THIN);
        stileNormale.setAlignment(HorizontalAlignment.CENTER);
        stileNormale.setVerticalAlignment(VerticalAlignment.CENTER);

        // --- STILE 2: Identico al primo, ma con testo in GRASSETTO (es. per intestazioni o totali) ---
        // Creiamo prima un font in grassetto
        Font fontGrassetto = workbook.createFont();
        fontGrassetto.setBold(true);

        // Creiamo il secondo stile
        CellStyle stileGrassetto = workbook.createCellStyle();
        stileGrassetto.setBorderTop(BorderStyle.THIN);
        stileGrassetto.setBorderBottom(BorderStyle.THIN);
        stileGrassetto.setBorderLeft(BorderStyle.THIN);
        stileGrassetto.setBorderRight(BorderStyle.THIN);
        stileGrassetto.setAlignment(HorizontalAlignment.CENTER);
        stileGrassetto.setVerticalAlignment(VerticalAlignment.CENTER);
        stileGrassetto.setFont(fontGrassetto); // Applichiamo il font in grassetto

        // --- STILE 3: Bordi sottili, centrato, testo normale, un decimale ---
        CellStyle stileNormaleUndecimale = workbook.createCellStyle();
        stileNormaleUndecimale.setBorderTop(BorderStyle.THIN);
        stileNormaleUndecimale.setBorderBottom(BorderStyle.THIN);
        stileNormaleUndecimale.setBorderLeft(BorderStyle.THIN);
        stileNormaleUndecimale.setBorderRight(BorderStyle.THIN);
        stileNormaleUndecimale.setAlignment(HorizontalAlignment.CENTER);
        stileNormaleUndecimale.setVerticalAlignment(VerticalAlignment.CENTER);
        stileNormaleUndecimale.setDataFormat(format.getFormat("0.0"));

        // --- STILE 4: Identico al primo, ma con testo in GRASSETTO un decimale (es. per intestazioni o totali) ---

        // Creiamo il secondo stile
        CellStyle stileGrassettoUnDecimale = workbook.createCellStyle();
        stileGrassettoUnDecimale.setBorderTop(BorderStyle.THIN);
        stileGrassettoUnDecimale.setBorderBottom(BorderStyle.THIN);
        stileGrassettoUnDecimale.setBorderLeft(BorderStyle.THIN);
        stileGrassettoUnDecimale.setBorderRight(BorderStyle.THIN);
        stileGrassettoUnDecimale.setAlignment(HorizontalAlignment.CENTER);
        stileGrassettoUnDecimale.setVerticalAlignment(VerticalAlignment.CENTER);
        stileGrassettoUnDecimale.setFont(fontGrassetto); // Applichiamo il font in grassetto
        stileGrassettoUnDecimale.setDataFormat(format.getFormat("0.0"));

        CellStyle stileCentrato = workbook.createCellStyle();
        stileCentrato.setAlignment(HorizontalAlignment.CENTER);     // Centrato orizzontalmente
        stileCentrato.setVerticalAlignment(VerticalAlignment.CENTER);
        if(json_file.exists()) {
            try {
                JSONObject json = readFromFile(context, nomeFile);

                // 3. Crea Foglio Larice
                if (json.has("diametro_larice")) {
                    JSONArray diametro_larice = (JSONArray) json.get("diametro_larice");
                    JSONArray h_larice = (JSONArray) json.get("altezza_larice");
                    String tariffa = (String) json.get("tariffa_l");
                    int tariffa_l = mappa_tariffe.get(tariffa);
                    Sheet sheet_larice = workbook.createSheet("Larice");
                    Row header_tariffa = sheet_larice.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_larice.addMergedRegion(region);
                    Row headerRow_l = sheet_larice.createRow(1);
                    Cell cell0_l = headerRow_l.createCell(0);
                    cell0_l.setCellValue("Diametro (cm)");
                    Cell cell1_l = headerRow_l.createCell(1);
                    cell1_l.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_l.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Larice, diametro_larice, tariffa_l);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_larice.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_larice.getInt(i));
                    }
                    Row rowheader_summary = sheet_larice.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_larice.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_larice.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_larice.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_larice.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_larice.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_larice.setColumnWidth(0, 12*256);
                    sheet_larice.setColumnWidth(1, 12*256);
                    sheet_larice.setColumnWidth(2, 12*256);
                    sheet_larice.setColumnWidth(4, 20*256);
                    sheet_larice.setColumnWidth(5, 10*256);
                    sheet_larice.setColumnWidth(6, 27*256);
                    sheet_larice.setColumnWidth(7, 19*256);

                }


                if (json.has("diametro_abete")) {
                    JSONArray diametro_abete = (JSONArray) json.get("diametro_abete");
                    JSONArray h_abete = (JSONArray) json.get("altezza_abete");
                    String tariffa = (String) json.get("tariffa_a");
                    int tariffa_a = mappa_tariffe.get(tariffa);
                    // 4. Crea Foglio Abete
                    Sheet sheet_abeterosso = workbook.createSheet("Abete rosso");
                    Row header_tariffa = sheet_abeterosso.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_abeterosso.addMergedRegion(region);
                    Row headerRow_a = sheet_abeterosso.createRow(1);
                    Cell cell0_a = headerRow_a.createCell(0);
                    cell0_a.setCellValue("Diametro (cm)");
                    Cell cell1_a = headerRow_a.createCell(1);
                    cell1_a.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_a.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Abeter, diametro_abete, tariffa_a);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_abeterosso.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_abete.getInt(i));
                    }
                    Row rowheader_summary = sheet_abeterosso.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_abeterosso.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_abeterosso.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_abeterosso.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_abeterosso.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_abeterosso.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_abeterosso.setColumnWidth(0, 12*256);
                    sheet_abeterosso.setColumnWidth(1, 12*256);
                    sheet_abeterosso.setColumnWidth(2, 12*256);
                    sheet_abeterosso.setColumnWidth(4, 20*256);
                    sheet_abeterosso.setColumnWidth(5, 10*256);
                    sheet_abeterosso.setColumnWidth(6, 27*256);
                    sheet_abeterosso.setColumnWidth(7, 19*256);

                }


                if (json.has("diametro_faggio")) {
                    JSONArray diametro_faggio = (JSONArray) json.get("diametro_faggio");
                    JSONArray h_faggio = (JSONArray) json.get("altezza_faggio");
                    String tariffa = (String) json.get("tariffa_f");
                    int tariffa_f = mappa_tariffe.get(tariffa)-2;
                    // 5. Crea Foglio Faggio
                    Sheet sheet_faggio = workbook.createSheet("Faggio");
                    Row header_tariffa = sheet_faggio.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_faggio.addMergedRegion(region);
                    Row headerRow_f = sheet_faggio.createRow(1);
                    Cell cell0_f = headerRow_f.createCell(0);
                    cell0_f.setCellValue("Diametro (cm)");
                    Cell cell1_f = headerRow_f.createCell(1);
                    cell1_f.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_f.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Faggio, diametro_faggio, tariffa_f);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_faggio.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_faggio.getInt(i));
                    }
                    Row rowheader_summary = sheet_faggio.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_faggio.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_faggio.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_faggio.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_faggio.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_faggio.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_faggio.setColumnWidth(0, 12*256);
                    sheet_faggio.setColumnWidth(1, 12*256);
                    sheet_faggio.setColumnWidth(2, 12*256);
                    sheet_faggio.setColumnWidth(4, 20*256);
                    sheet_faggio.setColumnWidth(5, 10*256);
                    sheet_faggio.setColumnWidth(6, 27*256);
                    sheet_faggio.setColumnWidth(7, 19*256);

                }

                if (json.has("diametro_abeteb")) {
                    JSONArray diametro_abeteb = (JSONArray) json.get("diametro_abeteb");
                    JSONArray h_abeteb = (JSONArray) json.get("altezza_abeteb");
                    String tariffa = (String) json.get("tariffa_ab");
                    int tariffa_ab = mappa_tariffe.get(tariffa);
                    // 4. Crea Foglio Abete
                    Sheet sheet_abetebianco = workbook.createSheet("Abete bianco");
                    Row header_tariffa = sheet_abetebianco.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_abetebianco.addMergedRegion(region);
                    Row headerRow_ab = sheet_abetebianco.createRow(1);
                    Cell cell0_a = headerRow_ab.createCell(0);
                    cell0_a.setCellValue("Diametro (cm)");
                    Cell cell1_a = headerRow_ab.createCell(1);
                    cell1_a.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_ab.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Abeteb, diametro_abeteb, tariffa_ab);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_abetebianco.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_abeteb.getInt(i));
                    }
                    Row rowheader_summary = sheet_abetebianco.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_abetebianco.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_abetebianco.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_abetebianco.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_abetebianco.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_abetebianco.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_abetebianco.setColumnWidth(0, 12*256);
                    sheet_abetebianco.setColumnWidth(1, 12*256);
                    sheet_abetebianco.setColumnWidth(2, 12*256);
                    sheet_abetebianco.setColumnWidth(4, 20*256);
                    sheet_abetebianco.setColumnWidth(5, 10*256);
                    sheet_abetebianco.setColumnWidth(6, 27*256);
                    sheet_abetebianco.setColumnWidth(7, 19*256);

                }

                if (json.has("diametro_pinos")) {
                    JSONArray diametro_pinos = (JSONArray) json.get("diametro_pinos");
                    JSONArray h_pinos = (JSONArray) json.get("altezza_pinos");
                    String tariffa = (String) json.get("tariffa_ps");
                    int tariffa_ps = mappa_tariffe.get(tariffa);
                    // 4. Crea Foglio pino silvestre
                    Sheet sheet_pinosilvestre = workbook.createSheet("Pino silvestre");
                    Row header_tariffa = sheet_pinosilvestre.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_pinosilvestre.addMergedRegion(region);
                    Row headerRow_ps = sheet_pinosilvestre.createRow(1);
                    Cell cell0_a = headerRow_ps.createCell(0);
                    cell0_a.setCellValue("Diametro (cm)");
                    Cell cell1_a = headerRow_ps.createCell(1);
                    cell1_a.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_ps.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Pinos, diametro_pinos, tariffa_ps);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_pinosilvestre.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_pinos.getInt(i));
                    }
                    Row rowheader_summary = sheet_pinosilvestre.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_pinosilvestre.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_pinosilvestre.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_pinosilvestre.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_pinosilvestre.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_pinosilvestre.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_pinosilvestre.setColumnWidth(0, 12*256);
                    sheet_pinosilvestre.setColumnWidth(1, 12*256);
                    sheet_pinosilvestre.setColumnWidth(2, 12*256);
                    sheet_pinosilvestre.setColumnWidth(4, 20*256);
                    sheet_pinosilvestre.setColumnWidth(5, 10*256);
                    sheet_pinosilvestre.setColumnWidth(6, 27*256);
                    sheet_pinosilvestre.setColumnWidth(7, 19*256);

                }

                if (json.has("diametro_pinoc")) {
                    JSONArray diametro_pinoc = (JSONArray) json.get("diametro_pinoc");
                    JSONArray h_pinoc = (JSONArray) json.get("altezza_pinoc");
                    String tariffa = (String) json.get("tariffa_pc");
                    int tariffa_pc = mappa_tariffe.get(tariffa)-4;
                    // 4. Crea Foglio pino silvestre
                    Sheet sheet_pinocembro = workbook.createSheet("Pino cembro");
                    Row header_tariffa = sheet_pinocembro.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_pinocembro.addMergedRegion(region);
                    Row headerRow_c = sheet_pinocembro.createRow(1);
                    Cell cell0_a = headerRow_c.createCell(0);
                    cell0_a.setCellValue("Diametro (cm)");
                    Cell cell1_a = headerRow_c.createCell(1);
                    cell1_a.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_c.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Pinoc, diametro_pinoc, tariffa_pc);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_pinocembro.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_pinoc.getInt(i));
                    }
                    Row rowheader_summary = sheet_pinocembro.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_pinocembro.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_pinocembro.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_pinocembro.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_pinocembro.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_pinocembro.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_pinocembro.setColumnWidth(0, 12*256);
                    sheet_pinocembro.setColumnWidth(1, 12*256);
                    sheet_pinocembro.setColumnWidth(2, 12*256);
                    sheet_pinocembro.setColumnWidth(4, 20*256);
                    sheet_pinocembro.setColumnWidth(5, 10*256);
                    sheet_pinocembro.setColumnWidth(6, 27*256);
                    sheet_pinocembro.setColumnWidth(7, 19*256);

                }

                if (json.has("diametro_pinon")) {
                    JSONArray diametro_pinon = (JSONArray) json.get("diametro_pinon");
                    JSONArray h_pinon = (JSONArray) json.get("altezza_pinon");
                    String tariffa = (String) json.get("tariffa_pn");
                    int tariffa_pn = mappa_tariffe.get(tariffa);
                    // 4. Crea Foglio pino nero
                    Sheet sheet_pinonero = workbook.createSheet("Pino nero");
                    Row header_tariffa = sheet_pinonero.createRow(0);
                    Cell tariffa_cell = header_tariffa.createCell(0);
                    tariffa_cell.setCellValue("Tariffa "+tariffa);
                    tariffa_cell.setCellStyle(stileCentrato);
                    CellRangeAddress region = new CellRangeAddress(
                            0, // Riga di inizio (es. riga 0)
                            0, // Riga di fine (es. riga 0, quindi stessa riga)
                            0, // Colonna di inizio (es. colonna A / indice 0)
                            1  // Colonna di fine (es. colonna C / indice 2)
                    );
                    sheet_pinonero.addMergedRegion(region);
                    Row headerRow_pn = sheet_pinonero.createRow(1);
                    Cell cell0_a = headerRow_pn.createCell(0);
                    cell0_a.setCellValue("Diametro (cm)");
                    Cell cell1_a = headerRow_pn.createCell(1);
                    cell1_a.setCellValue("Volume (mc)");
                    Cell cell2_l = headerRow_pn.createCell(2);
                    cell2_l.setCellValue("Altezza (m)");
                    float [][] mc_d = compute.compute_mc_single_plant(specie.Pinon, diametro_pinon, tariffa_pn);
                    int rowNum = 2;
                    int length = mc_d[0].length;
                    for (int i = 0; i<length; i++) {
                        Row row = sheet_pinonero.createRow(rowNum++);

                        row.createCell(0).setCellValue((float) mc_d[1][i]);
                        row.createCell(1).setCellValue((float) mc_d[0][i]);
                        row.createCell(2).setCellValue((int) h_pinon.getInt(i));
                    }
                    Row rowheader_summary = sheet_pinonero.getRow(2);
                    if (rowheader_summary==null){
                        rowheader_summary = sheet_pinonero.createRow(2);
                    }
                    Cell cell0 = rowheader_summary.createCell(4);
                    cell0.setCellValue("Classe Diametrica (cm)");
                    cell0.setCellStyle(stileGrassetto);
                    Cell cell1 = rowheader_summary.createCell(5);
                    cell1.setCellValue("N° Piante");
                    cell1.setCellStyle(stileGrassetto);
                    Cell cell2 = rowheader_summary.createCell(6);
                    cell2.setCellValue("Volume Unitario Medio (m³)");
                    cell2.setCellStyle(stileGrassetto);
                    Cell cell3 = rowheader_summary.createCell(7);
                    cell3.setCellValue("Volume Totale (m³)");
                    cell3.setCellStyle(stileGrassetto);
                    float[][] classi_matrice = compute.crea_classi_diametriche(diametro_minimo, intervallo, mc_d[0], mc_d[1]);
                    int righe_tabella = classi_matrice[0].length;
                    int rowNum_summary = 3;
                    int totale_piante = 0;
                    float totale_mc = 0f;
                    for (int i = 0; i<righe_tabella; i++) {
                        Row row = sheet_pinonero.getRow(rowNum_summary);
                        if (row==null){
                            row = sheet_pinonero.createRow(rowNum_summary);
                        }
                        Cell cella = row.createCell(4);
                        cella.setCellValue((int) classi_matrice[0][i]);
                        cella.setCellStyle(stileNormale);
                        Cell cellb = row.createCell(5);
                        cellb.setCellValue((int) classi_matrice[1][i]);
                        cellb.setCellStyle(stileNormale);
                        Cell cellc = row.createCell(6);
                        cellc.setCellValue((float) classi_matrice[2][i]);
                        cellc.setCellStyle(stileNormaleUndecimale);
                        Cell celld = row.createCell(7);
                        celld.setCellValue((float) classi_matrice[3][i]);
                        celld.setCellStyle(stileNormaleUndecimale);
                        totale_piante += (int) classi_matrice[1][i];
                        totale_mc += (float) classi_matrice[3][i];
                        rowNum_summary++;
                    }
                    Row row = sheet_pinonero.getRow(rowNum_summary);
                    if (row==null){
                        row = sheet_pinonero.createRow(rowNum_summary);
                    }
                    Cell cell4 =  row.createCell(4);
                    cell4.setCellValue("TOTALE");
                    cell4.setCellStyle(stileGrassetto);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(totale_piante);
                    cell5.setCellStyle(stileGrassetto);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue("-");
                    cell7.setCellStyle(stileGrassetto);
                    Cell cell6 = row.createCell(7);
                    cell6.setCellValue(totale_mc);
                    cell6.setCellStyle(stileGrassettoUnDecimale);

                    sheet_pinonero.setColumnWidth(0, 12*256);
                    sheet_pinonero.setColumnWidth(1, 12*256);
                    sheet_pinonero.setColumnWidth(2, 12*256);
                    sheet_pinonero.setColumnWidth(4, 20*256);
                    sheet_pinonero.setColumnWidth(5, 10*256);
                    sheet_pinonero.setColumnWidth(6, 27*256);
                    sheet_pinonero.setColumnWidth(7, 19*256);

                }

                // 6. Salva il file sul disco
                try (FileOutputStream fileOut = new FileOutputStream(fileExcel)) {
                    workbook.write(fileOut);
                    workbook.close();
                    Log.d(TAG, "File Excel salvato con successo in: " + fileExcel.getAbsolutePath());
                    return fileExcel;
                } catch (IOException e) {
                    Log.e(TAG, "Errore durante la scrittura del file Excel", e);
                    return null;
                }
            } catch (JSONException | NullPointerException e) {
                throw new RuntimeException(e);
            }



        }
        return null;
    }

    private static JSONObject readFromFile(Context context, String nome_file) throws JSONException {


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
}