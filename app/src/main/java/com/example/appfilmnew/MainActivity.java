package com.example.appfilmnew;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {


    ProgressBar pbXuLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }


        FilmNoiBatTask filmNoiBatTask = new FilmNoiBatTask();
        filmNoiBatTask.execute();


    }

    private void addEvents() {
        int current = pbXuLy.getProgress();
        pbXuLy.setProgress(current + 20);
    }

    private void addControls() {
        pbXuLy = findViewById(R.id.progressBar2);

    }



        private class FilmNoiBatTask extends AsyncTask<Void, Void, ArrayList<Film>> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(ArrayList<Film> films) {
                super.onPostExecute(films);
                DownLoadAsyncTask down = new DownLoadAsyncTask();
                down.execute(films);





            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected ArrayList<Film> doInBackground(Void... voids) {
                ArrayList<Film> dsFilm = new ArrayList<>();
                try {
                    URL url = new URL(program.ip+"Films");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(connection.getInputStream());
                    NodeList dsNode = document.getElementsByTagName("Film");
                    for (int i = 0; i < dsNode.getLength(); i++) {
                        Element nodeFilm = (Element) dsNode.item(i);
                        String ma = nodeFilm.getElementsByTagName("Ma").item(0).getTextContent();
                        String Ten = nodeFilm.getElementsByTagName("Ten").item(0).getTextContent();
                        String IdTheLoai = nodeFilm.getElementsByTagName("IdTheLoai").item(0).getTextContent();
                        String IdQuocGia = nodeFilm.getElementsByTagName("IdQuocGia").item(0).getTextContent();
                        String DienVien = nodeFilm.getElementsByTagName("DienVien").item(0).getTextContent();
                        String DaoDien = nodeFilm.getElementsByTagName("DaoDien").item(0).getTextContent();
                        String NoiDung = nodeFilm.getElementsByTagName("NoiDung").item(0).getTextContent();
                        int SoTap = Integer.parseInt(nodeFilm.getElementsByTagName("SoTapDangChieu").item(0).getTextContent());
                        String UrlHinh = nodeFilm.getElementsByTagName("UrlHinh").item(0).getTextContent();
                        String NameHinh = nodeFilm.getElementsByTagName("NameHinh").item(0).getTextContent().trim();
                        Film film = new Film();
                        film.setMa(ma);
                        film.setTen(Ten);
                        film.setIdTheLoai(IdTheLoai);
                        film.setIdQuocGia(IdQuocGia);
                        film.setDienVien(DienVien);
                        film.setDaoDien(DaoDien);
                        film.setNoiDung(NoiDung);
                        film.setSoTap(SoTap);
                        film.setUrlHinh(UrlHinh);
                        film.setNameHinh(NameHinh);
                        dsFilm.add(film);
                    }
                } catch (Exception e) {
                    Log.e("Loi", e.toString());
                }
                return dsFilm;
            }
        }
    class DownLoadAsyncTask extends AsyncTask<ArrayList<Film>, Integer, String> {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(ArrayList<Film>... arrayLists)
        {
            String path;
            String name;
            for(int i =0; i<arrayLists[0].size();i++) {
                path = arrayLists[0].get(i).getUrlHinh();
                 name = arrayLists[0].get(i).getNameHinh();

                int length = 0;
                try {
                    URL url = new URL(path);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.connect();
                    length = urlConnection.getContentLength();
                    File new_folder = new File("sdcard/photo");
                    if (!new_folder.exists()) {
                        new_folder.mkdir();
                    }
                    File inPut = new File(new_folder, name);
                    InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                    byte[] data = new byte[1024];
                    int total = 0, count = 0;
                    OutputStream outputStream = new FileOutputStream(inPut);
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        outputStream.write(data, 0, count);
                        int propress = total * 100 / length;
                        publishProgress(propress);
                    }
                    inputStream.close();
                    outputStream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Down Xong";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
           // progressDialog.setMax(100);
          //  progressDialog.setProgress(0);
            progressDialog.setTitle("vui lòng chờ trong giây lát...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.hide();
            Intent intent = new Intent(MainActivity.this, LoadDanhSachActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }
    }


