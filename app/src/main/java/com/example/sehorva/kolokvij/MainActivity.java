package com.example.sehorva.kolokvij;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBAdapter db2 = new DBAdapter(this);

        //---dodavanje zaposlenika---
        db2.open();
        long id = db2.insertIntoZaposlenik("Mirko", "mirko34@gmail.com", 2500);
       // id = db.insertIntoZaposlenik("Janko", "janac@hotmail.com", 3560);
       // id = db.insertIntoZaposlenik("Ivana", "ivana.maric@gmail.com", 5000);
       // id = db.insertIntoZaposlenik("Branko", "brahg@hotmail.com", 2800);
       // id = db.insertIntoZaposlenik("Hrvoje", "hrvojko123@gmail.com", 4200);
        db2.close();
/*
        //---dodavanje radnih mjesta---
        db.open();
        long id_mjesta = db.insertIntoRadnoMjesto(1, 2800);
        id_mjesta = db.insertIntoRadnoMjesto(5, 6000);
        id_mjesta = db.insertIntoRadnoMjesto(3, 4600);
        id_mjesta = db.insertIntoRadnoMjesto(4, 9000);
        id_mjesta = db.insertIntoRadnoMjesto(2, 3400);
        db.close();
*/

        //--dohvati sve zaposlenike---
        /*db.open();
        Cursor c = db.getSviZaposlenici();
        if (c.moveToFirst())
        {
            do {
                DisplayZaposlenika(c);
            } while (c.moveToNext());
        }
        db.close();*/

        //--dohvati sva radna mjesta---
        /*db.open();
        Cursor c2 = db.getSvaRadnaMjesta();
        if (c2.moveToFirst())
        {
            do {
                DisplayRadnogMjesta(c2);
            } while (c2.moveToNext());
        }
        db.close();*/


/*
        //---get a contact---
        db.open();
        Cursor cu = db.getContact(2);
        if (cu.moveToFirst())
            DisplayContact(cu);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();



        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();



        //---delete a contact---
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();*/
    }

    private void DisplayZaposlenika(Cursor c) {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Ime" + c.getString(1) + "\n" +
                        "Mail:  " + c.getString(2) + "\n" +
                        "Placa: " + c.getString(3),
                Toast.LENGTH_LONG).show();
    }

    private void DisplayRadnogMjesta(Cursor c) {
        Toast.makeText(this,
                "id_mjesta: " + c.getString(0) + "\n" +
                        "Nivo_odgovornosti: " + c.getString(1) + "\n" +
                        "Minimalna_placa:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}
