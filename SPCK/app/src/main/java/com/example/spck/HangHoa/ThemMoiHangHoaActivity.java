package com.example.spck.HangHoa;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThemMoiHangHoaActivity extends AppCompatActivity {

    ImageView imgThemAnhHH;
    EditText tvThemTenHH, tvThemGiaHH, tvThemDaBan, tvThemTonKho, tvThemMoTaHH;
    Button btnThemMoiHH, btnHuyThemMoiHH, btnThemChonAnh;
    Uri imageUri = null;
    Spinner spnThemLoaiSP;

    ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult (
            new ActivityResultContracts.GetContent (),
            new ActivityResultCallback<Uri> () {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imageUri = result;


                        Glide.with(ThemMoiHangHoaActivity.this)
                                .load(imageUri)  // Tải ảnh từ URI
                                .placeholder(R.drawable.baseline_image_24)  // Ảnh placeholder
                                .error(R.drawable.baseline_image_24)  // Ảnh lỗi
                                .into(imgThemAnhHH);  // Đặt ảnh vào ImageView
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_moi_hang_hoa);

        imgThemAnhHH = findViewById(R.id.imgThemAnhHH);
        tvThemTenHH = findViewById(R.id.tvThemTenHH);
        tvThemGiaHH = findViewById(R.id.tvThemGiaHH);
        tvThemDaBan = findViewById(R.id.tvThemDaBan);
        tvThemTonKho = findViewById(R.id.tvThemTonKho);
        tvThemMoTaHH = findViewById(R.id.tvThemMoTaHH);
        spnThemLoaiSP = findViewById(R.id.spnThemLoaiSP);
        btnThemMoiHH = findViewById(R.id.btnThemMoiHH);
        btnHuyThemMoiHH = findViewById(R.id.btnHuyThemMoiHH);
        btnThemChonAnh = findViewById(R.id.btnThemChonAnh);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.loai_san_pham, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThemLoaiSP.setAdapter(adapter);

        btnThemMoiHH.setOnClickListener(view -> {
            try {
                SQLiteConnect sqLiteConnect = new SQLiteConnect(getBaseContext(), "appquanlybanhang.sql", null, 1);
                String imageUriStr = (imageUri != null) ? imageUri.toString() : "";
                String tenSP = tvThemTenHH.getText().toString();
                int giaSP = Integer.parseInt(tvThemGiaHH.getText().toString());
                int daBan = Integer.parseInt(tvThemDaBan.getText().toString());
                int tonKho = Integer.parseInt(tvThemTonKho.getText().toString());
                String noiDungSP = tvThemMoTaHH.getText().toString();
                String loaiSP = spnThemLoaiSP.getSelectedItem().toString();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTime = sdf.format(new Date(System.currentTimeMillis()));

                String insertQuery = "INSERT INTO hanghoa (hinhAnh, tenSP, giaSP, daBan, tonKho, noiDungSP, loaiSP) " +
                        "VALUES ('" + imageUriStr + "', '" + tenSP + "', " + giaSP + ", " + daBan + ", " + tonKho + ", '" + noiDungSP + "', '" + loaiSP + "')";
                sqLiteConnect.queryData(insertQuery);


                String insertLichSu = "INSERT INTO lichsu (tenHH, thoiGian, chucNang) " +
                        "VALUES ('" + tenSP + "', '" + currentTime + "', 'Thêm mới hàng hóa')";
                sqLiteConnect.queryData(insertLichSu);

                Toast.makeText(ThemMoiHangHoaActivity.this,
                        "Thêm mới hàng hóa thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } catch (Exception e) {
                Toast.makeText(ThemMoiHangHoaActivity.this,
                        "Lỗi thêm mới: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        btnHuyThemMoiHH.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });

        btnThemChonAnh.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                imagePickerLauncher.launch("image/*");
            }
        });
    }
}