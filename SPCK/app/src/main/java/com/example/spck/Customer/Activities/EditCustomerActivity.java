package com.example.spck.Customer.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spck.Customer.database.CustomerDatabase;
import com.example.spck.Customer.model.Customer;
import com.example.demo.R;

public class EditCustomerActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtAddress, edtEmail, edtBirthDate, edtNotes;
    private Button btnSave;
    private ImageView imgBack;
    private CustomerDatabase databaseHelper;
    private int customerId; // ID khách hàng được sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        // Ánh xạ các trường EditText
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtNotes = findViewById(R.id.edtNotes);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgback1);

        // Khởi tạo cơ sở dữ liệu
        databaseHelper = new CustomerDatabase(this);

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

        // Lấy ID khách hàng từ Intent
        customerId = getIntent().getIntExtra("CUSTOMER_ID", -1);

        // Nếu ID không hợp lệ, hiển thị thông báo lỗi và kết thúc Activity
        if (customerId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, không thể sửa khách hàng!", Toast.LENGTH_SHORT).show();
            finish();  // Kết thúc Activity nếu không tìm thấy ID
            return;
        }

        // Hiển thị thông tin khách hàng hiện tại
        loadCustomerDetails(customerId);

        // Lưu thông tin đã sửa
        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newAddress = edtAddress.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();
            String newBirthDate = edtBirthDate.getText().toString().trim();
            String newNotes = edtNotes.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
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

            if (!newPhone.matches("\\d{10,11}")) {
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
                edtBirthDate.setError("Vui lòng nhập ngày sinh");
                edtBirthDate.requestFocus();
                return;
            }

            if (!newBirthDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                edtBirthDate.setError("Ngày sinh không hợp lệ. Định dạng phải là DD/MM/YYYY");
                edtBirthDate.requestFocus();
                return;
            }

            // Cập nhật thông tin khách hàng
            Customer updatedCustomer = new Customer();
            updatedCustomer.setId(customerId);
            updatedCustomer.setName(newName);
            updatedCustomer.setPhone(newPhone);
            updatedCustomer.setAddress(newAddress);
            updatedCustomer.setEmail(newEmail);
            updatedCustomer.setBirthDate(newBirthDate);
            updatedCustomer.setNotes(newNotes);

            boolean isUpdated = databaseHelper.updateCustomer(updatedCustomer);

            if (isUpdated) {
                Toast.makeText(EditCustomerActivity.this, "Cập nhật khách hàng thành công!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(EditCustomerActivity.this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút quay lại
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadCustomerDetails(int customerId) {
        Customer customer = databaseHelper.getCustomerById(customerId);  // Lấy khách hàng theo ID
        if (customer != null) {
            edtName.setText(customer.getName());
            edtPhone.setText(customer.getPhone());
            edtAddress.setText(customer.getAddress());
            edtEmail.setText(customer.getEmail());
            edtBirthDate.setText(customer.getBirthDate());
            edtNotes.setText(customer.getNotes());
        } else {
            Toast.makeText(this, "Không thể tải thông tin khách hàng!", Toast.LENGTH_SHORT).show();
            finish();  // Kết thúc Activity nếu không tìm thấy khách hàng
        }
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
