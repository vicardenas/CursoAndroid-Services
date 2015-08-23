package com.victor.cursoandroid_services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private ProgressBar progressBar;
    private TextView txtProgress;
    private Button btnIniciar;
    private Button btnDetener;
    private Button btnIniciarDialogo;

    private ProgressDialog progressDialog;

    private TareaAsincrona task;
    private TareaAsincronaDialog task2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnDetener = (Button) findViewById(R.id.btnDetener);
        btnIniciarDialogo = (Button) findViewById(R.id.btnIniciarDialogo);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new TareaAsincrona();
                task.execute();
            }
        });

        btnDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
            }
        });

        btnIniciarDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Procesando...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.setMax(100);

                task2 = new TareaAsincronaDialog();
                task2.execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void tareaLarga() {
        try {
            Thread.sleep(50);
        } catch(InterruptedException e) {}
    }

    public class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setMax(100);
            progressBar.setProgress(0);
            txtProgress.setText("0");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 1; i <= 100; i++) {
                tareaLarga();
                publishProgress(i);
                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0].intValue();
            progressBar.setProgress(progress);
            txtProgress.setText(Integer.toString(progress));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(MainActivity.this, "Tarea finalizada", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada por el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    public class TareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    TareaAsincronaDialog.this.cancel(true);
                }
            });
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 1; i <= 100; i++) {
                tareaLarga();
                publishProgress(i);
                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0].intValue();
            progressDialog.setProgress(progress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(MainActivity.this, "Tarea finalizada", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada por el usuario", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
