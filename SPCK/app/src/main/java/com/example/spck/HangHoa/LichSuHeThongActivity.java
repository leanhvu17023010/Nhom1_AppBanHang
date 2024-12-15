package com.example.spck.HangHoa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.demo.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LichSuHeThongActivity extends AppCompatActivity {

    ListView lvLichSu;
    SQLiteConnect sqLiteConnect;
    Button btnExport;
    ImageView imgThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_he_thong);

        lvLichSu = findViewById(R.id.lvLichSu);
        btnExport = findViewById(R.id.btnExport);
        imgThoat = findViewById(R.id.imgThoat);

        sqLiteConnect = new SQLiteConnect(this, "appquanlybanhang.sql", null, 1);

        loadLichSu();
        requestStoragePermissions();

        btnExport.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                exportDataToCSV();
            } else {
                requestStoragePermissions();
            }
        });

        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadLichSu() {
        String query = "SELECT * FROM lichsu ORDER BY key DESC";
        Cursor data = sqLiteConnect.getData(query);

        ArrayList<String> lichSuList = new ArrayList<>();
        while (data.moveToNext()) {
            String tenHH = data.getString(1);
            String thoiGian = data.getString(2);
            String chucNang = data.getString(3);

            lichSuList.add(tenHH + " | " + thoiGian + " | " + chucNang);
        }

        CustomAdapter adapter = new CustomAdapter(lichSuList);
        lvLichSu.setAdapter(adapter);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
    }

    private void exportDataToCSV() {
        File exportDir = new File(getExternalFilesDir(null), "ExportedData");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            showToast("Không thể tạo thư mục trong Downloads. Export thất bại!");
            return;
        }

        File file = new File(exportDir, "lichsu.csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Ten Hang Hoa,Thoi Gian,Chuc Nang\n"); // Tiêu đề CSV
            Cursor data = sqLiteConnect.getData("SELECT * FROM lichsu ORDER BY key DESC");

            if (data == null || !data.moveToFirst()) {
                showToast("Không có dữ liệu để export.");
                return;
            }

            do {
                String tenHH = data.getString(1);
                String thoiGian = data.getString(2);
                String chucNang = data.getString(3);

                // Log data to debug
                Log.d("ExportData", "Ten Hang Hoa: " + tenHH + ", Thoi Gian: " + thoiGian + ", Chuc Nang: " + chucNang);

                writer.append(tenHH)
                        .append(',')
                        .append(thoiGian)
                        .append(',')
                        .append(chucNang)
                        .append('\n');
            } while (data.moveToNext());

            // Log file path to debug
            Log.d("Export", "File path: " + file.getAbsolutePath());
            showToast("Export thành công: " + file.getAbsolutePath());

            openCSVFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Export thất bại: " + e.getMessage());
        }
    }

    private void openCSVFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "text/csv");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            showToast("Không có ứng dụng hỗ trợ mở file CSV.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportDataToCSV();
        } else {
            showToast("Quyền bị từ chối, không thể export dữ liệu.");
        }
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> listData;

        public CustomAdapter(ArrayList<String> listData) {
            super(LichSuHeThongActivity.this, R.layout.lv_lsht, listData);
            this.listData = listData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_lsht, parent, false);
            }

            String item = listData.get(position);
            String[] parts = item.split(" \\| ");

            TextView tvTenHH = convertView.findViewById(R.id.tvTenHH);
            TextView tvThoiGian = convertView.findViewById(R.id.tvThoiGian);
            TextView tvChucNang = convertView.findViewById(R.id.tvChucNang);

            tvTenHH.setText(parts[0]);
            tvThoiGian.setText(parts[1]);
            tvChucNang.setText(parts[2]);

            return convertView;
        }
    }


}
