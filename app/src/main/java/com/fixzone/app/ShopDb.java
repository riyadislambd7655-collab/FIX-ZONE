package com.fixzone.app;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShopDb extends SQLiteOpenHelper {
    public ShopDb(Context c) { super(c, "fixzone.db", null, 1); }
    public void onCreate(SQLiteDatabase d) {
        d.execSQL("CREATE TABLE inventory(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,qty INTEGER NOT NULL,price REAL NOT NULL)");
        d.execSQL("CREATE TABLE repairs(id INTEGER PRIMARY KEY AUTOINCREMENT,customer TEXT,phone TEXT,device TEXT,problem TEXT,status TEXT)");
    }
    public void onUpgrade(SQLiteDatabase d,int o,int n) { d.execSQL("DROP TABLE IF EXISTS inventory"); d.execSQL("DROP TABLE IF EXISTS repairs"); onCreate(d); }
    public void addItem(String name,int qty,double price) {
        ContentValues v=new ContentValues(); v.put("name",name); v.put("qty",qty); v.put("price",price); getWritableDatabase().insert("inventory",null,v);
    }
    public void addRepair(String customer,String phone,String device,String problem) {
        ContentValues v=new ContentValues(); v.put("customer",customer); v.put("phone",phone); v.put("device",device); v.put("problem",problem); v.put("status","Pending"); getWritableDatabase().insert("repairs",null,v);
    }
    public JSONArray exportJson() throws Exception {
        JSONObject out=new JSONObject(); JSONArray inv=new JSONArray(), rep=new JSONArray();
        Cursor c=getReadableDatabase().rawQuery("SELECT * FROM inventory",null);
        while(c.moveToNext()){ JSONObject x=new JSONObject(); x.put("id",c.getInt(0)); x.put("name",c.getString(1)); x.put("qty",c.getInt(2)); x.put("price",c.getDouble(3)); inv.put(x); } c.close();
        c=getReadableDatabase().rawQuery("SELECT * FROM repairs",null);
        while(c.moveToNext()){ JSONObject x=new JSONObject(); x.put("id",c.getInt(0)); x.put("customer",c.getString(1)); x.put("phone",c.getString(2)); x.put("device",c.getString(3)); x.put("problem",c.getString(4)); x.put("status",c.getString(5)); rep.put(x); } c.close();
        out.put("inventory",inv); out.put("repairs",rep); out.put("app","FIX ZONE"); return new JSONArray().put(out);
    }
}
