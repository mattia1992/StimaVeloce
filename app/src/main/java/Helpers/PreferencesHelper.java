package Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private Context application_context;
    public PreferencesHelper(Context context){
        application_context = context;
    }

    public enum PREF {nome_file}

    public SharedPreferences getSharedPreferencePomAI(){
        return application_context.getApplicationContext().getSharedPreferences("StimaVeloce_pref", Context.MODE_PRIVATE);
    }
    public boolean getBoolPref(PREF key, boolean defVal){
        return this.getSharedPreferencePomAI().getBoolean(key.name(), false);
    }
    public void setBoolPref(PREF key, boolean value){
        this.getSharedPreferencePomAI().edit().putBoolean(key.name(), value).commit();
    }
    public String getStringPref(PREF key, String defVal){
        return this.getSharedPreferencePomAI().getString(key.name(), defVal);
    }
    public void setStringPref(PREF key, String value){
        this.getSharedPreferencePomAI().edit().putString(key.name(), value).commit();
    }
    public int getIntPref(PREF key, int defVal){
        return this.getSharedPreferencePomAI().getInt(key.name(), defVal);
    }
    public void setIntPref(PREF key, int value){
        this.getSharedPreferencePomAI().edit().putInt(key.name(), value).commit();
    }
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        this.getSharedPreferencePomAI().registerOnSharedPreferenceChangeListener(listener);
    }
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        this.getSharedPreferencePomAI().unregisterOnSharedPreferenceChangeListener(listener);
    }
}

