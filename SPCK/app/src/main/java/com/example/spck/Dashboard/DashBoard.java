package com.example.spck.Dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.R;
import com.example.spck.Customer.Activities.DanhSachKhachHang;
import com.example.spck.DonHang.DonHangActivity;
import com.example.spck.HangHoa.LichSuHeThongActivity;
import com.example.spck.HangHoa.MainActivityHangHoa;
import com.example.spck.Login.DangNhapActivity;
import com.example.spck.Login.WelcomeActivity;
import com.example.spck.Login.XemChiTietAdminActivity;
import com.example.spck.NhaCungCap.NhaCungCapActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity {

    // Khai báo thành phần Header
    private ImageView adminBtn, logoutBtn;

    // Khai báo thành phần Shortcut
    private ImageView goodsBtn, stockBtn, salesBtn, supplierBtn, customerBtn, statisticsBtn;

    // Khai báo thành phần FinancialMonth
    private TextView total_revenue, total_expenses;

    // Khai báo thành phần Chart
    private LineChart lineChart;

    // Khai báo thành phần Statistical
    private TextView salesDay, profitDay, salesWeek, profitWeek, salesYear, profitYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("EMAIL", null);

        if (email == null) {
            // Nếu chưa đăng nhập, chuyển về màn hình đăng nhập
            Intent intent = new Intent(DashBoard.this, WelcomeActivity.class);
            startActivity(intent);
            finish(); // Kết thúc DashBoard
            return;
        }
//
//        if (isLoggedIn) {
//            // Điều hướng đến Activity chính
//            Intent intent = new Intent(this, DashBoard.class);      // Thay TrangChuActivity thành tên file trang chủ
//            startActivity(intent);
//            finish();
//        } else {
//            // Điều hướng đến màn hình Welcome
//            Intent intent = new Intent(this, WelcomeActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Khởi tạo các thành phần Header
        adminBtn = findViewById(R.id.adminBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Thiết lập sự kiện cho nút ảnh đại diện
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện đổi ảnh đại diện
                imgAdmin();
            }
        });

        // Thiết lập sự kiện cho nút đăng xuất
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện đăng xuất
                logOut();
            }
        });

        //Khởi tạo các thành phần Shortcut
        goodsBtn = findViewById(R.id.goodsBtn);

        salesBtn = findViewById(R.id.salesBtn);
        supplierBtn = findViewById(R.id.supplierBtn);
        customerBtn = findViewById(R.id.customerBtn);


        // Thiết lập sự kiện cho các nút
        goodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện cho nút Hàng hóa
                handleGoodsButton();
            }
        });



        salesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện cho nút Đơn bán hàng
                handleSalesButton();
            }
        });

        supplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện cho nút Hàng hóa
                handleSupplierButton();
            }
        });

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện cho nút Hàng hóa
                handleCustomerButton();
            }
        });



        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 100); // Mặc định là 0 nếu không có dữ liệu

        // Truyền dữ liệu vào các TextView
        updateFinancialMonth(totalAmount, 0);



        // Khởi tạo LineChart
        lineChart = findViewById(R.id.line_chart);

        // Truyền dữ liệu vào LineChart
        setDataChart();


        // Liên kết TextView với các ID trong XML
        salesDay = findViewById(R.id.salesDay);
        profitDay = findViewById(R.id.profitDay);
        salesWeek = findViewById(R.id.salesWeek);
        profitWeek = findViewById(R.id.profitWeek);
        salesYear = findViewById(R.id.salesYear);
        profitYear = findViewById(R.id.profitYear);

        // Gọi hàm cập nhật dữ liệu
        updateStatistics();
    }

    // Hàm xử lý sự kiện đổi ảnh Admin
    private void imgAdmin() {
        Intent intent = new Intent(DashBoard.this, XemChiTietAdminActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        // Tạo AlertDialog để xác nhận đăng xuất
        new androidx.appcompat.app.AlertDialog.Builder(DashBoard.this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    // Xóa thông tin đăng nhập đã lưu trong SharedPreferences
//                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.clear(); // Xóa toàn bộ dữ liệu
//                    editor.apply();

                    // Hiển thị thông báo đăng xuất
                    Toast.makeText(DashBoard.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(DashBoard.this, DangNhapActivity.class); // WelcomeActivity là màn hình đăng nhập
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Đóng hộp thoại, không làm gì cả
                    dialog.dismiss();
                })
                .show();
    }



    // Hàm xử lý sự kiện chuyển trang Hàng hóa
    private void handleGoodsButton() {
        Intent intent = new Intent(DashBoard.this, MainActivityHangHoa.class);
        startActivity(intent);

    }

    // Hàm xử lý sự kiện chuyển trang Tình trạng kho
    private void handleStockButton() {
        Intent intent = new Intent(DashBoard.this, LichSuHeThongActivity.class);
        startActivity(intent);

    }

    // Hàm xử lý sự kiện chuyển trang Đơn bán hàng
    private void handleSalesButton() {
        Intent intent = new Intent(DashBoard.this, DonHangActivity.class);
        startActivity(intent);
    }

    // Hàm xử lý sự kiện chuyển trang Nhà cung cấp
    private void handleSupplierButton() {
        Intent intent = new Intent(DashBoard.this, NhaCungCapActivity.class);
        startActivity(intent);
    }

    // Hàm xử lý sự kiện chuyển trang Khách hàng
    private void handleCustomerButton() {
        Intent intent = new Intent(DashBoard.this, DanhSachKhachHang.class);
        startActivity(intent);
    }

    // Hàm xử lý sự kiện chuyển trang Thống kê
    private void handleStatisticsButton() {

    }

    // Hàm cập nhật dữ liệu vào bảng Tài chính tháng này
    private void updateFinancialMonth(int totalRevenue, int totalExpenses) {

        // Khởi tạo các thành phần giao diện
        total_revenue = findViewById(R.id.total_revenue);
        total_expenses = findViewById(R.id.total_expenses);


        total_revenue.setText(String.valueOf(totalRevenue));
        total_expenses.setText(String.valueOf(totalExpenses));
    }

    //Hàm cập nhật dữ liệu vào biểu đồ
    private void setDataChart() {
        List<Entry> entries = new ArrayList<>();

        // Thêm dữ liệu mẫu vào danh sách
        entries.add(new Entry(1, 120));
        entries.add(new Entry(2, 300));
        entries.add(new Entry(3, 240));
        entries.add(new Entry(4, 400));
        entries.add(new Entry(5, 450));
        entries.add(new Entry(6, 700));
        entries.add(new Entry(7, 630));

        LineDataSet dataSet = new LineDataSet(entries, "Ngày"); // Tạo dataset với nhãn
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }

    // Hàm cập nhật dữ liệu cho các TextView
    private void updateStatistics() {
        // Dữ liệu mẫu
        int todaySales = 1000;        // Doanh số hôm nay
        int todayProfit = 300;       // Lợi nhuận hôm nay
        int weekSales = 7000;        // Doanh số 7 ngày
        int weekProfit = 2000;       // Lợi nhuận 7 ngày
        int monthSales = 30000;      // Doanh số 30 ngày
        int monthProfit = 10000;     // Lợi nhuận 30 ngày

        // Cập nhật dữ liệu vào các TextView
        salesDay.setText(String.valueOf(todaySales));
        profitDay.setText(String.valueOf(todayProfit));

        salesWeek.setText(String.valueOf(weekSales));
        profitWeek.setText(String.valueOf(weekProfit));

        salesYear.setText(String.valueOf(monthSales));
        profitYear.setText(String.valueOf(monthProfit));
    }

}