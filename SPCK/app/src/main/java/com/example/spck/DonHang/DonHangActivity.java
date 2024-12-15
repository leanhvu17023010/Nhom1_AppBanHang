package com.example.spck.DonHang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spck.Login.SQLiteConnect;
import com.example.spck.Dashboard.DashBoard;
import com.example.demo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DonHangActivity extends AppCompatActivity {
    EditText edtTimKiem;
    ImageView imgThoat;
    ListView lvDonHang;
    DonHangAdapter donHangAdapter;
    ArrayList<DonHang> listDonHang;
    SQLiteConnect sqLiteConnect;
    ArrayList<DonHang> filteredList = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_don_hang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTimKiem = findViewById(R.id.edtTimKiem);
        imgThoat = findViewById(R.id.imgThoat);
        lvDonHang = findViewById(R.id.lvNhaCungCap);
        listDonHang = new ArrayList<>();
        sqLiteConnect = new SQLiteConnect(DonHangActivity.this, getString(R.string.db_name), null, 1);

        String createDonHangTable = "CREATE TABLE IF NOT EXISTS donhang (" +
                "maDonHang INTEGER PRIMARY KEY, " +
                "id INTEGER NOT NULL, " +           // idkhachhang
                "tenKH TEXT NOT NULL, " +
                "ngayVaGio DATETIME NOT NULL, " +
                "tongTien INTEGER NOT NULL, " +
                "trangThai TEXT NOT NULL"  +
                ");";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('1', '1', 'Nguyen Thi Lan','2024-12-5 11:30:00','200000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('2', '2', 'Vũ Hà Yến Vy','2024-12-5 11:40:00','250000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('3', '3', 'Nguyễn Kiên Quyết','2024-12-5 11:50:00','220000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('4', '4', 'Phạm Quang Tú','2024-12-5 12:00:00','240000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('5', '5', 'Lê Anh Vũ','2024-12-5 12:10:00','270000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

        createDonHangTable = "INSERT OR IGNORE INTO donhang(maDonHang, id, tenKH, ngayVaGio, tongTien, trangThai) "+
                " VALUES ('6', '6', 'Nguyễn Đức Tân','2024-12-5 12:20:00','280000','Hoàn thành')";
        sqLiteConnect.queryData(createDonHangTable);

//        String deleteDonHangQuery = "DELETE FROM donhang";
//        sqLiteConnect.queryData(deleteDonHangQuery);

        donHangAdapter = new DonHangAdapter(DonHangActivity.this, R.layout.lv_don_hang, listDonHang);
        lvDonHang.setAdapter(donHangAdapter);
        loadDataDonHang();
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Truy vấn tổng thu từ bảng donhang
                String query = "SELECT SUM(tongTien) FROM donhang";
                Cursor cursor = sqLiteConnect.getData(query);

                int totalAmount = 0;

// Kiểm tra nếu cursor không rỗng và lấy kết quả
                if (cursor != null && cursor.moveToFirst()) {
                    totalAmount = cursor.getInt(0);  // Lấy tổng tiền từ cột đầu tiên (cột SUM)
                }

// Đóng cursor sau khi sử dụng
                cursor.close();

// Chuyển giá trị totalAmount sang màn hình DashBoard
                Intent intent = new Intent(DonHangActivity.this, DashBoard.class);
                intent.putExtra("totalAmount", totalAmount);
                startActivity(intent);

                finish();
            }
        });
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().toLowerCase();
                filterList(query);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void filterList(String query) {
        filteredList.clear();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (DonHang donHang : listDonHang) {
            if (String.valueOf(donHang.getMaDonHang()).toLowerCase().contains(lowerCaseQuery) ||
                    donHang.getTenKH().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(donHang);
            }
        }

        donHangAdapter = new DonHangAdapter(DonHangActivity.this, R.layout.lv_don_hang, filteredList);
        lvDonHang.setAdapter(donHangAdapter);
    }

    public void loadDataDonHang() {
        String query = "SELECT * FROM donhang";
        Cursor data = sqLiteConnect.getData(query);
        listDonHang.clear();
        while (data.moveToNext()) {
            try {
                int maDonHang = data.getInt(0);
                int id = data.getInt(1);
                String tenKH = data.getString(2);
                String ngayVaGio = data.getString(3);
                int tongTien = data.getInt(4);
                String trangThai = data.getString(5);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateNgayTao = dateFormat.parse(ngayVaGio);
                DonHang donHang = new DonHang(maDonHang, id, tenKH, dateNgayTao, tongTien, trangThai);
                listDonHang.add(donHang);
            } catch (Exception e) {
                Log.d("Lỗi đọc dữ liệu: ", e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        donHangAdapter.notifyDataSetChanged();
    }
}