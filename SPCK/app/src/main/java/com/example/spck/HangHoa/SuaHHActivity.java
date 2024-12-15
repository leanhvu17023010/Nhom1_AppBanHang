package com.example.spck.HangHoa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demo.R;

public class SuaHHActivity extends AppCompatActivity {

    ImageView imgSuaAnhHH;
    EditText edtSuaTenHH, edtSuaGiaHH, edtSuaDaBan, edtSuaTonKho, edtSuaMoTaHH;
    Spinner spnSuaLoaiSP;
    Button btnLuu, btnHuy, btnSuaChonAnh;
    HangHoa hh;
    SQLiteConnect sqLiteConnect;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_hhactivity);
        imgSuaAnhHH = findViewById(R.id.imgSuaAnhHH);
        edtSuaTenHH = findViewById(R.id.edtSuaTenHH);
        edtSuaGiaHH = findViewById(R.id.edtSuaGiaHH);
        edtSuaDaBan = findViewById(R.id.edtSuaDaBan);
        edtSuaTonKho = findViewById(R.id.edtSuaTonKho);
        edtSuaMoTaHH = findViewById(R.id.edtSuaMoTaHH);
        spnSuaLoaiSP = findViewById(R.id.spnSuaLoaiSP);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
        btnSuaChonAnh = findViewById(R.id.btnSuaChonAnh);

        sqLiteConnect = new SQLiteConnect(getBaseContext(), "appquanlybanhang.sql", null, 1);
        Intent intent = getIntent();
        hh = (HangHoa) intent.getSerializableExtra("hh_value");


        edtSuaTenHH.setText(hh.getTenSP());
        edtSuaGiaHH.setText(String.valueOf(hh.getGiaSP()));
        edtSuaDaBan.setText(String.valueOf(hh.getDaBan()));
        edtSuaTonKho.setText(String.valueOf(hh.getTonKho()));
        edtSuaMoTaHH.setText(hh.getNoiDungSP());

        Glide.with(this)
                .load(hh.getHinhAnh())
                .into(imgSuaAnhHH);


        btnLuu.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                try {
                    String hinhAnh = (imageUri != null) ? imageUri.toString() : hh.getHinhAnh();
                    String tenHH = edtSuaTenHH.getText().toString().trim();
                    int giaHH = Integer.parseInt(edtSuaGiaHH.getText().toString().trim());
                    int daBan = Integer.parseInt(edtSuaDaBan.getText().toString().trim());
                    int tonKho = Integer.parseInt(edtSuaTonKho.getText().toString().trim());
                    String moTa = edtSuaMoTaHH.getText().toString().trim();
                    String loaiSP = spnSuaLoaiSP.getSelectedItem().toString();

                    String query = "UPDATE hanghoa " +
                            "SET hinhAnh = '" + hinhAnh + "', " +
                            "tenSP = '" + tenHH + "', " +
                            "giaSP = " + giaHH + ", " +
                            "daBan = " + daBan + ", " +
                            "tonKho = " + tonKho + ", " +
                            "noiDungSP = '" + moTa + "', " +
                            "loaiSP = '" + loaiSP + "' " +
                            "WHERE key = " + hh.getKey() + ";";

                    String insertLichSu = "INSERT INTO lichsu (tenHH, thoiGian, chucNang) " +
                            "VALUES ('" + tenHH + "', datetime('now', 'localtime'), 'Sửa hàng hóa')";
                    sqLiteConnect.queryData(insertLichSu);

                    sqLiteConnect.queryData(query);
                    setResult(123);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(SuaHHActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnHuy.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });

        btnSuaChonAnh.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK ) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(imgSuaAnhHH);
        }
    }
}