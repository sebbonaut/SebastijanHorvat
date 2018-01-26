package com.example.sehorva.kolokvij;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Console;

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
    static final String KEY_PLACA = "placa";

    //za tablicu RADNOMJESTO(ID_MJESTA, NIVO_ODGOVORNOSTI, MINIMALNA_PLACA
    static final String DATABASE_TABLE_RADNOMJESTO = "radnomjesto";
    static final String KEY_ROWID_MJESTA = "id_mjesta";
    static final String KEY_NIVO_ODGOVORNOSTI = "nivo_odgovornosti";
    static final String KEY_MINIMALNA_PLACA = "minimalna_placa";


    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE_ZAPOSLENIK =
            "create table zaposlenik (id integer primary key autoincrement, "
                    + "ime text not null, mail text not null, placa integer not null);";

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
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_ZAPOSLENIK);
            db.execSQL(DATABASE_CREATE_RADNOMJESTO);
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
        Log.d("insert", ime+mail+placa);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IME, ime);
        initialValues.put(KEY_MAIL, mail);
        initialValues.put(KEY_PLACA, placa);
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

    //---vraca sve zaposlenike---
    public Cursor getSviZaposlenici()
    {
        return db.query(DATABASE_TABLE_ZAPOSLENIK, new String[] {KEY_ROWID, KEY_IME,
                KEY_MAIL, KEY_PLACA}, null, null, null, null, null);
    }

    //---vraca sva radna mjesta---
    public Cursor getSvaRadnaMjesta()
    {
        return db.query(DATABASE_TABLE_RADNOMJESTO, new String[] {KEY_ROWID_MJESTA, KEY_NIVO_ODGOVORNOSTI,
                KEY_MINIMALNA_PLACA}, null, null, null, null, null);
    }


    //---vraca odredjenog zaposlenika---
    public Cursor getZaposlenik(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_ZAPOSLENIK, new String[] {KEY_ROWID,
                                KEY_IME, KEY_MAIL, KEY_PLACA}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---vraca odredjeno radno mjesto---
    public Cursor getRadnoMjesto(long rowId_mjesta) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_RADNOMJESTO, new String[] {KEY_ROWID_MJESTA,
                                KEY_NIVO_ODGOVORNOSTI, KEY_MINIMALNA_PLACA}, KEY_ROWID_MJESTA + "=" + rowId_mjesta, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---azuriramo (updateamo) zaposlenika---
    public boolean updateZaposlenik(long rowId, String ime, String mail, Integer placa)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_IME, ime);
        args.put(KEY_MAIL, mail);
        args.put(KEY_PLACA, placa);
        return db.update(DATABASE_TABLE_ZAPOSLENIK, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---azuriramo (updateamo) radno mjesto---
    public boolean updateRadnoMjesto(long rowId_mjesta, Integer nivo_odgovornosti, Integer minimalna_placa)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NIVO_ODGOVORNOSTI, nivo_odgovornosti);
        args.put(KEY_MINIMALNA_PLACA, minimalna_placa);
        return db.update(DATABASE_TABLE_RADNOMJESTO, args, KEY_ROWID_MJESTA + "=" + rowId_mjesta, null) > 0;
    }

}
