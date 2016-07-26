package com.suiyueshentou.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suiyueshentou.utils.DebugLog;


/**
 * 数据库
 * Created by zc on 2016/7/4.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK;

    public static String mDatabaseName = "suiyueshentou.db";

    static {
        CREATE_BOOK = "create table test ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "sex text)";
    }

    private Context mContext;

    /**
     *
     * @param context
     * @param name  数据库名
     * @param factory
     * @param version  版本号
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        DebugLog.e(DebugLog.TAG, "书籍 建表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
