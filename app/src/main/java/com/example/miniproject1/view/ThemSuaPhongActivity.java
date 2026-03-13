package com.example.miniproject1.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.miniproject1.R;
import com.example.miniproject1.controller.PhongController;
import com.example.miniproject1.model.Phong;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * VIEW - Màn hình Thêm / Sửa thông tin phòng trọ.
 * Nhận mode = "them" (thêm mới) hoặc "sua" (chỉnh sửa) từ Intent.
 */
public class ThemSuaPhongActivity extends AppCompatActivity {

    // ── UI Components ─────────────────────────────────────────────────────────
    private TextInputLayout   tilMaPhong, tilTenPhong, tilGiaThue, tilTenNguoiThue;
    private TextInputEditText etMaPhong, etTenPhong, etGiaThue, etTenNguoiThue, etSoDienThoai;
    private RadioGroup        rgTinhTrang;
    private RadioButton       rbConTrong, rbDaThue;
    private Button            btnLuu;

    // ── Dữ liệu điều hướng ───────────────────────────────────────────────────
    private PhongController controller;
    private String mode;      // "them" hoặc "sua"
    private int    position;  // Vị trí phòng cần sửa (-1 nếu thêm mới)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_phong);

        // ── Toolbar ──────────────────────────────────────────────────────────
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // ── Lấy dữ liệu từ Intent ────────────────────────────────────────────
        controller = PhongController.getInstance();
        mode       = getIntent().getStringExtra("mode");
        position   = getIntent().getIntExtra("position", -1);

        khoiTaoViews();
        datTieuDeToolbar();

        // Nếu đang sửa -> đổ dữ liệu hiện tại vào form
        if ("sua".equals(mode) && position >= 0) {
            dienDuLieuVaoForm();
        }

        btnLuu.setOnClickListener(v -> luuPhong());
    }

    /** Ánh xạ tất cả View từ layout XML */
    private void khoiTaoViews() {
        tilMaPhong      = findViewById(R.id.tilMaPhong);
        tilTenPhong     = findViewById(R.id.tilTenPhong);
        tilGiaThue      = findViewById(R.id.tilGiaThue);
        tilTenNguoiThue = findViewById(R.id.tilTenNguoiThue);

        etMaPhong      = findViewById(R.id.etMaPhong);
        etTenPhong     = findViewById(R.id.etTenPhong);
        etGiaThue      = findViewById(R.id.etGiaThue);
        etTenNguoiThue = findViewById(R.id.etTenNguoiThue);
        etSoDienThoai  = findViewById(R.id.etSoDienThoai);

        rgTinhTrang = findViewById(R.id.rgTinhTrang);
        rbConTrong  = findViewById(R.id.rbConTrong);
        rbDaThue    = findViewById(R.id.rbDaThue);
        btnLuu      = findViewById(R.id.btnLuu);
    }

    /** Đặt tiêu đề Toolbar phù hợp với mode */
    private void datTieuDeToolbar() {
        if (getSupportActionBar() != null) {
            if ("them".equals(mode)) {
                getSupportActionBar().setTitle("Thêm phòng mới");
            } else {
                getSupportActionBar().setTitle("Sửa thông tin phòng");
            }
        }
    }

    /** Điền thông tin phòng hiện tại vào form (dùng khi mode = "sua") */
    private void dienDuLieuVaoForm() {
        Phong phong = controller.getPhong(position);
        etMaPhong.setText(phong.getMaPhong());
        etTenPhong.setText(phong.getTenPhong());
        // Hiển thị giá nguyên không có phần thập phân nếu là số nguyên
        long giaLong = (long) phong.getGiaThue();
        etGiaThue.setText(phong.getGiaThue() == giaLong
                ? String.valueOf(giaLong)
                : String.valueOf(phong.getGiaThue()));
        etTenNguoiThue.setText(phong.getTenNguoiThue());
        etSoDienThoai.setText(phong.getSoDienThoai());

        if (phong.isDaThue()) {
            rbDaThue.setChecked(true);
        } else {
            rbConTrong.setChecked(true);
        }
    }

    /** Validate dữ liệu và lưu phòng (thêm mới hoặc cập nhật) */
    private void luuPhong() {
        // Xóa các lỗi cũ
        tilMaPhong.setError(null);
        tilTenPhong.setError(null);
        tilGiaThue.setError(null);
        tilTenNguoiThue.setError(null);

        // ── Lấy giá trị từ form ───────────────────────────────────────────
        String maPhong      = getText(etMaPhong);
        String tenPhong     = getText(etTenPhong);
        String giaThueStr   = getText(etGiaThue);
        String tenNguoiThue = getText(etTenNguoiThue);
        String soDienThoai  = getText(etSoDienThoai);
        boolean daThue      = rbDaThue.isChecked();

        // ── Validate ──────────────────────────────────────────────────────
        boolean valid = true;

        if (TextUtils.isEmpty(maPhong)) {
            tilMaPhong.setError("Vui lòng nhập mã phòng");
            valid = false;
        }
        if (TextUtils.isEmpty(tenPhong)) {
            tilTenPhong.setError("Vui lòng nhập tên phòng");
            valid = false;
        }

        double giaThue = 0;
        if (TextUtils.isEmpty(giaThueStr)) {
            tilGiaThue.setError("Vui lòng nhập giá thuê");
            valid = false;
        } else {
            try {
                giaThue = Double.parseDouble(giaThueStr);
                if (giaThue <= 0) {
                    tilGiaThue.setError("Giá thuê phải lớn hơn 0");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                tilGiaThue.setError("Giá thuê không hợp lệ");
                valid = false;
            }
        }

        // Nếu chọn Đã thuê thì tên người thuê là bắt buộc
        if (daThue && TextUtils.isEmpty(tenNguoiThue)) {
            tilTenNguoiThue.setError("Vui lòng nhập tên người thuê");
            valid = false;
        }

        if (!valid) return;

        // ── Tạo đối tượng Phong và lưu qua Controller ─────────────────────
        Phong phong = new Phong(maPhong, tenPhong, giaThue, daThue, tenNguoiThue, soDienThoai);

        if ("them".equals(mode)) {
            controller.themPhong(phong);
            Toast.makeText(this, "✅ Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            controller.suaPhong(position, phong);
            Toast.makeText(this, "✅ Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
        }

        // Trả kết quả RESULT_OK để MainActivity biết cần refresh danh sách
        setResult(RESULT_OK);
        finish();
    }

    /** Helper: lấy text từ TextInputEditText, trả về chuỗi rỗng nếu null */
    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    /** Xử lý nút Back trên Toolbar */
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
