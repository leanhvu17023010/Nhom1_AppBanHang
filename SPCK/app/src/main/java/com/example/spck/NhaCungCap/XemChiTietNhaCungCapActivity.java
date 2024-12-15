package com.example.spck.NhaCungCap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.R;

import java.text.SimpleDateFormat;

public class XemChiTietNhaCungCapActivity extends AppCompatActivity {
    TextView tvMaNhaCC, tvTenNhaCC, tvSDT, tvDiaChi, tvEmail,
            tvNgayTao, tvGhiChu;
    ImageView imgThoat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_chi_tiet_nha_cung_cap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgThoat = findViewById(R.id.imgThoat);
        tvGhiChu = findViewById(R.id.tvGhiChu);
        tvNgayTao = findViewById(R.id.tvNgayTao);
        tvEmail = findViewById(R.id.tvEmail);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvSDT = findViewById(R.id.tvSDT);
        tvTenNhaCC = findViewById(R.id.tvTenNhaCC);
        tvMaNhaCC = findViewById(R.id.tvMaNhaCC);

        // lấy dữ liệu bên màn hình chính
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        NhaCungCap ncc = (NhaCungCap) data.get("ncc_value");
        tvMaNhaCC.setText(ncc.getMaNCC());
        tvTenNhaCC.setText(ncc.getTenNhaCC());
        tvSDT.setText(ncc.getSoDT());
        tvDiaChi.setText(ncc.getDiaChi());
        tvEmail.setText(ncc.getEmail());
        tvNgayTao.setText(dateFormat.format(ncc.getNgayTao()));
        tvGhiChu.setText(ncc.getGhiChu());


        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}