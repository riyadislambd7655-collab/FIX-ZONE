package com.fixzone.app;

import android.Manifest;
import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    ShopDb db; LinearLayout list; boolean bn=true;
    static final int CAMERA_REQ=41, BACKUP_REQ=42;
    protected void onCreate(Bundle b){ super.onCreate(b); db=new ShopDb(this); home(); }
    String t(String a,String b){return bn?a:b;}
    TextView label(String s,int size){ TextView v=new TextView(this); v.setText(s); v.setTextSize(size); v.setPadding(8,12,8,12); return v; }
    Button btn(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setOnClickListener(v->r.run());return b;}
    void setup(String title){ LinearLayout page=new LinearLayout(this);page.setOrientation(1);page.setPadding(16,12,16,12);page.setBackgroundColor(0xfff4f7fa); setContentView(page);
        LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);TextView brand=label("★ FIX ZONE",23);bar.addView(brand,new LinearLayout.LayoutParams(0,-2,1));bar.addView(btn(bn?"English":"বাংলা",()->{bn=!bn;home();}));page.addView(bar);page.addView(label(title,20));
        ScrollView sc=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(1);sc.addView(list);page.addView(sc,new LinearLayout.LayoutParams(-1,0,1)); }
    void home(){setup(t("দোকান ব্যবস্থাপনা","Shop Management"));
        list.addView(label(t("FIX AI • আপনার দোকানের সহকারী","FIX AI • Shop assistant"),16));
        list.addView(btn(t("পণ্য যোগ করুন","Add inventory item"),()->addInventory()));
        list.addView(btn(t("রিপেয়ার রেকর্ড যোগ করুন","Add repair record"),()->addRepair()));
        list.addView(btn(t("ইনভেন্টরি দেখুন","View inventory"),()->showTable("inventory")));
        list.addView(btn(t("রিপেয়ার তালিকা","Repair list"),()->showTable("repairs")));
        list.addView(btn(t("QR / Barcode স্ক্যান","Scan QR / Barcode"),()->startQr()));
        list.addView(btn(t("Admin biometric যাচাই","Admin biometric check"),()->biometric()));
        list.addView(btn(t("ডেটা Backup Export","Export data backup"),()->backup()));
        list.addView(btn(t("AI ও Cloud সংযোগ সেটআপ","AI & Cloud integration setup"),()->integrationInfo()));
        list.addView(label(t("নোট: Local database চালু। Cloud ও AI চালু করতে নিজস্ব নিরাপদ সার্ভার/API সংযোগ লাগবে।","Local database is active. Cloud and AI require your secure server/API connection."),13));
    }
    EditText field(String hint){EditText e=new EditText(this);e.setHint(hint);list.addView(e);return e;}
    void addInventory(){setup(t("নতুন পণ্য","New item"));EditText n=field(t("পণ্যের নাম","Item name")),q=field(t("পরিমাণ","Quantity")),p=field(t("দাম","Price"));list.addView(btn(t("সংরক্ষণ","Save"),()->{try{db.addItem(n.getText().toString(),Integer.parseInt(q.getText().toString()),Double.parseDouble(p.getText().toString()));home();}catch(Exception e){toast("সব ঘর সঠিকভাবে পূরণ করুন");}}));}
    void addRepair(){setup(t("রিপেয়ার রেকর্ড","Repair record"));EditText c=field(t("কাস্টমারের নাম","Customer name")),ph=field(t("ফোন","Phone")),d=field(t("মোবাইল মডেল","Device model")),pr=field(t("সমস্যা","Problem"));list.addView(btn(t("সংরক্ষণ","Save"),()->{db.addRepair(c.getText().toString(),ph.getText().toString(),d.getText().toString(),pr.getText().toString());home();}));}
    void showTable(String table){setup(table.equals("inventory")?t("ইনভেন্টরি","Inventory"):t("রিপেয়ার","Repairs"));android.database.Cursor c=db.getReadableDatabase().rawQuery("SELECT * FROM "+table,null);while(c.moveToNext()){StringBuilder s=new StringBuilder();for(int i=1;i<c.getColumnCount();i++)s.append(c.getString(i)).append(" • ");list.addView(label(s.toString(),15));}c.close();list.addView(btn(t("ফিরে যান","Back"),()->home()));}
    void startQr(){if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_REQ);}else startActivity(new Intent(this,QrActivity.class));}
    public void onRequestPermissionsResult(int r,String[] p,int[] g){super.onRequestPermissionsResult(r,p,g);if(r==CAMERA_REQ&&g.length>0&&g[0]==PackageManager.PERMISSION_GRANTED)startActivity(new Intent(this,QrActivity.class));}
    void biometric(){BiometricManager bm=BiometricManager.from(this);if(bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG|BiometricManager.Authenticators.BIOMETRIC_WEAK)!=BiometricManager.BIOMETRIC_SUCCESS){toast(t("এই ফোনে biometric প্রস্তুত নেই","Biometric is not available"));return;}
        BiometricPrompt prompt=new BiometricPrompt(this,ContextCompat.getMainExecutor(this),new BiometricPrompt.AuthenticationCallback(){public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult r){toast("Verified");}});
        BiometricPrompt.PromptInfo info=new BiometricPrompt.PromptInfo.Builder().setTitle("FIX ZONE Admin").setSubtitle(t("ফোনের biometric দিয়ে যাচাই করুন","Verify with device biometrics")).setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG|BiometricManager.Authenticators.BIOMETRIC_WEAK).setNegativeButtonText(t("বাতিল","Cancel")).build();prompt.authenticate(info);}
    void backup(){Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.setType("application/json");i.putExtra(Intent.EXTRA_TITLE,"FIX_ZONE_backup.json");startActivityForResult(i,BACKUP_REQ);}
    protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==BACKUP_REQ&&c==RESULT_OK&&d!=null)try(java.io.OutputStream o=getContentResolver().openOutputStream(d.getData())){o.write(db.exportJson().toString(2).getBytes("UTF-8"));toast("Backup exported");}catch(Exception e){toast("Backup failed");}}
    void integrationInfo(){setup(t("AI ও Cloud সংযোগ","AI & Cloud connection"));list.addView(label(t("AI: নিরাপদ backend endpoint ও API key প্রয়োজন। API key APK-তে রাখা যাবে না।","AI: a secure backend endpoint and API key are required. Never embed API keys in the APK."),15));list.addView(label(t("Cloud: Firebase/Drive project, credentials, rules ও user consent প্রয়োজন।","Cloud: Firebase/Drive project, credentials, security rules and user consent are required."),15));list.addView(label(t("এই source-এ integration points রাখা হয়েছে; live service এখনো সংযুক্ত নয়।","Integration points are prepared; live services are not connected yet."),14));list.addView(btn(t("ফিরে যান","Back"),()->home()));}
    void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
}
