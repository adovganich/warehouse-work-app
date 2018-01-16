package com.allein.freund.authapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


import com.allein.freund.authapp.remote.InvoiceDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.lang.reflect.Type;
import java.util.List;


public class ScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private List<InvoiceDetails> itemList;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton;
    private String lastScanResult;
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
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 70);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            Log.d(TAG, lastScanResult);
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
            itemList = gson.fromJson(items, type);
            Log.d(TAG, String.valueOf(itemList));
        } else {
            Log.d(TAG, "Items transition failed");
        }

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
}
