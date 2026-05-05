package com.library.library_manager.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SystemRule {

    // Định nghĩa các hằng số hệ thống
    MAX_BORROW_DAYS("14", "days", "Số ngày tối đa được mượn cho một lần mượn"),
    MAX_BORROW_BOOKS("5", "books", "Số lượng sách tối đa một sinh viên được giữ"),
    LATE_FEE_PER_DAY("5000", "VND", "Tiền phạt quá hạn trên mỗi ngày trả trễ"),
    RENEW_LIMIT("2", "times", "Số lần tối đa được phép gia hạn một cuốn sách"),
    WARNING_BEFORE_EXPIRY("2", "days", "Thời gian gửi thông báo trước khi sách hết hạn"),
    MIN_PASSWORD_LENGTH("8", "characters", "Độ dài mật khẩu tối thiểu của người dùng");

    String value;
    String unit;
    String description;

    /**
     * Chuyển đổi giá trị sang kiểu int
     */
    public int asInt() {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Chuyển đổi giá trị sang kiểu double
     */
    public double asDouble() {
        try {
            return Double.parseDouble(this.value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Chuyển đổi giá trị sang kiểu boolean
     */
    public boolean asBoolean() {
        return Boolean.parseBoolean(this.value);
    }
}