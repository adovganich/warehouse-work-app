package com.allein.freund.authapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allein.freund.authapp.remote.APIService;
import com.allein.freund.authapp.remote.APIUtils;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    public static final String START_TYPE = "com.allein.freund.authapp.START_TYPE";

    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton;
    private String lastScanResult;
    private String userCookie;
    private int startType;
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
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            Log.d(TAG, lastScanResult);
            if (lastScanResult != null) {
                if (startType == 0) {
                    addItem(lastScanResult);
                } else if (startType == 1) {
                    deleteItem(lastScanResult);
                }
            }
            try {
                barcodeScannerView.pause();
                Thread.sleep(1000);
                barcodeScannerView.resume();
            } catch (InterruptedException x) {
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
        startType = intent.getIntExtra(START_TYPE, -1);
        userCookie = intent.getStringExtra(LoginActivity.USER_COOKIE);
        mAPIService = APIUtils.getApiService();
        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.scanWindow);
        barcodeScannerView.setTorchListener(this);
        barcodeScannerView.decodeContinuous(callback);
        switchFlashlightButton = (Button)findViewById(R.id.flashlight);
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
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addItem(String itemId) {
        mAPIService.getItemRequest(userCookie, itemId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Item completion sent.");
                    showToastWithText("Complete!");
                    setResult(1);
                    finish();
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                    showToastWithText("Not found!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to complete Item:" + t.getMessage());
                showToastWithText("No connection!");
            }
        });
    }

    private void deleteItem(String itemId) {
        mAPIService.deleteItemRequest(userCookie, itemId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Item completion sent.");
                    showToastWithText("Complete!");
                    setResult(1);
                    finish();
                } else {
                    Log.i(TAG, "Something goes wrong:" + response.message());
                    showToastWithText("Not found!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to complete Item:" + t.getMessage());
                showToastWithText("No connection!");
            }
        });
    }

    private void showToastWithText(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
