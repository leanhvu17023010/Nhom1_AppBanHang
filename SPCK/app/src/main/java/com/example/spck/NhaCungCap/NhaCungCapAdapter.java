package com.example.spck.NhaCungCap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.spck.Login.SQLiteConnect;
import com.example.demo.R;

import java.util.ArrayList;

public class NhaCungCapAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<NhaCungCap> listNCC;
    static int PERMISSION_CODE = 100;

    public NhaCungCapAdapter(Activity context, int resource, ArrayList<NhaCungCap> listNCC){
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listNCC = listNCC;
    }

    @Override
    public int getCount() {
        return listNCC.size();
    }

    public ArrayList<NhaCungCap> getListNCC() {
        return listNCC;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);
        TextView tvMaVaTenNCC = customView.findViewById(R.id.tvMaVaTenNCC);
        TextView tvSDT = customView.findViewById(R.id.tvSDT);
        ImageView imgGoiDien = customView.findViewById(R.id.imgGoiDien);
        ImageView imgSuaNhaCC = customView.findViewById(R.id.imgSuaNhaCC);
        ImageView imgXoaNhaCC = customView.findViewById(R.id.imgXoaNhaCC);

        NhaCungCap ncc = listNCC.get(position);
        tvMaVaTenNCC.setText(ncc.getMaNCC() + " - " + ncc.getTenNhaCC());
        tvSDT.setText(ncc.getSoDT());




        imgGoiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra quyền CALL_PHONE
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Nếu chưa cấp quyền, yêu cầu quyền
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
                }
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    String soDT = tvSDT.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + soDT));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Cần cấp quyền gọi điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgSuaNhaCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Sửa" + ncc.getTenNhaCC(), Toast.LENGTH_SHORT).show();
                // gói dữ liệu để sửa
                Bundle data = new Bundle();
                data.putSerializable("ncc_value", ncc);
                Intent suaIntent = new Intent(context, SuaNhaCungCapActivity.class);
                suaIntent.putExtras(data);
                context.startActivityForResult(suaIntent, 123);// mã tự đặt
            }
        });

        imgXoaNhaCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa công việc");
                builder.setMessage("Bạn có chắc muốn xóa " + ncc.getMaNCC() +
                        "-" + ncc.getTenNhaCC() + "?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String query = "DELETE FROM nhacungcap WHERE key = '" + ncc.getKey() + "'";
                            SQLiteConnect sqLite = new SQLiteConnect(context, context.getString(R.string.db_name), null, 1 );
                            sqLite.queryData(query);
                            Toast.makeText(context, "Đã xóa " + ncc.getMaNCC() +
                                    "-" + ncc.getTenNhaCC(), Toast.LENGTH_SHORT).show();
                            ((NhaCungCapActivity)context).loadDataNhaCungCap();
                            dialog.dismiss();
                        }catch (Exception e){
                            Log.d("Lỗi xóa CSDL", e.toString());
                            Toast.makeText(context, "Lỗi xóa CSDL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();

            }
        });

        return customView;
    }
}
