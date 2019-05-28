package com.example.appfilmnew;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoadDanhSachActivity extends AppCompatActivity {
    SearchView searchView;
    BottomNavigationView bottomNav;
    String[] imageListViewPager;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    ListView listView;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_danh_sach);


        addControl();
    }

    private void XuLyViewpager(ArrayList<Film> info) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, info);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            LoadDanhSachActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }

    private void addControl() {
        viewPager = findViewById(R.id.viewpagerds);
        sliderDotspanel = findViewById(R.id.SliderDots);
        listView = findViewById(R.id.listview);
        bottomNav = findViewById(R.id.bottomNavView_Bar);
        bottomNav.setOnNavigationItemSelectedListener(listener);
        TrangChuTask trangChuTask = new TrangChuTask();
        trangChuTask.execute();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.ic_taikhoan: {
                            if (program.thongtin == null) {
                                Intent intent = new Intent(LoadDanhSachActivity.this, DangNhapActivity.class);
                                startActivity(intent);
                            } else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(LoadDanhSachActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View v = inflater.inflate(R.layout.thongtin_taikhoan, null);
                                final TextView thongTin = (TextView) v.findViewById(R.id.textViewThongTin);
                                final Button coin = (Button) v.findViewById(R.id.buttonCoin);
                                final Button thoat = (Button) v.findViewById(R.id.buttonThoat);
                                final Button logout = (Button) v.findViewById(R.id.buttonLogout);

                                String temp = "Tên: " + program.thongtin.getTen() +
                                        "\nEmail: " + program.thongtin.getEmail() +
                                        "\nCoin: " + program.thongtin.getCoin() +
                                        "\nDanh Sach Film Đã Mua:";
                                thongTin.setText(temp);
                                builder.setCancelable(true);
                                builder.setView(v);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                                thoat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                logout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        program.thongtin = null;
                                        dialog.dismiss();
                                        Intent intent = new Intent(LoadDanhSachActivity.this, LoadDanhSachActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                coin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(LoadDanhSachActivity.this);
                                        LayoutInflater inflater = getLayoutInflater();
                                        View vi = inflater.inflate(R.layout.nap_coin, null);
                                        final TextView thongTin = (TextView) vi.findViewById(R.id.textViewThongTinCoin);
                                        final Button logout = (Button) vi.findViewById(R.id.buttonThoaiCoin);


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
                                        logout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                            break;
                        case R.id.ic_theloai: {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoadDanhSachActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View v = inflater.inflate(R.layout.the_loai, null);

                            final Button haiHuoc = (Button) v.findViewById(R.id.button_haiHuoc);
                            final Button kinhDi = (Button) v.findViewById(R.id.button_kinhDi);
                            final Button kiemHiep = (Button) v.findViewById(R.id.button_kiemHiep);
                            final Button hanhDong = (Button) v.findViewById(R.id.button_hanhDong);


                            builder.setCancelable(true);
                            builder.setView(v);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            haiHuoc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyTheLoaiTask dsbyTheLoai = new dsbyTheLoaiTask();
                                    dsbyTheLoai.execute("idTheLoai=1");
                                }
                            });
                            kinhDi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyTheLoaiTask dsbyTheLoai = new dsbyTheLoaiTask();
                                    dsbyTheLoai.execute("idTheLoai=2");
                                }
                            });
                            kiemHiep.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyTheLoaiTask dsbyTheLoai = new dsbyTheLoaiTask();
                                    dsbyTheLoai.execute("idTheLoai=3");
                                }
                            });
                            hanhDong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyTheLoaiTask dsbyTheLoai = new dsbyTheLoaiTask();
                                    dsbyTheLoai.execute("idTheLoai=4");
                                }
                            });
                        }
                            break;
                        case R.id.ic_quocgia:
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoadDanhSachActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View v = inflater.inflate(R.layout.quoc_gia, null);

                            final Button vn = (Button) v.findViewById(R.id.button_vn);
                            final Button trungQuoc = (Button) v.findViewById(R.id.button_trungQuoc);
                            final Button my = (Button) v.findViewById(R.id.button_my);



                            builder.setCancelable(true);
                            builder.setView(v);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            vn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyQuocGiaTask dsbyQuocGia = new dsbyQuocGiaTask();
                                    dsbyQuocGia.execute("idQuocGia=1");
                                }
                            });
                            trungQuoc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyQuocGiaTask dsbyQuocGia = new dsbyQuocGiaTask();
                                    dsbyQuocGia.execute("idQuocGia=2");
                                }
                            });
                            my.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dsbyQuocGiaTask dsbyQuocGia = new dsbyQuocGiaTask();
                                    dsbyQuocGia.execute("idQuocGia=3");
                                }
                            });

                        }
                        break;

                        case R.id.ic_home:
                        {
                            TrangChuTask home = new  TrangChuTask();
                            home.execute();
                        }
                        break;
                    }
                    return true;
                }
            };

    private class TrangChuTask extends AsyncTask<Void, Void, List<PlatfForm>> {

        @Override
        protected List<PlatfForm> doInBackground(Void... voids) {
            List<PlatfForm> list = new ArrayList<>();
            ArrayList<Film> dsFilm = new ArrayList<>();
            try {
                URL url = new URL(program.ip + "Films");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("Film");
                dsFilm = getdsFilm(dsNode);

            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            PlatfForm pf = new PlatfForm(dsFilm, "Danh Sach Film");

            list.add(pf);
            dsFilm = new ArrayList<>();
            try {
                URL url = new URL(program.ip + "TopFilmNew");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("Film");
                dsFilm = getdsFilm(dsNode);

            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            pf = new PlatfForm(dsFilm, "Top Film");

            list.add(pf);
            dsFilm = new ArrayList<>();
            try {
                URL url = new URL(program.ip + "TopFilmView");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("Film");
                dsFilm = getdsFilm(dsNode);

            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }
            pf = new PlatfForm(dsFilm, "Top View Film");

            list.add(pf);
            return list;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<PlatfForm> platfForms) {
            super.onPostExecute(platfForms);
            PlatfForm films = platfForms.get(1);
            program.dsFilm = new ArrayList<>();
            program.dsFilm = films.infos;
            imageListViewPager = new String[films.infos.size()];
            for (int i = 0; i < films.infos.size(); i++) {
                String a = films.infos.get(i).getNameHinh();
                imageListViewPager[i] = a;
            }

            XuLyViewpager(films.infos);


            PlatfFormAdapter adapter1 = new PlatfFormAdapter(LoadDanhSachActivity.this, R.layout.listview_my_layout, platfForms);

            listView.setAdapter(adapter1);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                List<PlatfForm> list = new ArrayList<>();
                ArrayList<Film> dsFilm = new ArrayList<>();
                ArrayList<Film> temp = program.dsFilm;
                for (int i = 0; i < program.dsFilm.size(); i++) {
                    int result = program.dsFilm.get(i).getTen().toLowerCase().indexOf(query.toLowerCase());
                    if (result != -1) {
                        dsFilm.add(program.dsFilm.get(i));
                    }
                }
                PlatfForm pf = new PlatfForm(dsFilm, query);
                list.add(pf);
                PlatfFormAdapter adapter1 = new PlatfFormAdapter(LoadDanhSachActivity.this, R.layout.listview_my_layout, list);

                listView.setAdapter(adapter1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private ArrayList<Film> getdsFilm(NodeList dsNode) {
        ArrayList<Film> dsFilm = new ArrayList<>();
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
        return dsFilm;
    }

    private class dsbyTheLoaiTask extends AsyncTask<String, Void, ArrayList<Film>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Film> films) {
            super.onPostExecute(films);

            List<PlatfForm> list = new ArrayList<>();
            PlatfForm pf = new PlatfForm(films, "Thể Loại");
            list.add(pf);
            PlatfFormAdapter adapter1 = new PlatfFormAdapter(LoadDanhSachActivity.this, R.layout.listview_my_layout, list);

            listView.setAdapter(adapter1);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Film> doInBackground(String... strings) {
            ArrayList<Film> dsFilm = new ArrayList<>();
            try {
                URL url = new URL(program.ip + "GetFilmByIDTheLoai");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(strings[0]);
                writer.flush();
                writer.close();
                os.close();

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
        private class dsbyQuocGiaTask extends AsyncTask<String, Void, ArrayList<Film>> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(ArrayList<Film> films) {
                super.onPostExecute(films);

                List<PlatfForm> list = new ArrayList<>();
                PlatfForm pf = new PlatfForm(films, "Quốc Gia");
                list.add(pf);
                PlatfFormAdapter adapter1 = new PlatfFormAdapter(LoadDanhSachActivity.this, R.layout.listview_my_layout, list);

                listView.setAdapter(adapter1);

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected ArrayList<Film> doInBackground(String... strings) {
                ArrayList<Film> dsFilm = new ArrayList<>();
                try {
                    URL url = new URL(program.ip + "GetFilmByIDQuocGia");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    writer.write(strings[0]);
                    writer.flush();
                    writer.close();
                    os.close();

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
    }

