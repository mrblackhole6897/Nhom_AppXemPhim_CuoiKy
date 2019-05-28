package com.example.appfilmnew;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DangKiActivity extends AppCompatActivity {

    EditText idDangki,email,ten,passdangki;
    Button thoatdangki,xacnhandangki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        addControls();
        addEvents();
    }
    void addControls()
    {
        idDangki = findViewById(R.id.idDangki);
        email = findViewById(R.id.email);
        ten = findViewById(R.id.ten);
        passdangki = findViewById(R.id.passdangki);
        thoatdangki = findViewById(R.id.thoatdangki);
        xacnhandangki = findViewById(R.id.xacnhandangki);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    void addEvents()
    {
        xacnhandangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String temp ="id="+idDangki.getText();
                checkMaTask checkmaTask = new checkMaTask();
                checkmaTask.execute(temp);
            }
        });

        thoatdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class FilmNoiBatTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s=="true")
            {
                final AlertDialog.Builder builder=new AlertDialog.Builder(DangKiActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.loi,null);
                final TextView thongTin=(TextView) v.findViewById(R.id.textViewLoi);
                final Button loi=(Button) v.findViewById(R.id.buttonloi);


                String  temp ="Đăng Kí Thành Công";
                thongTin.setText(temp);
                builder.setCancelable(true);
                builder.setView(v);
                final AlertDialog dialog=builder.create();
                dialog.show();
                loi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                final AlertDialog.Builder builder=new AlertDialog.Builder(DangKiActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.loi,null);
                final TextView thongTin=(TextView) v.findViewById(R.id.textViewLoi);
                final Button loi=(Button) v.findViewById(R.id.buttonloi);


                String  temp ="Đăng Kí Không Thành Công. Vui Lòng Kiểm Tra Lại!!";
                thongTin.setText(temp);
                builder.setCancelable(true);
                builder.setView(v);
                final AlertDialog dialog=builder.create();
                dialog.show();
                loi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String kq="";
            try{
                URL url = new URL(program.ip+"DangKi");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(strings[0]);
                writer.flush();
                writer.close();
                os.close();

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("boolean");
                kq = dsNode.item(0).getTextContent();


            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            return kq;
        }
    }
    private class checkMaTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s =="true")
            {
                final AlertDialog.Builder builder=new AlertDialog.Builder(DangKiActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.loi,null);
                final TextView thongTin=(TextView) v.findViewById(R.id.textViewLoi);
                final Button loi=(Button) v.findViewById(R.id.buttonloi);


                String  temp ="Mã Đã Tồn Tại. Vui Lòng Kiểm Tra Lại!!";
                thongTin.setText(temp);
                builder.setCancelable(true);
                builder.setView(v);
                final AlertDialog dialog=builder.create();
                dialog.show();
                loi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                final String temp ="id="+idDangki.getText()+"&pass="+passdangki.getText()+"&ten="+ten.getText()+"&email="+email.getText()+"&row=2"+"&coin=0";
                FilmNoiBatTask filmNoiBatTask = new FilmNoiBatTask();
                filmNoiBatTask.execute(temp);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String kq="";
            try{
                URL url = new URL(program.ip+"checkId");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(strings[0]);
                writer.flush();
                writer.close();
                os.close();

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("boolean");
                kq = dsNode.item(0).getTextContent();


            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            return kq;
        }
    }

}
