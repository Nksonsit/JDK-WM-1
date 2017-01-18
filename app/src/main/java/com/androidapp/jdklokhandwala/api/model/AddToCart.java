package com.androidapp.jdklokhandwala.api.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidapp.jdklokhandwala.AddToCartModel;
import com.androidapp.jdklokhandwala.dbhelper.DatabaseManager;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 10-01-2017.
 */

@AutoValue
public abstract class AddToCart implements AddToCartModel {
    public static Factory<AddToCart> ADDTOCART_FACTORY = new Factory<>(AutoValue_AddToCart::new);
    public static RowMapper<AddToCart> ROW_MAPPER = ADDTOCART_FACTORY.select_all_dataMapper();

    public static void InsertProduct(AddToCart addToCart) {
        if (!CheckDuplication(addToCart)) {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            sqLiteDatabase.insert(AddToCart.TABLE_NAME, null, ADDTOCART_FACTORY.marshal()
                    .CategoryID(addToCart.CategoryID())
                    .ProductID(addToCart.ProductID())
                    .Name(addToCart.Name())
                    .UnitType(addToCart.UnitType())
                    .UnitValue(addToCart.UnitValue())
                    .KgWeight(addToCart.KgWeight())
                    .UnitTypes(addToCart.UnitTypes())
                    .DefaultWeight(addToCart.DefaultWeight())
                    .asContentValues());
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public static boolean CheckDuplication(AddToCart addToCart) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(AddToCart.SELECT_ALL_DATA_BY_ID, new String[]{String.valueOf(addToCart.CategoryID()), String.valueOf(addToCart.ProductID())});
        if (cursor.getCount() > 0) {
            return true;
        }
        DatabaseManager.getInstance().closeDatabase();
        return false;
    }

    public static boolean CheckDuplication(int CId,int PId) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(AddToCart.SELECT_ALL_DATA_BY_ID, new String[]{String.valueOf(CId), String.valueOf(PId)});
        if (cursor.getCount() > 0) {
            return true;
        }
        DatabaseManager.getInstance().closeDatabase();
        return false;
    }

    public static void DeleteAllData() {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.delete(AddToCart.TABLE_NAME, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<AddToCart> getCartList() {
        List<AddToCart> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(AddToCart.SELECT_ALL_DATA, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(ROW_MAPPER.map(cursor));
                } while (cursor.moveToNext());
            }
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static void DeleteItem(Long categoryId, Long productId) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.delete(AddToCart.TABLE_NAME, AddToCart.CATEGORYID + " = ? AND " + AddToCart.PRODUCTID + " = ? ", new String[]{String.valueOf(categoryId), String.valueOf(productId)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void UpdateItem(AddToCart addToCart) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.update(AddToCart.TABLE_NAME, ADDTOCART_FACTORY.marshal()
                .CategoryID(addToCart.CategoryID())
                .ProductID(addToCart.ProductID())
                .Name(addToCart.Name())
                .UnitType(addToCart.UnitType())
                .UnitValue(addToCart.UnitValue())
                .KgWeight(addToCart.KgWeight())
                .UnitTypes(addToCart.UnitTypes())
                .DefaultWeight(addToCart.DefaultWeight())
                .asContentValues(), AddToCart.CATEGORYID + " = ? AND " + AddToCart.PRODUCTID + " = ? ", new String[]{String.valueOf(addToCart.CategoryID()), String.valueOf(addToCart.ProductID())});
        DatabaseManager.getInstance().closeDatabase();
    }
}
