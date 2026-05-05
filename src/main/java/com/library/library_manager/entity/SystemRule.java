package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "system_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemRule {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "rule_id")
        Long id;

        @Column(name = "rule_key", unique = true, nullable = false, length = 100)
        String ruleKey; // Ví dụ: MAX_BORROW_DAYS, LATE_FEE_PER_DAY

        @Column(name = "rule_value", nullable = false, length = 255)
        String ruleValue; // Ví dụ: "14", "5000"

        @Column(name = "unit", length = 50)
        String unit; // Đơn vị: "days", "VND", "books"

        @Column(name = "data_type", length = 20)
        String dataType; // Để biết đường ép kiểu trong Java: "Integer", "Double", "Boolean"

        @Column(name = "description", columnDefinition = "TEXT")
        String description; // Giải thích quy tắc này dùng làm gì
}