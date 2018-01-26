package com.example.sehorva.kolokvij;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sehorva on 1/26/18.
 */

public class DBAdapter {

    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";

    //za tablicu ZAPOSLENIK(ID, IME, MAIL, PLACA
    static final String DATABASE_TABLE_ZAPOSLENIK = "zaposlenik";
    static final String KEY_ROWID = "id"; //_id
    static final String KEY_IME = "ime";
    static final String KEY_MAIL = "mail";
    static final String KEY_PLACA = "mail";

    //za tablicu RADNOMJESTO(ID_MJESTA, NIVO_ODGOVORNOSTI, MINIMALNA_PLACA
    static final String DATABASE_TABLE_RADNOMJESTO = "radnomjesto";
    static final String KEY_ROWID_MJESTA = "id_mjesta";
    static final String KEY_NIVO_ODGOVORNOSTI = "nivo_odgovornosti";
    static final String KEY_MINIMALNA_PLACA = "minimalna_placa";


    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE_ZAPOSLENIK =
            "create table zaposlenik (id integer primary key autoincrement, "
                    + "ime text not null, placa integer not null);";

    static final String DATABASE_CREATE_RADNOMJESTO =
            "create table radnomjesto (id_mjesta integer primary key autoincrement, "
                    + "nivo_odgovornosti integer not null, minimalna_placa integer not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_ZAPOSLENIK);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                db.execSQL(DATABASE_CREATE_RADNOMJESTO);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS zaposlenik");
            db.execSQL("DROP TABLE IF EXISTS radnomjesto");
            onCreate(db);
        }
    }

    //---otvara bazu podataka---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---zatvara bazu podataka---
    public void close()
    {
        DBHelper.close();
    }

    //---ubacuje zaposlenika u tablicu zaposlenik u bazi podataka---
    public long insertIntoZaposlenik(String ime, String mail, Integer placa)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IME, ime);
        initialValues.put(KEY_MAIL, mail);
        return db.insert(DATABASE_TABLE_ZAPOSLENIK, null, initialValues);
    }

    //---ubacuje radno mjesto u tablicu rednomjesto u bazi podataka---
    public long insertIntoRadnoMjesto(Integer nivo_odgovornosti, Integer minimalna_placa)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NIVO_ODGOVORNOSTI, nivo_odgovornosti);
        initialValues.put(KEY_MINIMALNA_PLACA, minimalna_placa);
        return db.insert(DATABASE_TABLE_RADNOMJESTO, null, initialValues);
    }

    //---brise odredjenog zaposlenika--
    public boolean deleteZaposlenik(long rowId)
    {
        return db.delete(DATABASE_TABLE_ZAPOSLENIK, KEY_ROWID + "=" + rowId, null) > 0;

    }

    //---brise odredjeno radno mjesto--
    public boolean deleteRadnoMjesto(long rowId)
    {
        return db.delete(DATABASE_TABLE_RADNOMJESTO, KEY_ROWID_MJESTA + "=" + rowId, null) > 0;
    }
/*
    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_EMAIL}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_EMAIL}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateContact(long rowId, String name, String email)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

*/
}
