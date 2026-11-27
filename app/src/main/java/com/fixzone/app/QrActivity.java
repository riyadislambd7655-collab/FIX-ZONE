package com.fixzone.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.*;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrActivity extends AppCompatActivity {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        PreviewView previewView = new PreviewView(this);
        setContentView(previewView);

        ListenableFuture<ProcessCameraProvider> f = ProcessCameraProvider.getInstance(this);
        f.addListener(() -> {
            try {
                ProcessCameraProvider provider = f.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                BarcodeScanner scanner = BarcodeScanning.getClient();

                analysis.setAnalyzer(executor, proxy -> {
                    @SuppressWarnings("UnsafeOptInUsageError")
                    android.media.Image mediaImage = proxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(mediaImage, proxy.getImageInfo().getRotationDegrees());
                        scanner.process(image).addOnSuccessListener(codes -> {
                            if (!codes.isEmpty()) {
                                String value = codes.get(0).getRawValue();
                                runOnUiThread(() -> {
                                    if (!isFinishing()) {
                                        new AlertDialog.Builder(QrActivity.this)
                                                .setTitle("FIX ZONE QR")
                                                .setMessage(value == null ? "" : value)
                                                .setPositiveButton("OK", (d, w) -> finish())
                                                .setCancelable(false)
                                                .show();
                                    }
                                });
                            }
                        }).addOnCompleteListener(task -> proxy.close());
                    } else {
                        proxy.close();
                    }
                });

                provider.unbindAll();
                provider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis);
            } catch (Exception e) {
                finish();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
