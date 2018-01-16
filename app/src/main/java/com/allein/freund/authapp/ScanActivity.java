package com.allein.freund.authapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.allein.freund.authapp.remote.APIService;
import com.allein.freund.authapp.remote.APIUtils;
import com.allein.freund.authapp.remote.InvoiceDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private List<InvoiceDetails> remainItemList;
    private List<InvoiceDetails> doneItemList;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton;
    private String lastScanResult;
    private InvoiceDetailsAdapter adapterDone;
    private InvoiceDetailsAdapter adapterRemain;
    private String userCookie;
    private String invoiceId;
    private APIService mAPIService;

    private boolean isFlashLightOn = false;
    private String TAG = "SCAN";
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastScanResult)) {
                // Prevent duplicate scans
                return;
            }

            lastScanResult = result.getText();
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 10);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            Log.d(TAG, lastScanResult);
            InvoiceDetails item = getItemFromList(Integer.parseInt(lastScanResult), remainItemList);
            if (item != null) {
                pushItemToDone(item);
                lastScanResult = null;
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent intent = getIntent();
        Gson gson = new Gson();
        String items = intent.getStringExtra(InvoiceDetailsActivity.INVOICE_DETAILS);
        if (items != null) {
            Type type = new TypeToken<List<InvoiceDetails>>() {
            }.getType();
            remainItemList = gson.fromJson(items, type);
            Log.d(TAG, String.valueOf(remainItemList));
        } else {
            Log.d(TAG, "Items transition failed");
        }
        userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
        invoiceId = intent.getStringExtra(InvoiceDetailsActivity.INVOICE_ID);
        mAPIService = APIUtils.getApiService();
        doneItemList = new ArrayList<>();
        ListView remainListView = (ListView) findViewById(R.id.scanToDo);
        adapterRemain = new InvoiceDetailsAdapter(this, remainItemList);
        remainListView.setAdapter(adapterRemain);
        ListView doneListView = (ListView) findViewById(R.id.scanDone);
        adapterDone = new InvoiceDetailsAdapter(this, doneItemList);
        doneListView.setAdapter(adapterDone);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.scanWindow);

        barcodeScannerView.setTorchListener(this);
        barcodeScannerView.decodeContinuous(callback);

        switchFlashlightButton = (Button) findViewById(R.id.flashlight);

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        } else {
            switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                }
            });
        }
    }

    private InvoiceDetails getItemFromList(int id, List<InvoiceDetails> list) {
        for (InvoiceDetails item : list) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private void pushItemToDone(InvoiceDetails item) {
        if (item.getAmount() < 2) {
            remainItemList.remove(item);
        } else {
            item.decreaseAmount();
        }

        InvoiceDetails doneItem = getItemFromList(item.getId(), doneItemList);
        if (doneItem != null) {
            doneItem.increaseAmount();
        } else {
            doneItem = new InvoiceDetails();
            doneItem.setCost(item.getCost());
            doneItem.setId(item.getId());
            doneItem.setName(item.getName());
            doneItem.setAmount(1);
            doneItemList.add(doneItem);
        }

        adapterDone.notifyDataSetChanged();
        adapterRemain.notifyDataSetChanged();
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashLightOn) {
            barcodeScannerView.setTorchOff();
            isFlashLightOn = false;
        } else {
            barcodeScannerView.setTorchOn();
            isFlashLightOn = true;
        }

    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.flashlight_OFF);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.flashlight_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeScannerView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void backToDetails(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void sendToServer() {
        mAPIService.sendInoviceComplected(userCookie, Integer.parseInt(invoiceId)).enqueue(new Callback<List<InvoiceDetails>>() {
            @Override
            public void onResponse(Call<List<InvoiceDetails>> call, Response<List<InvoiceDetails>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Invoice completion sent.");
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceDetails>> call, Throwable t) {
                Log.e(TAG, "Unable to complete invoice.");
            }
        });
    }

    public void send(View view) {
        sendToServer();
        finish();
    }
}
