package com.example.spck.NhaCungCap;

import android.content.Intent;
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

public class SuaNhaCungCapActivity extends AppCompatActivity {
    EditText edtTenNhaCC, edtSDT, edtDiaChi, edtEmail,edtGhiChu;
    ImageView imgThoat;
    Button btnLuu, btnHuyBo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_nha_cung_cap);
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

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        NhaCungCap ncc =(NhaCungCap) data.get("ncc_value");
        edtTenNhaCC.setText(ncc.getTenNhaCC());
        edtSDT.setText(ncc.getSoDT());
        edtDiaChi.setText(ncc.getDiaChi());
        edtEmail.setText(ncc.getEmail());
        edtGhiChu.setText(ncc.getGhiChu());

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
                    String tenNCC = edtTenNhaCC.getText().toString().trim();
                    String soDT = edtSDT.getText().toString().trim();
                    String diaChi = edtDiaChi.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String ghiChu = edtGhiChu.getText().toString().trim();

                    String query = "UPDATE nhacungcap " +
                            "set tenNCC = '" + tenNCC + "', " +
                            "soDT = '" + soDT + "', " +
                            "diaChi = '" + diaChi + "', " +
                            "email = '" + email + "', " +
                            "ghiChu = '" + ghiChu + "' " +
                            "WHERE key = '" + ncc.getKey() + "'; ";
                    SQLiteConnect sqLite = new SQLiteConnect(getBaseContext(), getString(R.string.db_name), null, 1);
                    sqLite.queryData(query);

                    Toast.makeText(SuaNhaCungCapActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    setResult(123);
                    finish();
                }catch (Exception e){
                    Toast.makeText(SuaNhaCungCapActivity.this, "Có lỗi trong quá trình sửa dữ liệu", Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi sửa nhà cung cấp", e.toString());
                }
            }
        });
    }
}