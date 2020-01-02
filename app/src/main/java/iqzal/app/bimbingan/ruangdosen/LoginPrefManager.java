package iqzal.app.bimbingan.ruangdosen;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPrefManager {
    Context context;

    LoginPrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String id, String status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IdUser", id);
        editor.putString("StatusUser", status);
        editor.commit();
    }

    public void clearData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }

    public String getIdLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("IdUser", "");
    }

    public String getStatusLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("StatusUser", "");
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isIdEmpty = sharedPreferences.getString("IdUser", "").isEmpty();
        boolean isStatusEmpty = sharedPreferences.getString("StatusUser", "").isEmpty();
        return isIdEmpty || isStatusEmpty;
    }
}
