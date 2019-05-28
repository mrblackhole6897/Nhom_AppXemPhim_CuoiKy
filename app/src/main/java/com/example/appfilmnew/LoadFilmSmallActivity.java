package com.example.appfilmnew;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoadFilmSmallActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    ListView listView;
    TextView textView;
    String  valueHinh;
    String  thongtin;
     String  value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_film_small);
        program.xemFilm = true;
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        Intent intent = getIntent();
        value = intent.getStringExtra("key_1").trim();
        valueHinh = intent.getStringExtra("key_hinh").trim();
          thongtin = intent.getStringExtra("key_Thongtin").trim();

        int tap=1;
        final String temp = "ma="+value+"&stt="+tap;


        addControls();

        String path = "sdcard/photo/"+valueHinh;
        imageView.setImageDrawable(Drawable.createFromPath(path));
        textView.setText(thongtin);
        String temp2 = "ma="+value;
        FilmTask filmTask = new  FilmTask();
        filmTask.execute(temp2);
        boolean checkFilm = false;
        if(program.dsFilmDaMua != null)
        {
        for(int i =0; i <program.dsFilmDaMua.length;i++)
        {
            if(value.equals(program.dsFilmDaMua[i]))
            {
                checkFilm = true;
                break;
            }
        }
        }
        if(checkFilm)
        {
            button.setText("Xem Ngay");
        }
        else
        {
            button.setText("Mua Ngay");
        }
        final boolean finalCheckFilm = checkFilm;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalCheckFilm) {
                    FilmNoiBatTask filmNoiBatTask = new FilmNoiBatTask();
                    filmNoiBatTask.execute(temp);
                }
                else
                {
                    if(program.thongtin==null)
                    {
                        Intent intent = new Intent(LoadFilmSmallActivity.this, DangNhapActivity.class);
                        startActivity(intent);


                    }
                    else
                    {
                        if(program.thongtin.getCoin() > 5)
                        {
                            String tempUpdate = "id="+program.thongtin.getId().trim()+"&newFilm="+value;
                            UpdateMuaFilmTask update = new UpdateMuaFilmTask();
                            update.execute(tempUpdate);
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoadFilmSmallActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View vi = inflater.inflate(R.layout.nap_coin, null);
                            final TextView thongTin = (TextView) vi.findViewById(R.id.textViewThongTinCoin);
                            final Button logout1 = (Button) vi.findViewById(R.id.buttonThoaiCoin);


                            String temp = "-Lưu ý:" +
                                    "\n1.Sử dụng ứng dụng MoMo trên di dộng" +
                                    "\n2.Vào mục Thanh Toán từ MoMo App" +
                                    "\n3.Hướng Camera vào mã QRCODE đã chọn" +
                                    "\n4.Thêm Tài Khoảng Nhận Xu Vào Phần Tin Nhắn" +
                                    "\n5.Chụp Lại Màn Hình Khi Giao Dịch Hoàn Thành" +
                                    "\n6.Vào Facebook: https://www.facebook.com/TuanHandSomeBoy" +
                                    "\n7.Và Gửi Yêu Cầu Cộng Xu";


                            thongTin.setText(temp);
                            builder.setCancelable(true);
                            builder.setView(vi);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            logout1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View vi) {

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            program.xemFilm =false;
            finish();
        return super.onOptionsItemSelected(item);
    }

    void addControls()
    {

     imageView = findViewById(R.id.imageView2);
        listView = findViewById(R.id.dsTapFilm);
        textView = findViewById(R.id.ThongTinFilm);
    button =findViewById(R.id.buttonXemFilm);
    }

    private class FilmNoiBatTask extends AsyncTask<String,Void, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            String kq="";
            try {
                URL url = new URL(program.ip+"GetUrlFilmByIdFilm");
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

                NodeList dsNode = document.getElementsByTagName("string");
                kq = dsNode.item(0).getTextContent();

            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            return kq;
        }





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(LoadFilmSmallActivity.this, LoadFilmBigActivity.class);
            intent.putExtra("key_2",s);
            startActivity(intent);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    private class FilmTask extends AsyncTask<String,Void, ArrayList<Film>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Film> ct_films) {
            super.onPostExecute(ct_films);
            List<PlatfForm> platfForms = new ArrayList<PlatfForm>();
            PlatfForm platfForm  = new PlatfForm(ct_films,"DS Film");

            platfForms.add(platfForm);
            PlatfFormAdapter adapter1 = new PlatfFormAdapter(LoadFilmSmallActivity.this, R.layout.listview_my_layout, platfForms);

            listView.setAdapter(adapter1);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Film> doInBackground(String... strings) {
          ArrayList<Film> ds = new ArrayList<>();
                      try {
                URL url = new URL(program.ip+"Ct_Film");
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

                NodeList dsNode = document.getElementsByTagName("Ct_Film");
                          for (int i=0;i< dsNode.getLength(); i++)
                          {
                              Element nodeFilm = (Element) dsNode.item(i);
                              String stt = nodeFilm.getElementsByTagName("STT").item(0).getTextContent();
                              String id = nodeFilm.getElementsByTagName("IdFilm").item(0).getTextContent();
                              String urlFilm = nodeFilm.getElementsByTagName("UrlFilm").item(0).getTextContent().toString();


                              Film film = new Film();
                              film.setTen(stt);
                              film.setMa(id);
                              film.setUrlHinh(urlFilm);
                              film.setNameHinh(valueHinh);

                              ds.add(film);
                          }


            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            return ds;
        }
    }

    private class UpdateMuaFilmTask extends AsyncTask<String,Void, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            String kq="";
            try {
                URL url = new URL(program.ip+"updateFilmDaMua");
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

                NodeList dsNode = document.getElementsByTagName("string");
                kq = dsNode.item(0).getTextContent();

            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            return kq;
        }





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


                  program.thongtin.setFilmDaMua(s);
                  program.thongtin.setCoin(program.thongtin.getCoin()-5);
            program.dsFilmDaMua = program.thongtin.getFilmDaMua().trim().split(";");
                  Intent intent = new Intent(LoadFilmSmallActivity.this, LoadFilmSmallActivity.class);
                  intent.putExtra("key_1", value);
                intent.putExtra("key_hinh", valueHinh);


                intent.putExtra("key_Thongtin", thongtin);
                  startActivity(intent);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
