package com.androidapp.jdklokhandwala.api.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidapp.jdklokhandwala.BookmarkModel;
import com.androidapp.jdklokhandwala.dbhelper.DatabaseManager;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static com.androidapp.jdklokhandwala.R.id.bookmark;

/**
 * Created by ishan on 10-01-2017.
 */

@AutoValue
public abstract class Bookmark implements BookmarkModel {
    public static Factory<Bookmark> BOOKMARK_FACTORY = new Factory<>(AutoValue_Bookmark::new);
    public static RowMapper<Bookmark> ROW_MAPPER = BOOKMARK_FACTORY.select_all_dataMapper();

    public static void InsertBookmark(int categoryID, String name, String image) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        if (!CheckDuplication(categoryID)) {
            sqLiteDatabase.insert(Bookmark.TABLE_NAME, null, BOOKMARK_FACTORY.marshal().CategoryId((long) categoryID).Image(image).Name(name).asContentValues());
        } else {
            DeleteBookmark((long) categoryID);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean CheckDuplication(int id) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Bookmark.SELECT_ALL_DATA_BY_ID, new String[]{String.valueOf(id)});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        DatabaseManager.getInstance().closeDatabase();
        return false;
    }

    public static void DeleteBookmark(Long id) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.delete(Bookmark.TABLE_NAME, Bookmark.CATEGORYID + " = ?", new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean IsBookmark(int id) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Bookmark.SELECT_ALL_DATA_BY_ID, new String[]{String.valueOf(id)});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        DatabaseManager.getInstance().closeDatabase();
        return false;
    }

    public static List<Bookmark> GetBookmarkList() {
        List<Bookmark> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Bookmark.SELECT_ALL_DATA, null);
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
}
