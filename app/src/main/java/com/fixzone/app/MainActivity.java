package com.fixzone.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ShopDb extends SQLiteOpenHelper {
    public ShopDb(Context context) {
        super(context, "fixzone.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, qty INTEGER, price REAL)");
        db.execSQL("CREATE TABLE repairs (id INTEGER PRIMARY KEY AUTOINCREMENT, customer TEXT, phone TEXT, device TEXT, issue TEXT, cost REAL, status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS repairs");
        onCreate(db);
    }

    public void addItem(String name, int qty, double price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("qty", qty);
        cv.put("price", price);
        db.insert("items", null, cv);
    }

    public void addRepair(String customer, String phone, String device, String issue, double cost) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("customer", customer);
        cv.put("phone", phone);
        cv.put("device", device);
        cv.put("issue", issue);
        cv.put("cost", cost);
        cv.put("status", "Pending");
        db.insert("repairs", null, cv);
    }

    public List<String> getAllItems() {
        List<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM items ORDER BY id DESC", null);
        while (c.moveToNext()) {
            list.add("📦 " + c.getString(1) + " | Qty: " + c.getInt(2) + " | ৳" + c.getDouble(3));
        }
        c.close();
        return list;
    }

    public List<String> getAllRepairs() {
        List<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM repairs ORDER BY id DESC", null);
        while (c.moveToNext()) {
            list.add("🛠️ " + c.getString(1) + " (" + c.getString(2) + ")\n📱 " + c.getString(3) + " - " + c.getString(4) + " | ৳" + c.getDouble(5));
        }
        c.close();
        return list;
    }

    public String exportBackupText() {
        StringBuilder sb = new StringBuilder("=== FIX ZONE BACKUP ===\n\n[INVENTORY]\n");
        for (String s : getAllItems()) sb.append(s).append("\n");
        sb.append("\n[REPAIRS]\n");
        for (String s : getAllRepairs()) sb.append(s).append("\n\n");
        return sb.toString();
    }
}
