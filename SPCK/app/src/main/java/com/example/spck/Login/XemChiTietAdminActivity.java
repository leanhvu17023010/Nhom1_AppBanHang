package com.example.spck.Login;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.R;


public class XemChiTietAdminActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1; // Mã yêu cầu cho Camera Intent

    ImageView ivProfileImage, imgback;
    Button btnTakePhoto, btnEdit, btnSave;
    EditText tvName, tvPhone, tvAddress, tvEmail, tvBirthDate, tvNotes;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private SQLiteConnect SQLiteConnect;
    private SQLiteDatabase db;
    private boolean isEditMode = false; // Trạng thái chỉnh sửa
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_chi_tiet_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvEmail = findViewById(R.id.tvEmail);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvNotes = findViewById(R.id.tvNotes);
        imgback = findViewById(R.id.imgback);
        // Khởi tạo SQLiteConnect
        SQLiteConnect = new SQLiteConnect(this, "appquanlybanhang.sql", null, 1);
        db = SQLiteConnect.getWritableDatabase();

        // Lấy tên đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("TEN_DANG_NHAP", null);

        if (tenDangNhap != null) {
            // Hiển thị thông tin chi tiết của người dùng
            loadUserDetails(tenDangNhap);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện nút chụp ảnh
        btnTakePhoto.setOnClickListener(view -> openCamera());

        // Xử lý sự kiện nút Edit
        btnEdit.setOnClickListener(view -> enableEditing());

        // Xử lý sự kiện nút Save
        btnSave.setOnClickListener(view -> saveUserDetails(tenDangNhap));


        // Khởi tạo ActivityResultLauncher để xử lý kết quả từ Camera Intent
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            ivProfileImage.setImageBitmap(imageBitmap); // Hiển thị ảnh lên ImageView
                        }
                    }
                }
        );


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadUserDetails(String tenDangNhap) {
        // Truy vấn cơ sở dữ liệu để lấy thông tin người dùng
        String query = "SELECT * FROM taikhoan WHERE ten_tai_khoan = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenDangNhap});

        if (cursor.moveToFirst()) {
            // Gán dữ liệu từ cơ sở dữ liệu lên các EditText
            tvName.setText(cursor.getString(cursor.getColumnIndexOrThrow("ten_tai_khoan")));
            tvPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("so_dien_thoai")));
            tvAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("dia_chi")));
            tvEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            tvBirthDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("ngay_sinh")));
            tvNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow("gioi_tinh")));

            // Đặt các EditText ở chế độ không chỉnh sửa
            disableEditing();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin chi tiết!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void enableEditing() {
        // Cho phép chỉnh sửa các EditText
        isEditMode = true;
        tvName.setEnabled(true);
        tvPhone.setEnabled(true);
        tvAddress.setEnabled(true);
        tvEmail.setEnabled(true);
        tvBirthDate.setEnabled(true);
        tvNotes.setEnabled(true);

        Toast.makeText(this, "Chế độ chỉnh sửa được bật!", Toast.LENGTH_SHORT).show();
    }

    private void disableEditing() {
        // Không cho phép chỉnh sửa các EditText
        isEditMode = false;
        tvName.setEnabled(false);
        tvPhone.setEnabled(false);
        tvAddress.setEnabled(false);
        tvEmail.setEnabled(false);
        tvBirthDate.setEnabled(false);
        tvNotes.setEnabled(false);
    }

    private void saveUserDetails(String tenDangNhap) {
        if (!isEditMode) {
            Toast.makeText(this, "Hãy nhấn nút chỉnh sửa trước khi lưu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu từ các EditText
        String name = tvName.getText().toString().trim();
        String phone = tvPhone.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        String birthDate = tvBirthDate.getText().toString().trim();
        String notes = tvNotes.getText().toString().trim();

        // Cập nhật thông tin người dùng trong cơ sở dữ liệu
        ContentValues contentValues = new ContentValues();
        contentValues.put("ten_tai_khoan", name);
        contentValues.put("so_dien_thoai", phone);
        contentValues.put("dia_chi", address);
        contentValues.put("email", email);
        contentValues.put("ngay_sinh", birthDate);
        contentValues.put("gioi_tinh", notes);

        int rowsUpdated = db.update("taikhoan", contentValues, "ten_tai_khoan = ?", new String[]{tenDangNhap});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Thông tin đã được cập nhật!", Toast.LENGTH_SHORT).show();
            disableEditing();
        } else {
            Toast.makeText(this, "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền camera đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền camera bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera() {
        // Kiểm tra quyền camera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Nếu quyền đã được cấp, mở camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Mở camera
                cameraLauncher.launch(takePictureIntent);  // Đảm bảo sử dụng cameraLauncher để mở camera
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Xử lý kết quả từ Camera Intent
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            // Nhận ảnh từ Intent
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            // Hiển thị ảnh trên ImageView
//            ivProfileImage.setImageBitmap(imageBitmap);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
