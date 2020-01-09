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

public class LoginDosen extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference firebaseRef;
    ProgressBar progressBar;
    EditText nik,sandi;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dosen);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        progressBar = findViewById(R.id.progressBar);
        nik = findViewById(R.id.txtNik);
        sandi = findViewById(R.id.txtPassDosen);
        btLogin = findViewById(R.id.loginDosen);
        firebaseRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = nik.getText().toString() ;
                final String password = sandi.getText().toString() ;
                progressBar.setVisibility(View.VISIBLE);
                if(username.length() > 0 && password.length() > 0){
                    firebaseRef.child("dosen").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userEmail =  dataSnapshot.child("email").getValue(String.class);
                            String userFullName =  dataSnapshot.child("fullname").getValue(String.class);
                            if(userEmail != null){
                                saveLoginDetails(username, "dosen", userFullName);
                                globalVariable.setId(username);
                                performLogin(userEmail,password);
                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast notice = Toast.makeText(LoginDosen.this, "NIK tidak terdaftar!", Toast.LENGTH_LONG);
                                notice.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast notice = Toast.makeText(LoginDosen.this, "Koneksi ke database gagal!", Toast.LENGTH_LONG);
                            notice.show();
                            finish();
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast notice = Toast.makeText(LoginDosen.this, "NIK atau Password kosong!", Toast.LENGTH_LONG);
                    notice.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginDosen.this, Choose.class));
        finish();
    }

    private void saveLoginDetails(String id, String status, String chatUserName){
        new LoginPrefManager(this).saveLoginDetails(id, status, chatUserName);
    }

    private void performLogin(String emailId, String password) {
        auth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(LoginDosen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(LoginDosen.this, "Password salah.",
                            Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(LoginDosen.this, MainActivityDosen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
