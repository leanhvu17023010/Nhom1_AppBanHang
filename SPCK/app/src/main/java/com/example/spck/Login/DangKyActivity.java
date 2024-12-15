package com.example.spck.Login;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.R;

public class DangKyActivity extends AppCompatActivity {
    Button btnDangNhap, btnDangKy;
    SQLiteConnect SQLiteConnect;
    EditText edtTenTK, edtEmailDK, edtSDT, edtMatKhau, edtNgaySinh, edtDiaChi;
    RadioButton radNam, radNu;
    RadioGroup radGioiTinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        // Đảm bảo xử lý các Insets của hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các EditText và Button
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        edtTenTK = findViewById(R.id.edtTenTK);
        edtEmailDK = findViewById(R.id.edtEmailDK);
        edtSDT = findViewById(R.id.edtSDT);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        radGioiTinh = findViewById(R.id.radGioiTinh);

        // Chuyển sang màn hình đăng nhập
        btnDangNhap.setOnClickListener(view -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        });


        // Khởi tạo SQLiteConnect
        SQLiteConnect = new SQLiteConnect(DangKyActivity.this, "appquanlybanhang.sql", null, 1);

        // Tạo bảng nếu chưa có
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "taikhoan ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten_tai_khoan VARCHAR(50) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "so_dien_thoai VARCHAR(15), " +
                "mat_khau VARCHAR(255) NOT NULL, " +
                "gioi_tinh TEXT, " +
                "ngay_sinh DATE, " +
                "dia_chi VARCHAR(255) );";
        SQLiteConnect.queryData(sql);

        // Đăng ký tài khoản
        btnDangKy.setOnClickListener(view -> {
            String tenTaiKhoan = edtTenTK.getText().toString().trim();
            String email = edtEmailDK.getText().toString().trim();
            String soDienThoai = edtSDT.getText().toString().trim();
            String matKhau = edtMatKhau.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();

            int selectedId = radGioiTinh.getCheckedRadioButtonId();
            String gioiTinh = selectedId == R.id.radNam ? "Nam" : "Nữ";

            // Kiểm tra dữ liệu
            if (tenTaiKhoan.isEmpty()) {
                edtTenTK.setError("Tên tài khoản không được để trống!");
                edtTenTK.requestFocus();
                return;
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmailDK.setError("Email không hợp lệ!");
                edtEmailDK.requestFocus();
                return;
            }

            if (SQLiteConnect.isEmailExist(email)) { // Kiểm tra email đã tồn tại
                edtEmailDK.setError("Email đã tồn tại trong hệ thống!");
                edtEmailDK.requestFocus();
                return;
            }

            if (soDienThoai.isEmpty() || !soDienThoai.matches("\\d{10,11}")) {
                edtSDT.setError("Số điện thoại không hợp lệ!");
                edtSDT.requestFocus();
                return;
            }

            if (matKhau.isEmpty() || matKhau.length() < 2) {
                edtMatKhau.setError("Mật khẩu phải có ít nhất 2 ký tự!");
                edtMatKhau.requestFocus();
                return;
            }

            if (ngaySinh.isEmpty()) {
                edtNgaySinh.setError("Ngày sinh không được để trống!");
                edtNgaySinh.requestFocus();
                return;
            }

            if (diaChi.isEmpty()) {
                edtDiaChi.setError("Địa chỉ không được để trống!");
                edtDiaChi.requestFocus();
                return;
            }

            // Lưu thông tin tài khoản vào SQLite
            SQLiteDatabase database = SQLiteConnect.getWritableDatabase();
            String sqlInsert = "INSERT INTO taikhoan (ten_tai_khoan, email, so_dien_thoai, mat_khau, gioi_tinh, ngay_sinh, dia_chi) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                database.execSQL(sqlInsert, new Object[]{tenTaiKhoan, email, soDienThoai, matKhau, gioiTinh, ngaySinh, diaChi});
                Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                // Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DangKyActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        // Quay lại màn hình đăng nhập khi nhấn nút back vật lý
        super.onBackPressed();
        Intent intent = new Intent(DangKyActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
