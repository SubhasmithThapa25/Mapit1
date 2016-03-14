package com.example.hp_pc.mapit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by HP-PC on 19-12-2015.
 */
class MainDbAdapter {
    DbAdapter smith;

    MainDbAdapter(Context context) {
        smith = new DbAdapter(context);
    }


    //insert data into database from sign up page
    long insertData(String name, String email, String password) {
        SQLiteDatabase database = smith.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbAdapter.Name, name);
        contentValues.put(DbAdapter.Email, email);
        contentValues.put(DbAdapter.Password, password);

        long l = database.insert(DbAdapter.TableName, null, contentValues);
        return l;
    }

    //function for the view all button
    String fetchData() {
        SQLiteDatabase database = smith.getWritableDatabase();
        String[] columns = {DbAdapter.id, DbAdapter.Name, DbAdapter.Email, DbAdapter.Password};
        Cursor cursor = database.query(DbAdapter.TableName, columns, null, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbAdapter.id));
            String Name = cursor.getString(cursor.getColumnIndex(DbAdapter.Name));
            String Email = cursor.getString(cursor.getColumnIndex(DbAdapter.Email));
            String Password = cursor.getString(cursor.getColumnIndex(DbAdapter.Password));

            stringBuffer.append(id + " " + Name + "" + Email + "" + Password + "\n");
        }
        String data = stringBuffer.toString();
        return data;
    }


    //function for the sign up page used for login
    Cursor Login(String email, String pass) {
        SQLiteDatabase database = smith.getWritableDatabase();
        String[] c_names = {DbAdapter.id, DbAdapter.Name, DbAdapter.Email, DbAdapter.Password};
        String selection = DbAdapter.Email + " =? and " + DbAdapter.Password + " =? ";
        String[] arg = {email, pass};
        Cursor cursor = database.query(DbAdapter.TableName, c_names, selection, arg, null, null, null, null);
        //Cursor cursor1=database.query()
        return cursor;
    }

    //function used for the deletion of records in database
    int delete(String lat,String lon) {
        SQLiteDatabase database = smith.getWritableDatabase();
        String[] arg = {lat,lon};
        int v = database.delete(DbAdapter.Location_table, DbAdapter.Lat + "=? and "+DbAdapter.Lon+"=?",arg);
        return v;
    }

 /*   int UpdateData(String newV, String oldV) {
        SQLiteDatabase database = smith.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(DbAdapter.Name, newV);
        String[] arg = {oldV};
        int val = database.update(DbAdapter.TableName, contentValues, DbAdapter.Name + "=?", arg);
        return val;
        }*/

    long storeLocations(String lat, String lon) {
        SQLiteDatabase database = smith.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbAdapter.Lat, lat);
        contentValues.put(DbAdapter.Lon, lon);
        long res = database.insert(DbAdapter.Location_table, null, contentValues);

        return res;
    }

    Cursor showLocations() {
        SQLiteDatabase database = smith.getWritableDatabase();
        String[] column = {DbAdapter.location_id, DbAdapter.Lat, DbAdapter.Lon};
        Cursor mycursor = database.query(DbAdapter.Location_table, column, null, null, null, null, null);
        return mycursor;

    }
Cursor fetchSelectiveLocationInfo(String lat,String lon){

    SQLiteDatabase database = smith.getWritableDatabase();
    String[] column = {DbAdapter.location_id, DbAdapter.tile, DbAdapter.desc};
    String selection= DbAdapter.Lat+"=? and "+DbAdapter.Lon+"=?";
    String[] arg={lat,lon};
    Cursor mycursor = database.query(DbAdapter.Location_table, column, selection, arg, null, null, null);
    return mycursor;

}
    int update_info(String tile, String desc, String lat, String lon) {
        SQLiteDatabase database = smith.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbAdapter.tile, tile);
        contentValues.put(DbAdapter.desc, desc);
        String selection = DbAdapter.Lat + "=? and " + DbAdapter.Lon + "=?";
        String[] selection_arg = {lat, lon};
        int res = database.update(DbAdapter.Location_table, contentValues, selection, selection_arg);
        return res;
    }

    public class DbAdapter extends SQLiteOpenHelper {
        private static final String name = "Users";
        private static final int version = 1;
        //Location Table

        private static final String Location_table = "locations";
        static final String location_id = "_id";
        static final String Lat = "latitude";
        static final String Lon = "longitude";
        static final String tile = "title";
        static final String desc = "desc";
        //User Table
        private static final String TableName = "details";
        private static final String id = "_id";
        static final String Name = "name";
        static final String Email = "email";
        static final String Password = "Password";
        static final String createQ = "create table " + TableName + "(" + id + " integer primary key autoincrement," + Name + " varchar(30)," + Email + " varchar(20)," + Password + " varchar(20));";
        private static final String loc_q = "create table " + Location_table + "(" + location_id + " integer primary key autoincrement," + Lat + " varchar(30)," + Lon + " varchar(30)," + tile + " varchar (30)," + desc + " varchar(255));";
        //insert into details values(1,'smith','smithsparadise@gmail.com','qwerty');
        Context context;

        public DbAdapter(Context context) {
            super(context, name, null, version);
            this.context = context;
            // Toast.makeText(context,"Cons called",Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(createQ);
                db.execSQL(loc_q);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}
