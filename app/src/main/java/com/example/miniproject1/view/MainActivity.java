package com.example.miniproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.R;
import com.example.miniproject.controller.PhongController;
import com.example.miniproject.view.adapter.PhongAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * VIEW - Màn hình chính: hiển thị danh sách phòng trọ bằng RecyclerView.
 * Xử lý các sự kiện: thêm (FAB), sửa (click), xóa (long click).
 */
public class MainActivity extends AppCompatActivity {

    private PhongAdapter adapter;
    private PhongController controller;
    private TextView tvEmpty;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ── Thiết lập Toolbar ────────────────────────────────────────────────
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ── Khởi tạo Controller và các View ──────────────────────────────────
        controller = PhongController.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        FloatingActionButton fabThem = findViewById(R.id.fabThem);

        // ── Thiết lập RecyclerView ────────────────────────────────────────────
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhongAdapter(
                this,
                controller.getDanhSachPhong(),
                new PhongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Click item -> Mở màn hình sửa thông tin phòng
                        Intent intent = new Intent(MainActivity.this, ThemSuaPhongActivity.class);
                        intent.putExtra("mode", "sua");
                        intent.putExtra("position", position);
                        activityLauncher.launch(intent);
                    }

                    @Override
                    public void onItemLongClick(int position) {
                        // Giữ item -> Hiện AlertDialog xác nhận xóa
                        showDeleteDialog(position);
                    }
                }
        );
        recyclerView.setAdapter(adapter);

        // ── Lắng nghe kết quả trả về từ ThemSuaPhongActivity ─────────────────
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        adapter.notifyDataSetChanged();
                        capNhatTrangThaiTrong();
                    }
                }
        );

        // ── FAB: Nhấn + để thêm phòng mới ────────────────────────────────────
        fabThem.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemSuaPhongActivity.class);
            intent.putExtra("mode", "them");
            activityLauncher.launch(intent);
        });

        capNhatTrangThaiTrong();
    }

    /**
     * Hiển thị / ẩn thông báo "Chưa có phòng nào" tuỳ theo danh sách rỗng hay không.
     */
    private void capNhatTrangThaiTrong() {
        if (controller.soLuongPhong() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * Hiện AlertDialog xác nhận trước khi xóa phòng.
     */
    private void showDeleteDialog(int position) {
        String tenPhong = controller.getPhong(position).getTenPhong();

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng\n\"" + tenPhong + "\" không?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    controller.xoaPhong(position);
                    adapter.notifyDataSetChanged();
                    capNhatTrangThaiTrong();
                    Toast.makeText(this,
                            "Đã xóa phòng \"" + tenPhong + "\"",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
