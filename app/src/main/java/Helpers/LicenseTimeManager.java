package Helpers;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LicenseTimeManager {

    private static final String PREFS_NAME = "stima_veloce_sec";
    private static final String KEY_SERVER_TIME = "last_server_time";
    private static final String KEY_ELAPSED_TIME = "last_elapsed_time";
    private static final String KEY_WALL_TIME = "last_wall_time";
    private static final String KEY_EXPIRATION = "license_expiration";

    // Tolleranza massima di sfasamento orologio (es. 5 minuti)
    private static final long CLOCK_TOLERANCE_MS = 5 * 60 * 1000;
    private static final long MS_IN_DAY = 24L * 60L * 60L * 1000L;
    public interface TimeCheckCallback {
        void onLicenseValid(int giorniRimanenti);
        void onLicenseExpired();
        void onInternetRequired(String reason);
    }

    /**
     * Chiama questo metodo passando la scadenza letta da Firestore (o 0 se sei offline e non hai letto Firestore)
     */
    public static void verificaAccesso(Context context, long firestoreExpirationTime, TimeCheckCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            long serverTime = getServerTimeFromGoogle();

            mainHandler.post(() -> {
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                if (serverTime > 0) {
                    // C'È INTERNET: Abbiamo l'ora reale dal server
                    long scadenzaDaUsare = firestoreExpirationTime > 0 ? firestoreExpirationTime : prefs.getLong(KEY_EXPIRATION, 0);

                    if (serverTime < scadenzaDaUsare) {
                        // Salviamo l'ancora sicura per l'uso offline
                        prefs.edit()
                                .putLong(KEY_SERVER_TIME, serverTime)
                                .putLong(KEY_ELAPSED_TIME, SystemClock.elapsedRealtime())
                                .putLong(KEY_WALL_TIME, System.currentTimeMillis())
                                .putLong(KEY_EXPIRATION, scadenzaDaUsare)
                                .apply();
                        long diffMs = Math.max(0L, scadenzaDaUsare - serverTime);
                        int giorniRimanenti = (int) (diffMs / MS_IN_DAY);
                        callback.onLicenseValid(giorniRimanenti);
                    } else {
                        callback.onLicenseExpired();
                    }

                } else {
                    // NON C'È INTERNET: Controlliamo se ha riavviato o cambiato l'ora
                    long savedServerTime = prefs.getLong(KEY_SERVER_TIME, 0);
                    long savedElapsed = prefs.getLong(KEY_ELAPSED_TIME, 0);
                    long savedWallTime = prefs.getLong(KEY_WALL_TIME, 0);
                    long savedExpiration = prefs.getLong(KEY_EXPIRATION, 0);

                    if (savedServerTime == 0 || savedExpiration == 0) {
                        callback.onInternetRequired("Prima verifica necessaria. Connettiti a internet.");
                        return;
                    }

                    long currentElapsed = SystemClock.elapsedRealtime();
                    long currentWall = System.currentTimeMillis();

                    // 1. Controllo RIAVVIO
                    if (currentElapsed < savedElapsed) {
                        callback.onInternetRequired("Dispositivo riavviato. Connettiti a internet per verificare la licenza.");
                        return;
                    }

                    // 2. Controllo SPOSTAMENTO ORA
                    long deltaElapsed = currentElapsed - savedElapsed;
                    long deltaWall = currentWall - savedWallTime;
                    if (Math.abs(deltaElapsed - deltaWall) > CLOCK_TOLERANCE_MS) {
                        callback.onInternetRequired("Rilevata modifica dell'orario. Connettiti a internet per verificare la licenza.");
                        return;
                    }

                    // 3. Nessuna manomissione: calcoliamo l'ora reale stimata offline
                    long estimatedRealTime = savedServerTime + deltaElapsed;
                    if (estimatedRealTime < savedExpiration) {
                        long diffMs = Math.max(0L, savedExpiration - estimatedRealTime);
                        int giorniRimanenti = (int) (diffMs / MS_IN_DAY);
                        callback.onLicenseValid(giorniRimanenti);
                    } else {
                        callback.onLicenseExpired();
                    }
                }
            });
        });
    }

    /**
     * Fa una richiesta HEAD leggerissima a Google per leggere l'ora esatta dall'header HTTP "Date"
     */
    private static long getServerTimeFromGoogle() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            long date = conn.getDate(); // Ritorna il timestamp UTC in millisecondi
            conn.disconnect();
            return date;
        } catch (Exception e) {
            return -1; // Nessuna connessione o timeout
        }
    }


}