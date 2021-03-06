package com.viba.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    String phoneNumber;
    EditText editText;

    Button buttonSignIn;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();
        buttonSignIn = findViewById(R.id.buttonSignIn);


        editText = findViewById(R.id.editTextCode);


        phoneNumber = getIntent().getStringExtra("phoneNumber");
        username = getIntent().getStringExtra("uname");
        sendVerificationCode(phoneNumber);

        // save phone number
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                try {
                    verifyCode(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference rootRef;
                            rootRef = FirebaseDatabase.getInstance().getReference("Users");
                            HashMap<String, Object> locMap = new HashMap<>();
                            locMap.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            locMap.put("uphone", phoneNumber);
                            locMap.put("uname",username);
                            locMap.put("uemail","");


                            // userLoc = FirebaseDatabase.getInstance().getReference("Users Location");
                            String uid = mAuth.getCurrentUser().getUid();
                            rootRef.child(uid).updateChildren(locMap);

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("uuid", uid);

                            rootRef.child(uid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    startActivity(new Intent(VerifyPhoneActivity.this, HomeActivity.class));
                                    finish();
                                }
                            });

                        } else {
                            // Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Toast.makeText(VerifyPhoneActivity.this, "SMS Code sent", Toast.LENGTH_LONG).show();

            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Toast.makeText(VerifyPhoneActivity.this, "completed", Toast.LENGTH_LONG).show();

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, ""+e, Toast.LENGTH_LONG).show();
        }
    };
}