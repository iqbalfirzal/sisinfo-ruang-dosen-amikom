package iqzal.app.bimbingan.ruangdosen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginMhs extends AppCompatActivity {
    ProgressBar progressBar;
    EditText nim,sandi;
    Button btLogin;
    private FirebaseAuth auth;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mahasiswa);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        progressBar = findViewById(R.id.progressBar);
        nim = findViewById(R.id.txtNim);
        sandi = findViewById(R.id.txtPassMhs);
        btLogin = findViewById(R.id.loginMhs);
        firebaseRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldusername = nim.getText().toString() ;
                final String username = oldusername.replace(".","");
                final String password = sandi.getText().toString() ;
                progressBar.setVisibility(View.VISIBLE);
                if(username.length() > 0 && password.length() > 0){
                    firebaseRef.child("mhs").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userEmail =  dataSnapshot.child("email").getValue(String.class);
                            String userFullName =  dataSnapshot.child("fullname").getValue(String.class);
                            if(userEmail != null){
                                saveLoginDetails(username, "mhs", userFullName);
                                globalVariable.setId(username);
                                performLogin(userEmail,password);
                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast notice = Toast.makeText(LoginMhs.this, "NIM tidak terdaftar!", Toast.LENGTH_LONG);
                                notice.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast notice = Toast.makeText(LoginMhs.this, "Koneksi ke database gagal.", Toast.LENGTH_LONG);
                            notice.show();
                            finish();
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast notice = Toast.makeText(LoginMhs.this, "NIM atau Password kosong!", Toast.LENGTH_LONG);
                    notice.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginMhs.this, Choose.class));
        finish();
    }

    private void saveLoginDetails(String id, String status, String chatUserName){
        new LoginPrefManager(this).saveLoginDetails(id, status, chatUserName);
    }

    private void performLogin(String emailId, String password) {
        auth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(LoginMhs.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(LoginMhs.this, "Password salah.",
                            Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(LoginMhs.this, MainActivityMhs.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
