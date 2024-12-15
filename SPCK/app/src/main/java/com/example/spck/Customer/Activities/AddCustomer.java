package com.example.spck.Customer.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spck.Customer.database.CustomerDatabase;
import com.example.spck.Customer.model.Customer;
import com.example.demo.R;

public class AddCustomer extends AppCompatActivity {

    private EditText edtName, edtPhone, edtAddress, edtEmail, edtBirthDate, edtNotes;
    private Button btnSave;
    ImageView imgBack;
    private CustomerDatabase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        // Ánh xạ các trường EditText
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtNotes = findViewById(R.id.edtNotes);

        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgback1);

        // Khởi tạo đối tượng cơ sở dữ liệu
        databaseHelper = new CustomerDatabase(this);

// Cải thiện việc xử lý nhập ngày sinh
        edtBirthDate.addTextChangedListener(new TextWatcher() {
            private boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;

                isEditing = true;
                String input = s.toString();
                String formattedDate = formatDateString(input);

                edtBirthDate.setText(formattedDate);
                edtBirthDate.setSelection(formattedDate.length()); // Đặt con trỏ ở cuối chuỗi
                isEditing = false;
            }
        });

        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newAddress = edtAddress.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();
            String newBirthDate = edtBirthDate.getText().toString().trim();
            String newNotes = edtNotes.getText().toString().trim();

            // Kiểm tra dữ liệu từng ô
            if (TextUtils.isEmpty(newName)) {
                edtName.setError("Vui lòng nhập tên");
                edtName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(newPhone)) {
                edtPhone.setError("Vui lòng nhập số điện thoại");
                edtPhone.requestFocus();
                return;
            }

            if (!newPhone.matches("\\d{10,11}")) { // Kiểm tra định dạng số điện thoại
                edtPhone.setError("Số điện thoại không hợp lệ. Vui lòng nhập 10-11 chữ số.");
                edtPhone.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(newAddress)) {
                edtAddress.setError("Vui lòng nhập địa chỉ");
                edtAddress.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(newEmail)) {
                edtEmail.setError("Vui lòng nhập email");
                edtEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) { // Kiểm tra định dạng email
                edtEmail.setError("Email không hợp lệ");
                edtEmail.requestFocus();
                return;
            }

            if (databaseHelper.isEmailExist(newEmail)) {
                edtEmail.setError("Email đã tồn tại trong hệ thống");
                edtEmail.requestFocus();
                return;
            }


            if (TextUtils.isEmpty(newBirthDate)) {
                edtBirthDate.setError("Ngày sinh không hợp lệ. Hãy nhập theo định dạng DD/MM/YYYY (ví dụ: 08/09/2003)");
                edtBirthDate.requestFocus();
                return;
            }

            // Nếu tất cả dữ liệu hợp lệ
            Customer newCustomer = new Customer();
            newCustomer.setName(newName);
            newCustomer.setPhone(newPhone);
            newCustomer.setAddress(newAddress);
            newCustomer.setEmail(newEmail);
            newCustomer.setBirthDate(newBirthDate);
            newCustomer.setNotes(newNotes);

            boolean isAdded = databaseHelper.addCustomer(newCustomer);

            if (isAdded) {
                Toast.makeText(AddCustomer.this, "Thêm khách hàng thành công!", Toast.LENGTH_SHORT).show();
                Log.d("AddCustomer", "Khách hàng đã được thêm vào cơ sở dữ liệu.");
                setResult(RESULT_OK); // Để Activity trước đó biết cần tải lại danh sách
                finish(); // Đóng Activity
            } else {
                Toast.makeText(AddCustomer.this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                Log.d("AddCustomer", "Lỗi khi thêm khách hàng.");
            }
        });

        // Xử lý sự kiện khi nhấn nút quay lại
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Hàm xử lý tự động thêm dấu //
    private String formatDateString(String input) {
        input = input.replaceAll("[^\\d]", ""); // Loại bỏ các ký tự không phải số

        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i == 2 || i == 4) {
                formatted.append('/'); // Thêm dấu '/' sau ngày và tháng
            }
            formatted.append(input.charAt(i));
        }

        return formatted.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại Activity trước đó
    }
}
