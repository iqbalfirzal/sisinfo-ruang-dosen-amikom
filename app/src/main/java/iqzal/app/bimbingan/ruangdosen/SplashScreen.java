package iqzal.app.bimbingan.ruangdosen;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;


public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        networkChecking();
    }

    private void networkChecking(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Intent intent = new Intent(SplashScreen.this, MainActivityDosen.class);
            startActivity(intent);
            finish();

            //if (isOnline()) {
            //    checkLoggedIn();
            //} else {
            //    showNoDataReceivedNoticeDialog();
            //}
        } else {
            showNoConnectionNoticeDialog();
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 google.com");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    private void showNoDataReceivedNoticeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tidak dapat terhubung");
        builder.setMessage("Pastikan koneksi wifi atau data memiliki akses internet.");
        builder.setCancelable(false);
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAndRemoveTask();
            }
        });
        builder.show();
    }

    private void showNoConnectionNoticeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tidak ada koneksi internet");
        builder.setMessage("Aplikasi tidak akan bekerja dengan baik tanpa koneksi internet.");
        builder.setCancelable(false);
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAndRemoveTask();
            }
        });
        builder.show();
    }

    private void checkLoggedIn(){
        String iduser = new LoginPrefManager(this).getIdLoggedIn();
        String status = new LoginPrefManager(this).getStatusLoggedIn();
        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        globalVariable.setId(iduser);
        if (!new LoginPrefManager(this).isUserLogedOut()) {
            if (status.equals("dosen")){
                Intent intent = new Intent(SplashScreen.this, DashDosen.class);
                startActivity(intent);
                finish();
            }else if(status.equals("mhs")){
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashScreen.this, Choose.class);
                startActivity(intent);
                finish();
            }
        }else{
            Intent intent = new Intent(SplashScreen.this, Choose.class);
            startActivity(intent);
            finish();
        }
    }
}
