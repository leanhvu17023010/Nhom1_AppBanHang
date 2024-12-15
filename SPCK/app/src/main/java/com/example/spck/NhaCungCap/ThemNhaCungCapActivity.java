package com.example.spck.NhaCungCap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spck.Login.SQLiteConnect;
import com.example.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThemNhaCungCapActivity extends AppCompatActivity {
    EditText edtTenNhaCC, edtSDT, edtDiaChi, edtEmail,edtGhiChu, edtMaNhaCC;
    Button btnLuu, btnHuyBo;
    ImageView imgThoat;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_nha_cung_cap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnHuyBo = findViewById(R.id.btnHuyBo);
        btnLuu = findViewById(R.id.btnLuu);
        imgThoat = findViewById(R.id.imgThoat);
        edtTenNhaCC = findViewById(R.id.edtTenNhaCC);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        edtMaNhaCC = findViewById(R.id.edtMaNhaCC);

        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String maNCC = edtMaNhaCC.getText().toString().trim();
                    String tenNCC = edtTenNhaCC.getText().toString().trim();
                    String soDT = edtSDT.getText().toString().trim();
                    String diaChi = edtDiaChi.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String ghiChu = edtGhiChu.getText().toString().trim();
                    // Lấy ngày tạo
                    String ngayTao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    // Kiểm tra xem các trường không rỗng
                    if (maNCC.isEmpty() ||tenNCC.isEmpty()|| soDT.isEmpty() || diaChi.isEmpty() || email.isEmpty()) {
                        Toast.makeText(ThemNhaCungCapActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String query = "INSERT INTO nhacungcap (maNCC, tenNCC, soDT, diaChi, email, ngayTao, ghiChu) " +
                            "VALUES ('" + maNCC + "', '" + tenNCC + "', '" + soDT + "', '" + diaChi + "', '" + email + "', '" + ngayTao + "', '" + ghiChu + "');";

                    SQLiteConnect sqLite = new SQLiteConnect(getBaseContext(),getString(R.string.db_name), null, 1 );
                    sqLite.queryData(query);
                    Toast.makeText(ThemNhaCungCapActivity.this, "Thêm " + maNCC + "-" + tenNCC + " thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();

                }catch (Exception e){
                    Toast.makeText(ThemNhaCungCapActivity.this, "Có lỗi trong quá trình thêm mới dữ liệu", Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi thêm công việc", e.toString());
                }


            }
        });
    }
}