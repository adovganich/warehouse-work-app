package com.allein.freund.authapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.allein.freund.authapp.remote.APIService;
import com.allein.freund.authapp.remote.APIUtils;
import com.allein.freund.authapp.remote.AuthService;
import com.allein.freund.authapp.remote.Invoice;
import com.allein.freund.authapp.remote.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private APIService mAPIService;
    private String userCookie;
    private String TAG = "MAIN";
    private List<Invoice> invoiceList;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
        mAPIService = APIUtils.getApiService();
        invoiceList = new ArrayList<>();
        ListView invoiceListView = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, invoiceList);
        invoiceListView.setAdapter(adapter);

        getInvoices();
    }

    private void getInvoices() {
        mAPIService.getInvoices(userCookie).enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if (response.isSuccessful()) {
                    List<Invoice> invoices = response.body();
                    populateList(invoices);
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                Log.e(TAG, "Unable to fetch invoices.");
            }
        });
    }

    public void refreshInvoices(View view) {
        getInvoices();
    }

    public void logout(View view) {
        logout();
    }

    private void logout() {
        finish();
        AuthService mAuthService = APIUtils.getAuthService();
        mAuthService.logout().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i(TAG, "Logout.");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Unable to logout form server:" + t.getMessage());
            }
        });
    }

    private void populateList(List<Invoice> invoices) {
        invoiceList.clear();
        invoiceList.addAll(invoices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

}
