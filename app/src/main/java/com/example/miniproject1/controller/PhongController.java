package com.example.miniproject.controller;

import com.example.miniproject.model.Phong;

import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER - Quản lý toàn bộ dữ liệu phòng trọ trong bộ nhớ (List).
 * Sử dụng Singleton để đảm bảo chỉ có một instance duy nhất trong ứng dụng.
 */
public class PhongController {

    private static PhongController instance;
    private final List<Phong> danhSachPhong;

    private PhongController() {
        danhSachPhong = new ArrayList<>();
    }

    /** Lấy instance duy nhất của Controller */
    public static PhongController getInstance() {
        if (instance == null) {
            instance = new PhongController();
        }
        return instance;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** Trả về toàn bộ danh sách phòng */
    public List<Phong> getDanhSachPhong() {
        return danhSachPhong;
    }

    /** Lấy một phòng theo vị trí trong danh sách */
    public Phong getPhong(int index) {
        return danhSachPhong.get(index);
    }

    /** Trả về tổng số phòng */
    public int soLuongPhong() {
        return danhSachPhong.size();
    }

    // ── Create ────────────────────────────────────────────────────────────────

    /** Thêm một phòng mới vào danh sách */
    public void themPhong(Phong phong) {
        danhSachPhong.add(phong);
    }

    // ── Update ────────────────────────────────────────────────────────────────

    /** Cập nhật thông tin một phòng theo vị trí */
    public void suaPhong(int index, Phong phong) {
        danhSachPhong.set(index, phong);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    /** Xóa một phòng khỏi danh sách theo vị trí */
    public void xoaPhong(int index) {
        danhSachPhong.remove(index);
    }
}
