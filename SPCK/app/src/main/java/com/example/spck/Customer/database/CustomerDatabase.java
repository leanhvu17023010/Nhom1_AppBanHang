package com.example.spck.Customer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.spck.Customer.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "customer_data.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_NAME = "customer";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_BIRTHDATE = "birthdate";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_PHOTO = "photo";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_BIRTHDATE + " TEXT, " +
                    COLUMN_NOTES + " TEXT, " +
                    COLUMN_PHOTO + " BLOB)";

    // Constructor
    public CustomerDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            String ALTER_TABLE = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PHOTO + " BLOB";
            db.execSQL(ALTER_TABLE);
        }
    }

    // Phương thức lấy tất cả khách hàng từ cơ sở dữ liệu
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {

            Log.d("Database", "Số lượng khách hàng trong cơ sở dữ liệu: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    Log.d("Database", "Adding customer: ID = " + customer.getId() + ", Name = " + customer.getName());
                    customerList.add(customer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database", "Error while fetching customers: " + e.getMessage());
        }

        Log.d("Database", "Số lượng khách hàng trong list: " + customerList.size());
        return customerList;
    }


    // Thêm khách hàng vào cơ sở dữ liệu
    public boolean addCustomer(Customer customer) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, customer.getName());
            values.put(COLUMN_PHONE, customer.getPhone());
            values.put(COLUMN_ADDRESS, customer.getAddress());
            values.put(COLUMN_EMAIL, customer.getEmail());
            values.put(COLUMN_BIRTHDATE, customer.getBirthDate());
            values.put(COLUMN_NOTES, customer.getNotes());


            long result = db.insert(TABLE_NAME, null, values);
            if (result == -1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Xóa khách hàng theo ID
    public boolean deleteCustomer(int id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            return rowsDeleted > 0;
        } catch (Exception e) {
            Log.e("Database", "Error while deleting customer: " + e.getMessage());
            return false;
        }
    }

    // Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(
                     TABLE_NAME,
                     new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_ADDRESS, COLUMN_EMAIL, COLUMN_BIRTHDATE, COLUMN_NOTES, COLUMN_PHOTO},
                     COLUMN_ID + "=?",
                     new String[]{String.valueOf(customerId)},
                     null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                customer.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDATE)));
                customer.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));

                // Kiểm tra null trước khi lấy ảnh
                byte[] photo = cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)) ? null : cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
                customer.setPhoto(photo);
            }
        } catch (Exception e) {
            Log.e("Database", "Error while fetching customer by ID: " + e.getMessage());
        }
        return customer;
    }

    // Cập nhật thông tin khách hàng
    public boolean updateCustomer(Customer customer) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, customer.getName());
            values.put(COLUMN_PHONE, customer.getPhone());
            values.put(COLUMN_ADDRESS, customer.getAddress());
            values.put(COLUMN_EMAIL, customer.getEmail());
            values.put(COLUMN_BIRTHDATE, customer.getBirthDate());
            values.put(COLUMN_NOTES, customer.getNotes());

            // Kiểm tra nếu ảnh mới có sẵn, nếu không giữ nguyên ảnh cũ
            if (customer.getPhoto() != null) {
                values.put(COLUMN_PHOTO, customer.getPhoto()); // Lưu ảnh mới
            } else {
                // Không cập nhật ảnh nếu không có ảnh mới
                // Giữ nguyên ảnh cũ, không cần thay đổi trường ảnh
            }

            // Cập nhật thông tin khách hàng trong cơ sở dữ liệu theo ID
            int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(customer.getId())});
            return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
        } catch (Exception e) {
            Log.e("Database", "Error while updating customer: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }

    // Tìm kiếm khách hàng theo tên
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customerList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE ?";
        String searchKeyword = "%" + keyword + "%";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{searchKeyword})) {

            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                    customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
                    customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                    customer.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDATE)));
                    customer.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));

                    byte[] photo = cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)) ? null : cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
                    customer.setPhoto(photo);

                    customerList.add(customer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database", "Error while searching customers by name: " + e.getMessage());
        }
        return customerList;
    }

    public boolean isEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM Customer WHERE email = ?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }


}
