package com.allein.freund.authapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends AppCompatActivity {
    private String userCookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        this.userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
    }

}
