package com.fixzone.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ShopDb db;
    private boolean isBangla = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ShopDb(this);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                return false;
            }
        });

        loadDashboard();
    }

    private void loadDashboard() {
        int itemCount = db.getAllItems().size();
        int repairCount = db.getAllRepairs().size();

        String html = "<!doctype html><html lang='bn'><head><meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "<style>" +
                "*{box-sizing:border-box;user-select:none}" +
                "body{margin:0;min-height:100vh;padding:16px;font-family:system-ui,-apple-system,sans-serif;color:#f7fbff;" +
                "background:radial-gradient(circle at 20% 10%,#123c72 0,transparent 40%),linear-gradient(145deg,#020713,#071b35 55%,#020713)}" +
                ".card{max-width:480px;margin:0 auto;padding:22px 18px;border:1.5px solid #d9a82e;border-radius:22px;" +
                "background:linear-gradient(160deg,#0c2546,#030b18);box-shadow:0 0 25px #087bff44,inset 0 0 18px #d9a82e22}" +
                ".top-bar{display:flex;justify-content:space-between;align-items:center;margin-bottom:10px}" +
                ".lang-btn{background:#091d38;color:#ffd45e;border:1px solid #d9a82e;padding:6px 12px;border-radius:10px;font-weight:700;font-size:12px}" +
                ".header{text-align:center;margin-bottom:18px}" +
                ".logo{width:76px;height:76px;margin:0 auto 10px;border:2.5px solid #e7b83e;border-radius:50%;display:grid;place-items:center;" +
                "font-size:28px;font-weight:900;color:#f8d56b;text-shadow:0 0 12px #ffc400;box-shadow:0 0 18px #087bff88}" +
                "h1{margin:0;font-size:26px;letter-spacing:2px;color:#ffd45e}" +
                ".sub{font-size:13px;color:#d8e8ff;font-weight:600;margin-top:2px}" +
                ".tag{color:#f6d16c;font-size:11px;letter-spacing:1.5px;margin-top:4px}" +
                ".stats{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-bottom:16px}" +
                ".stat-box{background:#061429;border:1px solid #d9a82e66;border-radius:14px;padding:12px;text-align:center}" +
                ".stat-num{font-size:22px;font-weight:900;color:#ffd45e}" +
                ".stat-lbl{font-size:12px;color:#b8d4f5;margin-top:2px}" +
                ".grid{display:grid;grid-template-columns:1fr 1fr;gap:10px}" +
                ".btn{display:flex;flex-direction:column;align-items:center;justify-content:center;text-align:center;gap:6px;" +
                "padding:14px 10px;border-radius:14px;font-size:14px;font-weight:700;color:#f7fbff;" +
                "background:linear-gradient(145deg,#0f2d54,#06152b);border:1px solid #d9a82e88;box-shadow:0 4px 12px #0006}" +
                ".btn:active{transform:scale(0.97);border-color:#ffd45e}" +
                ".btn-gold{grid-column:span 2;flex-direction:row;color:#071426;background:linear-gradient(100deg,#ffe18a,#c88b19);border:none;font-size:15px;font-weight:800}" +
                ".links{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-top:14px}" +
                "a.ext{display:flex;align-items:center;justify-content:center;gap:6px;padding:12px;border-radius:12px;text-decoration:none;font-size:13px;font-weight:800}" +
                "a.map{color:#071426;background:linear-gradient(100deg,#ffe18a,#c88b19)}" +
                "a.fb{color:#fff;background:linear-gradient(100deg,#1877f2,#0752b5)}" +
                ".details{margin-top:18px;padding-top:14px;border-top:1px solid #ffffff22;text-align:center;font-size:12px;line-height:1.6;color:#d7e5f7}" +
                ".details strong{color:#ffdc79;font-size:14px}" +
                "footer{margin-top:12px;text-align:center;color:#f5d06b;font-size:11px;letter-spacing:1px}" +
                "</style></head><body><div class='card'>" +
                "<div class='top-bar'><span style='color:#ffd45e;font-size:12px;font-weight:700'>● SHOP SYSTEM</span>" +
                "<button class='lang-btn' onclick='Android.toggleLang()'>" + (isBangla ? "ENGLISH" : "বাংলা") + "</button></div>" +
                "<div class='header'><div class='logo'>FZ</div><h1>FIX ZONE</h1>" +
                "<div class='sub'>Advanced Tech Lab</div>" +
                "<div class='tag'>" + (isBangla ? "আপনার বিশ্বস্ত টেক পার্টনার" : "YOUR TRUSTED TECH PARTNER") + "</div></div>" +
                "<div class='stats'>" +
                "<div class='stat-box'><div class='stat-num'>" + itemCount + "</div><div class='stat-lbl'>" + (isBangla ? "মোট পণ্য (Stock)" : "Total Items") + "</div></div>" +
                "<div class='stat-box'><div class='stat-num'>" + repairCount + "</div><div class='stat-lbl'>" + (isBangla ? "রিপেয়ার রেকর্ড" : "Repair Records") + "</div></div>" +
                "</div>" +
                "<div class='grid'>" +
                "<div class='btn' onclick='Android.addItem()'><span>📦</span>" + (isBangla ? "পণ্য যোগ করুন" : "Add Product") + "</div>" +
                "<div class='btn' onclick='Android.addRepair()'><span>🛠️</span>" + (isBangla ? "রিপেয়ার রেকর্ড যোগ" : "Add Repair") + "</div>" +
                "<div class='btn' onclick='Android.viewInventory()'><span>📋</span>" + (isBangla ? "ইনভেন্টরি দেখুন" : "View Inventory") + "</div>" +
                "<div class='btn' onclick='Android.viewRepairs()'><span>📱</span>" + (isBangla ? "রিপেয়ার তালিকা" : "Repair List") + "</div>" +
                "<div class='btn' onclick='Android.authAdmin()'><span>🔐</span>" + (isBangla ? "ADMIN লক যাচাই" : "Admin Biometric") + "</div>" +
                "<div class='btn' onclick='Android.backupData()'><span>💾</span>" + (isBangla ? "ডেটা BACKUP" : "Export Backup") + "</div>" +
                "<div class='btn btn-gold' onclick='Android.openQr()'><span>📷</span> " + (isBangla ? "QR / BARCODE স্ক্যান করুন" : "SCAN QR / BARCODE") + "</div>" +
                "</div>" +
                "<div class='links'>" +
                "<a class='ext map' href='https://goo.gl'>📍 LOCATION</a>" +
                "<a class='ext fb' href='https://facebook.com'>f FACEBOOK</a>" +
                "</div>" +
                "<div class='details'><strong>MD RIYAD</strong><br>G.S.M Technician / Proprietor<br>" +
                "01792403699 | 01302378142<br>Dighirpar Bottola, Palli Bidyut, Chandra, Gazipur, Dhaka</div>" +
                "<footer>FIX ZONE — SERVICE WITH TRUST</footer>" +
                "</div></body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void toggleLang() {
            runOnUiThread(() -> {
                isBangla = !isBangla;
                loadDashboard();
            });
        }

        @JavascriptInterface
        public void addItem() {
            runOnUiThread(() -> {
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 30, 50, 10);
                EditText name = new EditText(MainActivity.this);
                name.setHint(isBangla ? "পণ্যের নাম" : "Item Name");
                EditText qty = new EditText(MainActivity.this);
                qty.setHint(isBangla ? "পরিমাণ (Qty)" : "Quantity");
                qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                EditText price = new EditText(MainActivity.this);
                price.setHint(isBangla ? "দাম (টাকা)" : "Price");
                price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layout.addView(name); layout.addView(qty); layout.addView(price);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(isBangla ? "নতুন পণ্য যোগ করুন" : "Add New Item")
                        .setView(layout)
                        .setPositiveButton("Save", (d, w) -> {
                            try {
                                db.addItem(name.getText().toString(), Integer.parseInt(qty.getText().toString()), Double.parseDouble(price.getText().toString()));
                                Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                loadDashboard();
                            } catch (Exception e) {
