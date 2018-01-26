package com.example.sehorva.kolokvij;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sehorva on 1/26/18.
 */

public class MyService extends Service {

    //za timer:
    int counter = 0;

    String skinutTekst = new String("");
    int polozajUTekstu = 0;
    boolean tekstSkinut = false;


    static final int UPDATE_INTERVAL = 1500;
    private Timer timer = new Timer();
    // do tuda za timer

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ovaj servis radi dok ga se ne zaustavi eksplicitno
        // dakle vraca sticky

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //new DownloadTextTask().execute("http://humanstxt.org/humans.txt");
        new DownloadTextTask().execute("https://web.math.pmf.unizg.hr/~karaga/android/images_2/predefiniranekonstante.txt");
        //za timer:
        doSomethingRepeatedly();


        return START_STICKY;
    }

    //za timer
    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                //Log.d("MyService", String.valueOf(++counter));
                String[] tekst = skinutTekst.split("\\s+");
                if (polozajUTekstu < tekst.length)
                {
                    Log.d("MyService", tekst[polozajUTekstu]);
                    ++polozajUTekstu;
                }
                else if(tekstSkinut == true)
                {
                    //onDestroy();
                    stopSelf();
                }
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //za timer
        if (timer != null){
            timer.cancel();

        }
        Log.d("MyService", "Service zaustavljen");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String DownloadText(String URL)
    {
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        int BUFFER_SIZE = 2000;
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                skinutTekst += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "Tekst downloadan.", Toast.LENGTH_SHORT).show();
            tekstSkinut = true;
        }
    }


}


