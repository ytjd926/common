//package com.tangjd.common.db;
//
//import android.database.sqlite.SQLiteDatabase;
//
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//import com.minorfish.patrolpda.abs.App;
//
//import java.sql.SQLException;
//
///**
// * Author: tangjd
// * Date: 2017/9/11
// */
//
//public class DatabaseHelperSample extends OrmLiteSqliteOpenHelper {
//    public static final String DB_NAME = "database.db";
//    public static final int DB_VERSION = 1;
//    private static DatabaseHelperSample mHelper;
//
//    public static DatabaseHelperSample getInstance() {
//        if (mHelper == null) {
//            synchronized (DatabaseHelperSample.class) {
//                if (mHelper == null) {
//                    // mHelper = OpenHelperManager.getHelper(App.getApp().getApplicationContext(), DatabaseHelperSample.class);
//                    mHelper = new DatabaseHelperSample();
//                }
//            }
//        }
//        return mHelper;
//    }
//
//    public DatabaseHelperSample() {
//        super(App.getApp().getApplicationContext(), DB_NAME, null, DB_VERSION);
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//        try {
//            TableUtils.createTable(connectionSource, TbPatrol.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//            TableUtils.dropTable(connectionSource, TbPatrol.class, true);
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
