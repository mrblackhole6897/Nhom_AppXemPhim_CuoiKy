package com.example.appfilmnew;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<Film> infons = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Film> info) {
        infons = info;

        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.name.setText( infons.get(position).getTen());

        String path = "sdcard/photo/"+infons.get(position).getNameHinh();
       holder.image.setImageDrawable(Drawable.createFromPath(path));




        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + infons.get(position).getUrlHinh());
                Toast.makeText(mContext, infons.get(position).getMa(), Toast.LENGTH_SHORT).show();
               if(program.xemFilm == false) {
                   Intent intent = new Intent(mContext, LoadFilmSmallActivity.class);
                   intent.putExtra("key_1", infons.get(position).getMa().toString());
                   intent.putExtra("key_hinh", infons.get(position).getNameHinh().toString());
                   String temp = "Tên: " + infons.get(position).getTen() +
                           "\nĐạo Diễn: " + infons.get(position).getDaoDien() +
                           "\nDiễn Viên: " + infons.get(position).getDienVien() +
                           "\nNội Dung: " + infons.get(position).getNoiDung();

                   intent.putExtra("key_Thongtin", temp);
                   mContext.startActivity(intent);
               }
               else
               {
                   boolean checkFilm = false;
                   if(program.dsFilmDaMua != null)
                   {
                       for(int i =0; i <program.dsFilmDaMua.length;i++)
                       {
                           if(infons.get(position).getMa().equals(program.dsFilmDaMua[i]))
                           {
                               checkFilm = true;

                           }
                       }
                   }
                   if(checkFilm) {


                       String temp = "ma=" + infons.get(position).getMa() + "&stt=" + infons.get(position).getTen();
                       FilmNoiBatTask filmNoiBatTask = new FilmNoiBatTask();
                       filmNoiBatTask.execute(temp);
                   }
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
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
            Intent intent = new Intent(mContext, LoadFilmBigActivity.class);
            intent.putExtra("key_2",s);
            mContext.startActivity(intent);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}