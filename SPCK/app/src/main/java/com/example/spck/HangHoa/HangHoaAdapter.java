package com.example.spck.HangHoa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo.R;

import java.util.ArrayList;

public class HangHoaAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<HangHoa> listHH;

    public HangHoaAdapter(Activity context, int resource, ArrayList<HangHoa> listHH) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listHH  = listHH;
    }

    public int getCount(){
        return listHH.size();
    }

    public ArrayList<HangHoa> getListHH() {
        return listHH;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource,null);

        TextView tvTenQL = customView.findViewById(R.id.tvTenHT);
        TextView tvHangTonKho = customView.findViewById(R.id.tvHangTonKho);
        TextView tvHangDaBan = customView.findViewById(R.id.tvHangDaBan);
        HangHoa hh = this.listHH.get(position);
        tvTenQL.setText(hh.getTenSP());
        tvHangTonKho.setText(String.valueOf(hh.getTonKho()));
        tvHangDaBan.setText(String.valueOf(hh.getDaBan()));
        ImageView imgXoaHH = customView.findViewById(R.id.imgXoaHH);
        ImageView imgSuaHH = customView.findViewById(R.id.imgSuaHH);




        imgXoaHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa hàng hóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa hàng hóa " + hh.getTenSP() + " không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {

                            String deleteQuery = "DELETE FROM hanghoa WHERE key = '" + hh.getKey() + "'";
                            SQLiteConnect sqLiteConnect = new SQLiteConnect(context, context.getString(R.string.db_name), null, 1);
                            sqLiteConnect.queryData(deleteQuery);


                            String insertLichSu = "INSERT INTO lichsu (tenHH, thoiGian, chucNang) " +
                                    "VALUES ('" + hh.getTenSP() + "', datetime('now', 'localtime'), 'Xóa hàng hóa')";
                            sqLiteConnect.queryData(insertLichSu);

                            Toast.makeText(context, "Xóa " + hh.getTenSP() + " thành công", Toast.LENGTH_SHORT).show();
                            ((MainActivityHangHoa) context).loadDataHH();
                            dialogInterface.dismiss();
                        } catch (Exception e) {
                            Log.d("Lỗi Xóa CSDL", e.toString());
                            Toast.makeText(context, "Lỗi Xóa CSDL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.create().show();
            }
        });

        imgSuaHH.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sửa " + hh.getTenSP() , Toast.LENGTH_SHORT).show();
                Intent suaHHIntent = new Intent(context, SuaHHActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("hh_value", hh);
                suaHHIntent.putExtras(data);
                context.startActivityForResult (suaHHIntent, 123);
            }
        });



        return customView;
    }



}