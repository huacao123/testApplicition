package com.example.wendy.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/3/15.
 * 数据库
 */

public class MyDBHelper extends SQLiteOpenHelper {

    //private static final String dbName = "APP_Login";
    //private static final int dbVersion = 1;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create Table person(personid Integer primary key autoincrement," +
                "classes text,username text,password text,birthday text,department text," +
                "job text,telephone text)";

        db.execSQL(sql);

/*        sql="insert into QQ_Login(qqnum,qqname,qqpass,qqimg,qqaction,belongcountry) values(?,?,?,?,?,?)";
        for(int i=0;i<users.length;i++){
            db.execSQL(sql, new Object[]{users[i].getQqnum(),users[i].getQqname(),users[i].getQqpass(),
                    users[i].getQqimg(),users[i].getQqaction(),users[i].getBelongcountry()});
        }
        sql="Create Table QQ_Contact(id integer primary key autoincrement," +
                "countactnum varchar,belongqq varchar)";

        db.execSQL(sql);
        sql="insert into QQ_Contact(countactnum,belongqq) values(?,?)";

        for(int i=0;i<nums.length;i++){
            db.execSQL(sql,new Object[]{nums[i],"000001"});
        }

        sql="Create View vContacts as select p.*,q.qqname,q.qqimg,q.qqaction,q.belongcountry" +
                " from QQ_Contact p left join QQ_Login q on p.countactnum=q.qqnum";
        db.execSQL(sql);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*        String sql="Create Table QQ_Contact(id integer primary key autoincrement," +
                "countactnum varchar,belongqq varchar)";

        db.execSQL(sql);
        sql="insert into QQ_Contact(countactnum,belongqq) values(?,?)";

        for(int i=0;i<nums.length;i++){
            db.execSQL(sql,new Object[]{nums[i],"000001"});
        }

        sql="Create View vContacts as select p.*,q.qqname,q.qqimg,q.qqaction,q.belongcountry" +
                " from QQ_Contact p left join QQ_Login q on p.countactnum=q.qqnum";
        db.execSQL(sql);*/
    }
}

