package com.fixzone.app;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import com.google.mlkit.vision.barcode.*;
import com.google.mlkit.vision.common.InputImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrActivity extends AppCompatActivity {
    private ExecutorService executor=Executors.newSingleThreadExecutor(); private boolean done=false;
    public void onCreate(Bundle b){super.onCreate(b);PreviewView view=new PreviewView(this);setContentView(view);
        ListenableFuture<ProcessCameraProvider> f=ProcessCameraProvider.getInstance(this);
        f.addListener(()->{try{ProcessCameraProvider provider=f.get();Preview preview=new Preview.Builder().build();preview.setSurfaceProvider(view.getSurfaceProvider());
            ImageAnalysis analysis=new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
            BarcodeScanner scanner=BarcodeScanning.getClient();analysis.setAnalyzer(executor,proxy->{if(done){proxy.close();return;}android.media.Image media=proxy.getImage();if(media==null){proxy.close();return;}
                InputImage image=InputImage.fromMediaImage(media,proxy.getImageInfo().getRotationDegrees());
                scanner.process(image).addOnSuccessListener(codes->{if(!codes.isEmpty()&&!done){done=true;String value=codes.get(0).getRawValue();runOnUiThread(()->{new AlertDialog.Builder(this).setTitle("FIX ZONE QR").setMessage(value==null?"":value).setPositiveButton("OK",(d,w)->finish()).show();});}}).addOnCompleteListener(task->proxy.close());});
            provider.unbindAll();provider.bindToLifecycle(this,CameraSelector.DEFAULT_BACK_CAMERA,preview,analysis);
        }catch(Exception e){finish();}},ContextCompat.getMainExecutor(this));}
    protected void onDestroy(){super.onDestroy();executor.shutdown();}
}
