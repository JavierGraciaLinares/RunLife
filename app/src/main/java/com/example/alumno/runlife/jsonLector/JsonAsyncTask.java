package com.example.alumno.runlife.jsonLector;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.alumno.runlife.Carrera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javi on 30/05/2018.
 */

public class JsonAsyncTask extends AsyncTask<Void, Carrera, Void> {
    public static final String OK = "OK";
    private static final String STATUS = "status";
    private static final String CARRERAS = "carreras";
    private static final String FECHA = "fecha";
    private static final String HORA = "hora";
    private static final String LUGAR = "lugar";
    private static final String NOMBRE = "nombre";
    private static final String DESCRIPCION = "descripcion";
    private static final String DISTANCIA = "distancia";
    private static final String ENLACE = "enlace";

    private IJsonCarrera iJsonCarrera;

    public JsonAsyncTask(IJsonCarrera iJsonCarrera) {
        this.iJsonCarrera = iJsonCarrera;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StringBuffer result = new StringBuffer();
        String line;
        try {

            //Acemos la conexion que nos devolverá el JSON
            URL aURL = new URL("https://testrunlife.firebaseapp.com/JSON/jsoncarreras.json");
            HttpURLConnection aConnection = (HttpURLConnection) aURL.openConnection();
            aConnection.setRequestMethod("GET");

            //Cargamos el JSON
            BufferedReader aBufferedReader = new BufferedReader(new InputStreamReader(aConnection.getInputStream()));
            while ((line = aBufferedReader.readLine()) != null) {
                result.append(line);
            }
            aConnection.disconnect();

            JSONObject anObject = new JSONObject(result.toString());

            if (anObject.getString(STATUS).equals(OK)) {
                JSONArray resultArray = anObject.getJSONArray(CARRERAS);
                for (int i = 0; i < resultArray.length(); i++) {
                    try {
                        JSONObject carreraJson = resultArray.getJSONObject(i);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fecha = simpleDateFormat.parse(carreraJson.getString(FECHA));
                        String hora = carreraJson.getString(HORA);
                        String lugar = carreraJson.getString(LUGAR);
                        String nombre = carreraJson.getString(NOMBRE);
                        String descripcion = carreraJson.getString(DESCRIPCION);
                        String distancia = carreraJson.getString(DISTANCIA);
                        Uri enlace = Uri.parse(carreraJson.getString(ENLACE));
                        publishProgress(new Carrera(fecha,hora,lugar,nombre,descripcion,distancia,enlace));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Carrera... values) {
        super.onProgressUpdate(values);
        iJsonCarrera.addCarreraJson(values[0]);
    }
}
