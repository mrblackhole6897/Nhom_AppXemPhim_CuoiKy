package com.example.appfilmnew;

import android.app.AlertDialog;
import android.content.Intent;
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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DangNhapActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    EditText id, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
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
        button = findViewById(R.id.dangnhap);
        textView = findViewById(R.id.dangki);
        id = findViewById(R.id.iddangnhap);
        pass = findViewById(R.id.passdangnhap);
    }
    void addEvents()
    {

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String temp ="id="+id.getText()+"&pass="+pass.getText();
               FilmNoiBatTask filmNoiBatTask = new FilmNoiBatTask();
               filmNoiBatTask.execute(temp);
           }
       });
       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(DangNhapActivity.this, DangKiActivity.class);
               startActivity(intent);
           }
       });
    }

    private class FilmNoiBatTask extends AsyncTask<String, Void, ThongTinUser> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ThongTinUser thongTinUser) {
            super.onPostExecute(thongTinUser);
            if(thongTinUser.getId()!=null) {
                program.thongtin = thongTinUser;
                program.dsFilmDaMua = thongTinUser.getFilmDaMua().trim().split(";");

                onBackPressed();
            }
            else
            {

                final AlertDialog.Builder builder=new AlertDialog.Builder(DangNhapActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.loi,null);
                final TextView thongTin=(TextView) v.findViewById(R.id.textViewLoi);
                final Button loi=(Button) v.findViewById(R.id.buttonloi);


                String  temp ="Đăng Nhập Không Thành Công. Vui Lòng Kiểm Tra Lại!!";
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
        protected ThongTinUser doInBackground(String... strings) {
            ThongTinUser thongTinUser = new ThongTinUser();
            try{
            URL url = new URL(program.ip+"DangNhap");
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
            NodeList dsNode = document.getElementsByTagName("ThongTinUser");

           Element element = (Element) dsNode.item(0);
           String Id = element.getElementsByTagName("Id").item(0).getTextContent();
           String Ten = element.getElementsByTagName("Ten").item(0).getTextContent();
           String Email = element.getElementsByTagName("Email").item(0).getTextContent();
           String FilmDaMua = element.getElementsByTagName("FilmDaMua").item(0).getTextContent();


           int Coin =Integer.parseInt(element.getElementsByTagName("Coin").item(0).getTextContent());
           int row = Integer.parseInt(element.getElementsByTagName("row").item(0).getTextContent());


           thongTinUser.setId(Id);
           thongTinUser.setTen(Ten);
           thongTinUser.setEmail(Email);
           thongTinUser.setFilmDaMua(FilmDaMua);
           thongTinUser.setCoin(Coin);
           thongTinUser.setRow(row);

        } catch (Exception e) {
            Log.e("Loi", e.toString());
        }
            return thongTinUser;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
