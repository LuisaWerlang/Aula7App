package com.example.aula7app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.mime.Header;

public class Exe2 extends AppCompatActivity {

    private TextView txt_media_temperatura;
    private TextView txt_media_umidade;
    private TextView txt_media_ponto_orvalho;
    private TextView txt_media_pressao;
    private ListView dados;

    String de[] = {"temperatura", "umidade", "orvalho", "pressao"};
    int para[] = {R.id.temperatura, R.id.umidade, R.id.ponto_orvalho, R.id.pressao};
    List<Map<String, String>> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe2);

        txt_media_temperatura = findViewById(R.id.media_temperatura);
        txt_media_umidade = findViewById(R.id.media_umidade);
        txt_media_ponto_orvalho = findViewById(R.id.media_ponto_orvalho);
        txt_media_pressao = findViewById(R.id.media_pressao);
        dados = findViewById(R.id.dados);
    }

    public void onBuscarDados(View view) {
        lista = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ghelfer.net/la/weather.json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String data = new String(responseBody);
                try {
                    loadData(data);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void loadData(String data) throws JSONException {
        double somaTemp = 0;
        double somaUmid = 0;
        double somaOrv = 0;
        double somaPres = 0;

        JSONObject obj = new JSONObject(data);
        JSONArray array = obj.getJSONArray("weather");

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            String temperatura = json.get("temperature").toString();
            String umidade = json.get("humidity").toString();
            String ponto_orvalho = json.get("dewpoint").toString();
            String pressao = json.get("pressure").toString();

            somaTemp += Double.parseDouble(temperatura);
            somaUmid += Double.parseDouble(umidade);
            somaOrv += Double.parseDouble(ponto_orvalho);
            somaPres += Double.parseDouble(pressao);

            Map<String, String> mapa = new HashMap<>();
            mapa.put("temperatura", temperatura);
            mapa.put("umidade", umidade);
            mapa.put("orvalho", ponto_orvalho);
            mapa.put("pressao", pressao);
            lista.add(mapa);
        }

        txt_media_temperatura.setText(String.valueOf(somaTemp / array.length()));
        txt_media_umidade.setText(String.valueOf(somaUmid / array.length()));
        txt_media_ponto_orvalho.setText(String.valueOf(somaOrv / array.length()));
        txt_media_pressao.setText(String.valueOf(somaPres / array.length()));

        SimpleAdapter adapter = new SimpleAdapter(this, lista, R.layout.linha_tempo, de, para);
        dados.setAdapter(adapter);
    }
}