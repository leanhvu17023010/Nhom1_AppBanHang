package com.example.spck.DonHang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.spck.Login.SQLiteConnect;
import com.example.demo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DonHangAdapter extends ArrayAdapter<DonHang> {
    Activity context;
    int resource;
    ArrayList<DonHang> listDonHang;

    public DonHangAdapter(Activity context, int resource, ArrayList<DonHang> listDonHang) {
        super(context, resource, listDonHang);
        this.context = context;
        this.resource = resource;
        this.listDonHang = listDonHang;
    }

    @Override
    public int getCount() {
        return listDonHang.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        // Các TextView trong customView
        TextView tvMaHD = customView.findViewById(R.id.tvMaHD);
        TextView tvTenKhachHang = customView.findViewById(R.id.tvTenKhachHang);
        TextView tvNgayVaGio = customView.findViewById(R.id.tvNgayVaGio);
        TextView tvTrangThai = customView.findViewById(R.id.tvTrangThai);
        TextView tvTongTien = customView.findViewById(R.id.tvTongTien);

        DonHang donHang = listDonHang.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        tvMaHD.setText(String.valueOf(donHang.getMaDonHang()));
        tvTenKhachHang.setText(donHang.getTenKH());
        tvNgayVaGio.setText(dateFormat.format(donHang.getNgayVaGio()));
        tvTrangThai.setText(donHang.getTrangThai());
        tvTongTien.setText(String.valueOf(donHang.getTongTien()));

        // Thêm màu cho trạng thái ban đầu
        if ("Hoàn thành".equals(donHang.getTrangThai())) {
            tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.green)); // Màu xanh
        } else if ("Đang chờ".equals(donHang.getTrangThai())) {
            tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.red)); // Màu đỏ
        }

        tvTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại để chọn trạng thái
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thay đổi trạng thái");
                builder.setItems(new CharSequence[]{"Hoàn thành", "Đang chờ"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // Xác định trạng thái mới
                            String trangThai = (which == 0) ? "Hoàn thành" : "Đang chờ";
                            String maDonHang = tvMaHD.getText().toString(); // Lấy mã đơn hàng từ TextView
                            SQLiteConnect sqLiteConnect = new SQLiteConnect(context, context.getString(R.string.db_name), null, 1);

                            // Cập nhật trạng thái trong cơ sở dữ liệu
                            if (sqLiteConnect != null) {
                                String query = "UPDATE donhang SET trangThai = '" + trangThai + "' WHERE maDonHang = " + maDonHang;
                                sqLiteConnect.queryData(query);

                                // Cập nhật giao diện
                                tvTrangThai.setText(trangThai);
                                if ("Hoàn thành".equals(trangThai)) {
                                    tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.green));
                                } else {
                                    tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.red));
                                }
                                Toast.makeText(context, "Đã cập nhật trạng thái: " + trangThai, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Lỗi: Không kết nối được cơ sở dữ liệu.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Lỗi", e.toString());
                            Toast.makeText(context, "Lỗi khi cập nhật trạng thái.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        return customView;
    }
}
