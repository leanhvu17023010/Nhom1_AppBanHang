package com.example.spck.Customer.AdapterCustomer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spck.Customer.Activities.EditCustomerActivity;
import com.example.spck.Customer.database.CustomerDatabase;
import com.example.spck.Customer.model.Customer;
import com.example.demo.R;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    private Context context;
    private List<Customer> customerList;
    private CustomerDatabase databaseHelper;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.databaseHelper = new CustomerDatabase(context); // Khởi tạo cơ sở dữ liệu
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return customerList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_customer, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.imgDeleteCustomer = convertView.findViewById(R.id.imgDeleteCustomer);
            viewHolder.imgEditCustomer = convertView.findViewById(R.id.imgEditCustomer); // Thêm ImageView sửa

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin khách hàng từ danh sách
        Customer customer = customerList.get(position);

        if (customer != null) {
            // Hiển thị tên khách hàng
            viewHolder.tvName.setText(customer.getName());

            // Sự kiện xóa khách hàng
            viewHolder.imgDeleteCustomer.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa khách hàng này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            boolean isDeleted = databaseHelper.deleteCustomer(customer.getId());
                            if (isDeleted) {
                                // Tải lại danh sách từ cơ sở dữ liệu
                                customerList.clear();
                                customerList.addAll(databaseHelper.getAllCustomers());
                                notifyDataSetChanged();  // Cập nhật giao diện
                                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

            // Sự kiện sửa khách hàng
            viewHolder.imgEditCustomer.setOnClickListener(v -> {
                // Lấy đối tượng Customer từ danh sách
                Customer selectedCustomer = customerList.get(position);

                // Kiểm tra nếu đối tượng selectedCustomer không phải là null
                if (selectedCustomer != null) {
                    // Tạo Intent để mở màn hình EditCustomerActivity
                    Intent intent = new Intent(context, EditCustomerActivity.class);

                    // Truyền thông tin khách hàng vào EditCustomerActivity
                    intent.putExtra("CUSTOMER_ID", selectedCustomer.getId());  // Truyền ID của khách hàng
                    intent.putExtra("customerName", selectedCustomer.getName());
                    intent.putExtra("customerPhone", selectedCustomer.getPhone());
                    intent.putExtra("customerAddress", selectedCustomer.getAddress());
                    intent.putExtra("customerEmail", selectedCustomer.getEmail());
                    intent.putExtra("customerBirthDate", selectedCustomer.getBirthDate());
                    intent.putExtra("customerNotes", selectedCustomer.getNotes());

                    // Mở màn hình EditCustomerActivity
                    context.startActivity(intent);  // Dùng context để startActivity thay vì gọi từ DanhSachKhachHang
                } else {
                    // Nếu không tìm thấy khách hàng, hiển thị thông báo lỗi
                    Toast.makeText(context, "Không tìm thấy khách hàng!", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        ImageView imgDeleteCustomer;
        ImageView imgEditCustomer; // Thêm thùng rác sửa
    }

    @Override
    protected void finalize() throws Throwable {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.finalize();
    }
}
