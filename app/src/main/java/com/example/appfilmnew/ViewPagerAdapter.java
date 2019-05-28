package com.example.appfilmnew;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {


   Context context;

    ArrayList<Film> info;
    private LayoutInflater layoutInflater;

     ViewPagerAdapter(Context context, ArrayList<Film> info)
     {
         this.context = context;
         this.info = info;
     }
    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //String path = "sdcard/photo/"+films[position];
        String path = "sdcard/photo/"+info.get(position).getNameHinh();
        imageView.setImageDrawable(Drawable.createFromPath(path));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,LoadFilmSmallActivity.class);
                String temp = info.get(position).getMa().toString();
                intent.putExtra("key_1",temp);
                intent.putExtra("key_hinh", info.get(position).getNameHinh().toString());
                String temp2 = "Tên: " + info.get(position).getTen() +
                        "\nĐạo Diễn: " + info.get(position).getDaoDien() +
                        "\nDiễn Viên: " + info.get(position).getDienVien() +
                        "\nNội Dung: " + info.get(position).getNoiDung();

                intent.putExtra("key_Thongtin", temp2);
                context.startActivity(intent);

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
