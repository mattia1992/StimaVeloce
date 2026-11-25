package com.example.stimaveloce;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.IntRange;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpHelper {
    public static String SERVER = "https://stima-legno.uni-box.it/";
    public static String ENDPOINT = "appexpiration26/";
    public Context application_context;
    public HttpHelper(Context context){
        application_context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) application_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int typeConnection = getConnectionType();
        boolean isconnected = false;
        if(typeConnection > 0){
            isconnected = true;
        }
        return isconnected;
    }
    public Integer day_to_exiperation(){
        int days = 0;
        RequestExpiration request_expiration = new RequestExpiration();
        Thread thread = new Thread(request_expiration);
        thread.start();

        try {
            thread.join();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally{
            days = (int) ((request_expiration.getExpired_date()- request_expiration.getDate())/(60*60*24));
            return days;
        }

    }
    public boolean isLicenceActive() {

        RequestExpiration request_expiration = new RequestExpiration();
        Thread thread = new Thread(request_expiration);
        thread.start();

        try {
            thread.join();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally{
            if (request_expiration.getDate() < request_expiration.getExpired_date()) {

                return true;
            }else{

                return false;
            }
        }
    }

    public static class RequestExpiration implements Runnable
    {
        private volatile  Integer server_date;
        private volatile  Integer expired_date;
        @Override
        public void run(){
            HttpsURLConnection connection = null;
            try {

                URL url = new URL(SERVER);
                connection = (HttpsURLConnection) url.openConnection();

                int response_code = connection.getResponseCode();
                if (response_code == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String jsonString = response.toString();
                    Log.i("stima_veloce", jsonString);
                    JSONObject json_ = new JSONObject(jsonString);
                    server_date = (int) json_.get("server_timestamp");
                    expired_date = (int) json_.get("deadline_timestamp");
                    Log.i("stima_veloce", String.valueOf(server_date));
                    Log.i("response_Stima_legno", String.valueOf(expired_date>server_date));

                }else{
                    Log.i("response_pomAI", "failed");
                }

            }catch (MalformedURLException e){
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }
        public Integer getDate(){
            return server_date;
        }
        public Integer getExpired_date(){
            return expired_date;
        }
    }

    @IntRange(from = 0, to = 3)
    public int getConnectionType() {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn
        ConnectivityManager cm = (ConnectivityManager) application_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        result = 3;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = 1;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_VPN) {
                        result = 3;
                    }
                }
            }
        }
        return result;
    }

}
