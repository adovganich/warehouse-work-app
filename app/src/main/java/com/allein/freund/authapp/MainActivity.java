package com.allein.freund.authapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.allein.freund.authapp.remote.APIService;
import com.allein.freund.authapp.remote.APIUtils;
import com.allein.freund.authapp.remote.AuthService;
import com.allein.freund.authapp.remote.Item;
import com.allein.freund.authapp.remote.ItemDetails;
import com.allein.freund.authapp.remote.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ITEM_ID = "com.allein.freund.authapp.ITEM_ID";
    public static final String ITEM_NAME = "com.allein.freund.authapp.ITEM_NAME";
    private String TAG = "MAIN";
    private APIService mAPIService;
    private String userCookie;
    private List<Item> ItemList;
    private ListViewAdapter adapter;
    private Button logoutButton;
    private Button refreshButton;
    private Button addButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
        mAPIService = APIUtils.getApiService();
        ItemList = new ArrayList<>();
        ListView ItemListView = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, ItemList);
        ItemListView.setAdapter(adapter);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        getItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItems();
    }

    private Item getItem(int position) {
        return ItemList.get(position);
    }

    private void getItems() {
        mAPIService.getItems(userCookie).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    List<Item> Items = response.body();
                    populateList(Items);
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e(TAG, "Unable to fetch Items.");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.logoutButton:
                logout();
                break;
            case R.id.refreshButton:
                getItems();
                break;
            case R.id.addButton:
                scanToAddNew();
                break;
            case R.id.deleteButton:
                scanToDelete();
                break;
        }
    }

    private void scanToAddNew() {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanActivity.START_TYPE, 0);
        intent.putExtra(LoginActivity.USER_COOKIE, userCookie);
        startActivityForResult(intent, 1);
        getItems();
    }

    private void scanToDelete() {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanActivity.START_TYPE, 1);
        intent.putExtra(LoginActivity.USER_COOKIE, userCookie);
        startActivityForResult(intent, 1);
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

    private void populateList(List<Item> Items) {
        ItemList.clear();
        ItemList.addAll(Items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void passToItemDetailsActivity(Item Item) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra(LoginActivity.USER_COOKIE, userCookie);
        intent.putExtra(ITEM_ID, String.valueOf(Item.getId()));
        intent.putExtra(ITEM_NAME, Item.getName());
//        intent.putExtra("ExtraObj", Item);
        startActivity(intent);
    }
}
