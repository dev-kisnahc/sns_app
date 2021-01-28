package com.kisnahc.sns_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private FirebaseAuth firebaseAuth;


    EditText email, pwd, repwd, name;
    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.et_email_signup);
        pwd = findViewById(R.id.et_pwd_signup);
        repwd = findViewById(R.id.et_re_pwd);
        name = findViewById(R.id.et_username);



        findViewById(R.id.btn_signup2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                String Pwd = pwd.getText().toString().trim();
                String RePwd = repwd.getText().toString().trim();

                if (Pwd.equals(RePwd)) {
                    Log.d(TAG, "등록버튼" + email + " , " + pwd);

                    firebaseAuth.createUserWithEmailAndPassword(Email, Pwd).addOnCompleteListener(SignupActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String Email = user.getEmail();
                                        String Uid = user.getUid();
                                        String Name = name.getText().toString().trim();

                                        HashMap<Object,String> hashMap = new HashMap<>();

                                        hashMap.put("uid",Uid);
                                        hashMap.put("email",Email);
                                        hashMap.put("name",Name);



                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(SignupActivity.this, "회원가입 완료!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(SignupActivity.this, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "실패" + pwd);
                    return;
                }
            }
        });


    }
}