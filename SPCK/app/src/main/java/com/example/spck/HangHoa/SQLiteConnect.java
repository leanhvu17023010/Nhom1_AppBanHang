package com.example.spck.HangHoa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteConnect extends SQLiteOpenHelper {
    public SQLiteConnect(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    // Truy vấn ko trả kết quả : CREATE, DELETE,....
    public void queryData(String query){
        SQLiteDatabase Sqlitedatabase = getWritableDatabase ();
        Sqlitedatabase.execSQL (query);
    }

    // Truy vấn trả kết quả : SELECT
    public Cursor getData(String query){
        SQLiteDatabase Sqlitedatabase = getReadableDatabase ();
        Cursor data = Sqlitedatabase.rawQuery (query, null);
        return data;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



}
