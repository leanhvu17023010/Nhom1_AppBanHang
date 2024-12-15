package com.example.spck.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spck.Dashboard.DashBoard;
import com.example.demo.R;

public class DangNhapActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtMatKhau;
    private Button btnDangKy, btnDangNhap;
    private SQLiteConnect SQLiteConnect;
    private CheckBox chkRememberMe;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        chkRememberMe = findViewById(R.id.chkRememberMe);


        // Khởi tạo SQLiteConnect
        SQLiteConnect = new SQLiteConnect(DangNhapActivity.this, "appquanlybanhang.sql", null, 1);

        // Tải thông tin tài khoản đã lưu
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("EMAIL", "");
        String savedPassword = sharedPreferences.getString("MATKHAU", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            edtEmail.setText(savedEmail);
            edtMatKhau.setText(savedPassword);
            chkRememberMe.setChecked(true);
        }

        // Xử lý nút Đăng ký
        btnDangKy.setOnClickListener(view -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
            finish();
        });

        btnDangNhap.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String matkhau = edtMatKhau.getText().toString().trim();

            if (email.isEmpty() || matkhau.isEmpty()) {
                Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra thông tin đăng nhập
            if (kiemTraDangNhap(email, matkhau)) {
                // Lưu tên đăng nhập vào SharedPreferences
                String tenDangNhap = layTenDangNhap(email);
                if (tenDangNhap != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit(); // Sử dụng biến đã khai báo ở trên
                    editor.putString("TEN_DANG_NHAP", tenDangNhap);

                    // Lưu thông tin tài khoản nếu "Nhớ tài khoản" được chọn
                    if (chkRememberMe.isChecked()) {
                        editor.putString("EMAIL", email);
                        editor.putString("MATKHAU", matkhau);
                    } else {
                        editor.remove("EMAIL");
                        editor.remove("MATKHAU");
                    }
                    editor.apply();
                }

                // Chuyển đến Trang chủ
                Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DangNhapActivity.this, DashBoard.class); // Trang chính của ứng dụng
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(DangNhapActivity.this, "Email hoặc mật khẩu không đúng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean kiemTraDangNhap(String email, String matkhau) {
        SQLiteDatabase database = SQLiteConnect.getReadableDatabase(); // Khởi tạo database từ SQLiteConnect
        String query = "SELECT * FROM taikhoan WHERE email = ? AND mat_khau = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email, matkhau});

        boolean isValid = cursor.getCount() > 0; // Nếu tìm thấy tài khoản
        cursor.close();
        return isValid;
    }

    private String layTenDangNhap(String email) {
        SQLiteDatabase database = SQLiteConnect.getReadableDatabase(); // Khởi tạo database từ SQLiteConnect
        String query = "SELECT ten_tai_khoan FROM taikhoan WHERE email = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            String tenDangNhap = cursor.getString(cursor.getColumnIndexOrThrow("ten_tai_khoan"));
            cursor.close();
            return tenDangNhap;
        }
        cursor.close();
        return null;
    }

    @Override
    public void onBackPressed() {
        // Quay lại màn hình đăng nhập khi nhấn nút back vật lý
        super.onBackPressed();
        Intent intent = new Intent(DangNhapActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
