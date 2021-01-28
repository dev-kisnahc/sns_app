package com.kisnahc.sns_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //naver login
    private static final String CLIENT_ID = "aecYqKzQfK_5kXYHIMHg";
    private static final String CLIENT_SECRET = "fTfEMeqoKh";
    private static final String CLIENT_NAME = "sns_app";

    OAuthLogin oAuthLogin;
    OAuthLoginButton oAuthLoginButton;


    //firebase sign in

    EditText email_signin;
    EditText pwd_signin;
    private  FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;


    private String userName;
    private String profileUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //kakao login
        Function2<OAuthToken, Throwable, Unit> function2 = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    Log.i(TAG, "invoke: oAuthToken is not null");

                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            Log.i(TAG, "invoke: id = " + user.getId());
                            Log.i(TAG, "invoke: email = " + user.getKakaoAccount());

                            Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_LONG).show();

                            redirectSigupActivity();
                            return null;
                        }
                    });
                }
                return null;
            }
        };
        findViewById(R.id.btn_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    LoginClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);


                } else {
                    LoginClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);
                }
            }
        });

        //naver login
        oAuthLogin = OAuthLogin.getInstance();
        oAuthLogin.init(LoginActivity.this, CLIENT_ID, CLIENT_SECRET, CLIENT_NAME);

        findViewById(R.id.btn_naver).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                @SuppressLint("HandlerLeak")
                OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {


                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = oAuthLogin.getAccessToken(LoginActivity.this);
                            String refreshToken = oAuthLogin.getRefreshToken(LoginActivity.this);
                            Long expiresAt = oAuthLogin.getExpiresAt(LoginActivity.this);
                            String tokenType = oAuthLogin.getTokenType(LoginActivity.this);

                            redirectSigupActivity();
                        } else {
                            String errorCode = oAuthLogin.getLastErrorCode(LoginActivity.this).getCode();
                            String errorDesc = oAuthLogin.getLastErrorDesc(LoginActivity.this);

                            Toast.makeText(LoginActivity.this, "errorCode :" + errorCode + ", errorDesc :" + errorDesc, Toast.LENGTH_LONG).show();

                            Log.w(TAG, "errorCode :" +errorCode + "errorDesc :" + errorDesc );
                        }
                    }
                };
                oAuthLogin.startOauthLoginActivity(LoginActivity.this, oAuthLoginHandler);
            }
        });

        //sign in email

        firebaseAuth = FirebaseAuth.getInstance();
        email_signin = findViewById(R.id.et_email);
        pwd_signin = findViewById(R.id.et_password);

        findViewById(R.id.btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email_signin.getText().toString().trim();
                String mPwd = pwd_signin.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(mEmail, mPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            Toast.makeText(LoginActivity.this, "로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                            redirectSigupActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }


        });
        //google signin

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.btn_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                            redirectSigupActivity();

                        } else {
                            Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirectSigupActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}