package com.example.aula7app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Exe1 extends AppCompatActivity {

    private TextView txtCEP;
    private TextView txtLogradouro;
    private TextView txtComplemento;
    private TextView txtBairro;
    private TextView txtLocalidade;
    private TextView txtUF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe1);

        txtCEP = findViewById(R.id.txtCEP);
        txtLogradouro = findViewById(R.id.txtLogradouro);
        txtComplemento = findViewById(R.id.txtComplemento);
        txtBairro = findViewById(R.id.txtBairro);
        txtLocalidade = findViewById(R.id.txtLocalidade);
        txtUF = findViewById(R.id.txtUF);
    }

    public void onBuscarCep(View view) {
        String cep = txtCEP.getText().toString();
        new HttpAsyncTak().execute(cep);
    }

    private class HttpAsyncTak extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Exe1.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://viacep.com.br/ws/" + params[0] + "/json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int status = urlConnection.getResponseCode();

                if(status == 200) {
                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String inputString;

                    while( (inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }
                    urlConnection.disconnect();
                    return builder.toString();
                }
            } catch (Exception e) {
                Log.e("URL", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result != null) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String logradouro = obj.getString("logradouro");
                    String complemento = obj.getString("complemento");
                    String bairro = obj.getString("bairro");
                    String localidade = obj.getString("localidade");
                    String uf = obj.getString("uf");

                    txtLogradouro.setText(logradouro);
                    txtComplemento.setText(complemento);
                    txtBairro.setText(bairro);
                    txtLocalidade.setText(localidade);
                    txtUF.setText(uf);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}