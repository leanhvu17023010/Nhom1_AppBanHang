package com.example.spck.HangHoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demo.R;

public class XemChiTietActivity extends AppCompatActivity {
    ImageView imgAnhHH;
    TextView tvTenHH, tvGiaHH, tvDaBan, tvTonKho, tvMoTaHH, tvLoaiHH;
    Button btnQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet);

        imgAnhHH = findViewById(R.id.imgAnhHH);
        tvTenHH = findViewById(R.id.tvTenHH);
        tvGiaHH = findViewById(R.id.tvGiaHH);
        tvDaBan = findViewById(R.id.tvDaBan);
        tvTonKho = findViewById(R.id.tvTonKho);
        tvMoTaHH = findViewById(R.id.tvMoTaHH);
        tvLoaiHH = findViewById(R.id.tvLoaiHH);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        btnQuayLai.setOnClickListener(view -> finish());

        Intent intent = getIntent ();
        Bundle data = intent.getExtras ();
        HangHoa hh = (HangHoa) data.get("hh_value");
        tvTenHH.setText(hh.getTenSP());
        tvGiaHH.setText("đ" + String.valueOf(hh.getGiaSP()));
        tvDaBan.setText(String.valueOf(hh.getDaBan()));
        tvTonKho.setText(String.valueOf(hh.getTonKho()));
        tvMoTaHH.setText(hh.getNoiDungSP());
        tvLoaiHH.setText(hh.getLoaiSP());

        String hinhanh = hh.getHinhAnh();
        if (hinhanh != null && !hinhanh.isEmpty()) {
            Glide.with(XemChiTietActivity.this)
                    .load(hinhanh)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_24)
                    .into(imgAnhHH);
        } else {
            imgAnhHH.setImageResource(R.drawable.baseline_image_24); // Đặt ảnh mặc định nếu không có ảnh
        }
    }
}