package com.allein.freund.authapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.allein.freund.authapp.remote.APIService;
import com.allein.freund.authapp.remote.APIUtils;
import com.allein.freund.authapp.remote.ItemDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {
    private APIService mAPIService;
    private String userCookie;
    private int ItemId;
    private List<ItemDetails> ItemDetails;
    private ItemDetailsAdapter adapter;
    public static final String ITEM_DETAILS = "com.allein.freund.authapp.ITEM_DETAILS";
    public static final String ITEM_ID = "com.allein.freund.authapp.ITEM_ID";
    private String TAG = "ITEM_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();

        String id = intent.getStringExtra(MainActivity.ITEM_ID);
        String name = intent.getStringExtra(MainActivity.ITEM_NAME);

        TextView detailsTitle = (TextView) findViewById(R.id.itemDetailsTitle);
        detailsTitle.setText("Order No. " + id);
        TextView detailsName = (TextView) findViewById(R.id.detailsName);
        detailsName.setText(name);

        mAPIService = APIUtils.getApiService();
        ItemId = Integer.parseInt(id);
        userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
        ItemDetails = new ArrayList<>();

        ListView detailsListView = (ListView) findViewById(R.id.detailsListView);
        adapter = new ItemDetailsAdapter(this, ItemDetails);
        detailsListView.setAdapter(adapter);

        getItemDetails();
    }

    private void getItemDetails() {
        mAPIService.getItemDetails(userCookie, ItemId).enqueue(new Callback<List<ItemDetails>>() {
            @Override
            public void onResponse(Call<List<ItemDetails>> call, Response<List<ItemDetails>> response) {
                if (response.isSuccessful()) {
                    List<ItemDetails> details = response.body();
                    setItemDetails(details);
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ItemDetails>> call, Throwable t) {
                Log.e(TAG, "Unable to fetch details.");
            }
        });
    }

    private void setItemDetails(List<ItemDetails> details) {
        ItemDetails.clear();
        ItemDetails.addAll(details);
        adapter.notifyDataSetChanged();

    }

    public void refreshItem(View view) {
        getItemDetails();
    }

    public void backToMain(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void scan(View view) {
        passToScanActivity();
    }

    private void passToScanActivity() {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ItemDetails>>() {
        }.getType();
        String json = gson.toJson(ItemDetails, type);
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ITEM_DETAILS, json);
        intent.putExtra(ITEM_ID, String.valueOf(ItemId));
        intent.putExtra(LoginActivity.USER_COOKIE, userCookie);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            finish();
        }
    }
}
