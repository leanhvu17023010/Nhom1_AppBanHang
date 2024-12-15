package com.example.spck.HangHoa;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;

import java.util.ArrayList;

public class MainActivityHangHoa extends AppCompatActivity {
    ImageView imgThoat, imgThemHH;
    ListView lvHangHoa;
    ArrayList<HangHoa> listHH;
    HangHoaAdapter hangHoaAdapter;
    SQLiteConnect sqLiteConnect;
    TextView tvTonKho, tvDaBan;
    private boolean isDaBanDescending = true;
    private boolean isTonKhoDescending = true;
    Spinner spnPhanLoai;
    ArrayAdapter<String> adapterPhanLoai;
    ArrayList<String> listLoaiSP = new ArrayList<>();
    ArrayList<HangHoa> filteredList = new ArrayList<>();
    private EditText edtTimKiem;
    Button btnLichSu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hang_hoa);

        lvHangHoa = findViewById(R.id.lvHangHoa);
        tvTonKho = findViewById(R.id.tvTonKho);
        tvDaBan = findViewById(R.id.tvDaBan);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        imgThoat = findViewById(R.id.imgThoat);
        imgThemHH = findViewById(R.id.imgThemHH);
        btnLichSu = findViewById(R.id.btnLichSu);
        btnLichSu.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivityHangHoa.this, LichSuHeThongActivity.class);
            startActivity(intent);
        });


        listHH = new ArrayList<>();
        sqLiteConnect = new SQLiteConnect(getBaseContext(), "appquanlybanhang.sql", null, 1);
        String query = "CREATE TABLE IF NOT EXISTS hanghoa (" +
                "key INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "hinhAnh TEXT , " +
                "tenSP TEXT NOT NULL, " +
                "giaSP INTEGER, " +
                "daBan INTEGER, " +
                "tonKho INTEGER, " +
                "noiDungSP TEXT NOT NULL, " +
                "loaiSP TEXT NOT NULL)";
        sqLiteConnect.queryData(query);

        String createHistoryTable = "CREATE TABLE IF NOT EXISTS lichsu (" +
                "key INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenHH TEXT, " +
                "thoiGian TEXT, " +
                "chucNang TEXT)";
        sqLiteConnect.queryData(createHistoryTable);


        listLoaiSP.add("Tất cả");
        listLoaiSP.add("Mỹ phẩm");
        listLoaiSP.add("Thực phẩm");
        listLoaiSP.add("Quần");
        listLoaiSP.add("Áo");

        adapterPhanLoai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiSP);
        adapterPhanLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPhanLoai = findViewById(R.id.spnPhanLoai);
        spnPhanLoai.setAdapter(adapterPhanLoai);

        hangHoaAdapter = new HangHoaAdapter(MainActivityHangHoa.this, R.layout.lv_hanghoa, listHH);
        lvHangHoa.setAdapter(hangHoaAdapter);

        loadDataHH();

        imgThemHH.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent themMoiIntent = new Intent(MainActivityHangHoa.this, ThemMoiHangHoaActivity.class);
                ThemMoiHHLauncher.launch (themMoiIntent);
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String query = charSequence.toString().toLowerCase();
                filterList(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvDaBan.setOnClickListener(v -> {
            if (isDaBanDescending) {
                loadDataHHSorted("daBan DESC");
                Toast.makeText(MainActivityHangHoa.this, "Sắp xếp Đã bán giảm dần", Toast.LENGTH_SHORT).show();
            } else {
                loadDataHHSorted("daBan ASC");
                Toast.makeText(MainActivityHangHoa.this, "Sắp xếp Đã bán tăng dần", Toast.LENGTH_SHORT).show();
            }
            isDaBanDescending = !isDaBanDescending;
        });

        tvTonKho.setOnClickListener(v -> {
            if (isTonKhoDescending) {
                loadDataHHSorted("tonKho DESC");
                Toast.makeText(MainActivityHangHoa.this, "Sắp xếp Tồn kho giảm dần", Toast.LENGTH_SHORT).show();
            } else {
                loadDataHHSorted("tonKho ASC");
                Toast.makeText(MainActivityHangHoa.this, "Sắp xếp Tồn kho tăng dần", Toast.LENGTH_SHORT).show();
            }
            isTonKhoDescending = !isTonKhoDescending;
        });

        lvHangHoa.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent xemChiTietIntent = new Intent(MainActivityHangHoa.this, XemChiTietActivity.class);
            Bundle data = new Bundle();
            HangHoa hh = hangHoaAdapter.getListHH().get(i);
            data.putSerializable("hh_value", hh);
            xemChiTietIntent.putExtras(data);
            startActivity(xemChiTietIntent);
            Toast.makeText(MainActivityHangHoa.this, "Xem chi tiết " + hh.getTenSP(), Toast.LENGTH_SHORT).show();
        });

        spnPhanLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedLoai = listLoaiSP.get(position);
                if (selectedLoai.equals("Tất cả")) {
                    loadDataHH();  // Nếu chọn "Tất cả", hiển thị tất cả sản phẩm
                } else {
                    filterByLoaiSP(selectedLoai);  // Nếu chọn một loại, lọc theo loại đó
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void filterList(String query) {
        filteredList.clear();
        for (HangHoa hh : listHH) {
            if (hh.getTenSP().toLowerCase().contains(query)) {
                filteredList.add(hh);
            }
        }
        hangHoaAdapter = new HangHoaAdapter(MainActivityHangHoa.this, R.layout.lv_hanghoa, filteredList);
        lvHangHoa.setAdapter(hangHoaAdapter);
    }

    private void filterByLoaiSP(String loaiSP) {
        filteredList.clear();
        for (HangHoa hh : listHH) {
            if (hh.getLoaiSP().equalsIgnoreCase(loaiSP)) {
                filteredList.add(hh);
            }
        }
        hangHoaAdapter = new HangHoaAdapter(MainActivityHangHoa.this, R.layout.lv_hanghoa, filteredList);
        lvHangHoa.setAdapter(hangHoaAdapter);
    }


    public void loadDataHH() {
        loadDataHHSorted(null);
        // Đảm bảo adapter sử dụng danh sách gốc (listHH)
        hangHoaAdapter = new HangHoaAdapter(MainActivityHangHoa.this, R.layout.lv_hanghoa, listHH);
        lvHangHoa.setAdapter(hangHoaAdapter);
    }

    public void loadDataHHSorted(String sortOrder) {
        String query = "SELECT * FROM hanghoa";
        if (sortOrder != null) {
            query += " ORDER BY " + sortOrder;
        }
        Cursor data = sqLiteConnect.getData(query);
        listHH.clear();
        while (data.moveToNext()) {
            try {
                int key = data.getInt(0);
                String hinhAnh = data.getString(1);
                String tenSP = data.getString(2);
                int giaSP = data.getInt(3);
                int daBan = data.getInt(4);
                int tonKho = data.getInt(5);
                String noiDungSP = data.getString(6);
                String loaiSP = data.getString(7);
                HangHoa hh = new HangHoa(key, hinhAnh, tenSP, giaSP, daBan, tonKho, noiDungSP, loaiSP);
                listHH.add(hh);
            } catch (Exception e) {
                Log.d("Lỗi đọc dữ liệu", e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        hangHoaAdapter.notifyDataSetChanged();




    }


    ActivityResultLauncher<Intent> ThemMoiHHLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        loadDataHH();
                    }
                }
            }
    );




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem menuThemMoiHH = menu.findItem(R.id.menuThemMoiHH);
        menuThemMoiHH.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent themMoiHHIntent = new Intent(MainActivityHangHoa.this, ThemMoiHangHoaActivity.class);
                ThemMoiHHLauncher.launch(themMoiHHIntent);
                return false;

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 123) {
            loadDataHH();
        }
    }

}