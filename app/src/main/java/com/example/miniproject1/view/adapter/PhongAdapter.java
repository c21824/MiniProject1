package com.example.miniproject1.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject1.R;
import com.example.miniproject.model.Phong;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * ADAPTER - Cầu nối giữa dữ liệu List<Phong> và RecyclerView hiển thị trên UI.
 * Thuộc tầng View trong kiến trúc MVC.
 */
public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder> {

    // ── Interface callback để Activity xử lý sự kiện ─────────────────────────
    public interface OnItemClickListener {
        void onItemClick(int position);       // Click -> Sửa phòng
        void onItemLongClick(int position);   // Giữ  -> Xóa phòng
    }

    private final Context context;
    private final List<Phong> danhSachPhong;
    private final OnItemClickListener listener;

    public PhongAdapter(Context context, List<Phong> danhSachPhong, OnItemClickListener listener) {
        this.context = context;
        this.danhSachPhong = danhSachPhong;
        this.listener = listener;
    }

    // ── Tạo ViewHolder từ layout item_phong.xml ───────────────────────────────
    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong, parent, false);
        return new PhongViewHolder(view);
    }

    // ── Gán dữ liệu vào từng item ─────────────────────────────────────────────
    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
        Phong phong = danhSachPhong.get(position);

        holder.tvMaPhong.setText("Mã: " + phong.getMaPhong());
        holder.tvTenPhong.setText(phong.getTenPhong());

        // Format giá tiền kiểu Việt Nam: 3.500.000 VNĐ/tháng
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvGiaThue.setText(nf.format(phong.getGiaThue()) + " VNĐ/tháng");

        // ── Tô màu trạng thái (Xanh = Còn trống / Đỏ = Đã thuê) ─────────────
        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setCornerRadius(32f);

        if (phong.isDaThue()) {
            badgeBg.setColor(Color.parseColor("#FFEBEE")); // Đỏ nhạt
            holder.tvTinhTrang.setTextColor(Color.parseColor("#C62828")); // Đỏ đậm
            holder.tvTinhTrang.setText("● Đã thuê");
        } else {
            badgeBg.setColor(Color.parseColor("#E8F5E9")); // Xanh nhạt
            holder.tvTinhTrang.setTextColor(Color.parseColor("#2E7D32")); // Xanh đậm
            holder.tvTinhTrang.setText("● Còn trống");
        }
        holder.tvTinhTrang.setBackground(badgeBg);

        // ── Sự kiện click (sửa) và long click (xóa) ──────────────────────────
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_ID) {
                listener.onItemClick(pos);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_ID) {
                listener.onItemLongClick(pos);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    // ── ViewHolder: ánh xạ các View trong item_phong.xml ─────────────────────
    public static class PhongViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMaPhong;
        final TextView tvTenPhong;
        final TextView tvGiaThue;
        final TextView tvTinhTrang;

        public PhongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhong   = itemView.findViewById(R.id.tvMaPhong);
            tvTenPhong  = itemView.findViewById(R.id.tvTenPhong);
            tvGiaThue   = itemView.findViewById(R.id.tvGiaThue);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang);
        }
    }
}
