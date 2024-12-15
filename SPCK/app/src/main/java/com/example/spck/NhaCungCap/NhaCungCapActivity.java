package com.example.spck.NhaCungCap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spck.Login.SQLiteConnect;
import com.example.demo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NhaCungCapActivity extends AppCompatActivity {
    ImageView imgThemNhaCC, imgThoat;
    EditText edtTimKiem;
    ListView lvNhaCungCap;
    ArrayList<NhaCungCap> listNCC;
    NhaCungCapAdapter nhaCungCapAdapter;
    SQLiteConnect sqLite;
    ArrayList<NhaCungCap> filteredList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nha_cung_cap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvNhaCungCap = findViewById(R.id.lvNhaCungCap);
        imgThemNhaCC = findViewById(R.id.imgThemNhaCC);
        imgThoat = findViewById(R.id.imgThoat);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        listNCC = new ArrayList<>();

        sqLite = new SQLiteConnect(getBaseContext(),getString(R.string.db_name), null, 1);

        //Tạo bảng thay vì thêm dữ liệu như trên
        String query = "CREATE TABLE IF NOT EXISTS nhacungcap(" +
                "key integer primary key autoincrement unique, "+
                "maNCC char not null, " +
                "tenNCC char not null, " +
                "soDT char, " +
                "diaChi char not null, " +
                "email char not null, " +
                "ngayTao datetime not null, " +
                "ghiChu char not null);";
        sqLite.queryData(query);
//        query = "INSERT INTO nhacungcap(maNCC, tenNCC, soDT, diaChi, email, ngayTao, ghiChu) "+
//                " VALUES ('ncc01', 'Mỹ phẩm','0368149726','nghe an','nguyenlan2372004@gmail.com', '2023-10-05','khong')";
//        sqLite.queryData(query);


        nhaCungCapAdapter = new NhaCungCapAdapter(NhaCungCapActivity.this, R.layout.lv_nha_cung_cap,listNCC);
        lvNhaCungCap.setAdapter(nhaCungCapAdapter);
        loadDataNhaCungCap();

        // Khởi tạo màn hình xem chi tiết
        lvNhaCungCap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent xemChiTietIntent = new Intent(getBaseContext(), XemChiTietNhaCungCapActivity.class);
                Bundle data = new Bundle();
                NhaCungCap cv = nhaCungCapAdapter.getListNCC().get(position);
                data.putSerializable("ncc_value", cv); //(1: mã để lấy ra, 2: giá trị)
                xemChiTietIntent.putExtras(data); // đẩy dữ liệu vào
                startActivity(xemChiTietIntent);
                Toast.makeText(NhaCungCapActivity.this,
                        listNCC.get(position).getTenNhaCC(), Toast.LENGTH_SHORT).show();

            }
        });
        imgThemNhaCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent themMoiIntent = new Intent(NhaCungCapActivity.this, ThemNhaCungCapActivity.class);
                themMoiNCCLauncher.launch(themMoiIntent);
            }
        });
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String query = charSequence.toString().toLowerCase();
                filterList(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void filterList(String query) {

        filteredList.clear(); // Xóa danh sách hiện tại

        // Chuyển từ khóa tìm kiếm thành chữ thường để so sánh không phân biệt hoa/thường
        String lowerCaseQuery = query.toLowerCase().trim();

        // Duyệt qua tất cả nhà cung cấp trong listNCC
        for (NhaCungCap ncc : listNCC) {
            // Kiểm tra nếu tên, mã hoặc số điện thoại của nhà cung cấp chứa từ khóa tìm kiếm
            if (ncc.getMaNCC().toLowerCase().contains(lowerCaseQuery) ||
                    ncc.getTenNhaCC().toLowerCase().contains(lowerCaseQuery) ||
                    ncc.getSoDT().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(ncc); // Thêm vào danh sách tìm được
            }
        }

        // Cập nhật adapter với danh sách đã lọc
        nhaCungCapAdapter = new NhaCungCapAdapter(NhaCungCapActivity.this, R.layout.lv_nha_cung_cap, filteredList);
        lvNhaCungCap.setAdapter(nhaCungCapAdapter);
    }

    public void  loadDataNhaCungCap(){
        String query = "SELECT * FROM nhacungcap";
        Cursor data = sqLite.getData(query);
        listNCC.clear();
        while (data.moveToNext()){
            try {
                int key = data.getInt(0);
                String maNCC = data.getString(1);
                String tenNCC = data.getString(2);
                String soDT = data.getString(3);
                String diaChi = data.getString(4);
                String email = data.getString(5);
                String ngayTao = data.getString(6);
                String ghiChu = data.getString(7);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNgayTao = dateFormat.parse(ngayTao);
                NhaCungCap cv = new NhaCungCap(key, maNCC, tenNCC, soDT, diaChi, email, dateNgayTao, ghiChu);
                listNCC.add(cv);
            } catch (Exception e) {
                Log.d("Lỗi đọc dữ liệu: ", e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        nhaCungCapAdapter.notifyDataSetChanged();
    }
    // phần sửa công việc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == 123){
            loadDataNhaCungCap();
        }
    }
    // phần thêm mới
    ActivityResultLauncher themMoiNCCLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        loadDataNhaCungCap();
                    }
                }
            });

}