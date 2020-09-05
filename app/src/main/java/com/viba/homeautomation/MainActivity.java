package com.viba.homeautomation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    GoogleSignInClient mgoogleSignInClient;
    private FirebaseAuth mAuth;
    int RC_SIGn_In = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }



    public void googlesignin(View view) {

        signIn();
    }

    public void phonesignin(View view) {
        startActivity(new Intent(MainActivity.this, Register.class));
    }
    private void signIn() {
        Intent SignInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(SignInIntent, RC_SIGn_In);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGn_In) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                FireBaseGoogleOuth(acc);

            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            FireBaseGoogleOuth(null);
            }
        }
    }

    private void FireBaseGoogleOuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build();

                    mgoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                    GoogleSignInAccount account1 = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                    if (account1 != null) {
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        HashMap<String, Object> jayasurya = new HashMap<>();
                        jayasurya.put("uname", account1.getDisplayName());
                        jayasurya.put("uemail", account1.getEmail());
                        jayasurya.put("uphone","");
                        jayasurya.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(jayasurya);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
    }
}