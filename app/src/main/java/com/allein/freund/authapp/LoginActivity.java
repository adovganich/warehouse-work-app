package com.allein.freund.authapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allein.freund.authapp.AuthService.AuthUtils;
import com.allein.freund.authapp.AuthService.AuthService;
import com.allein.freund.authapp.AuthService.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private AuthService mAuthService;
    private String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mAuthService = AuthUtils.getAuthService();
        Button loginBtn = (Button) findViewById(R.id.btn_submit);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText emailInput = (EditText) findViewById(R.id.email_input);
                final EditText passwordInput = (EditText) findViewById(R.id.password_input);

                if (credentialsAreValid(emailInput, passwordInput)) {
                    String email = emailInput.getText().toString().trim();
                    String password = passwordInput.getText().toString().trim();
                    sendCredentials(email, password);
                }
            }
        });
    }

    public void sendCredentials(String email, String password) {
        mAuthService.sendCredentials(email, password)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            loginToast("Login complete.");
                            Log.i(TAG, "login complete.");
                            passToMainActivity();
                        } else {
                            loginToast("Credentials are invalid.");
                            Log.i(TAG, "credentials are invalid.");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        loginToast("Login failed. Server is offline.");
                        Log.e(TAG, "Unable to sent credentials to API. Server is offline.");
                    }
                });
    }

    public void passToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loginToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private Boolean credentialsAreValid(EditText emailInput, EditText passwordInput) {
        Boolean flag = true;
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Fill e-mail field.");
            flag = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Wrong e-mail format.");
            flag = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Fill password field.");
            flag = false;
        }
        return flag;
    }

}

