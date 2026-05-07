package com.library.library_manager.controller;

import com.library.library_manager.dto.ReservationRequestDTO;
import com.library.library_manager.dto.ReservationResponse;
import com.library.library_manager.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final StudentService studentService;
    private final String CURRENT_USER = "SV001";

    // 1. Tạo yêu cầu đặt trước (Sử dụng ReservationRequestDTO)
    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(studentService.createReservation(dto, CURRENT_USER));
    }

    // 2. Xem danh sách đặt trước
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        // Tận dụng hàm getReservations đã viết ở module 3.4
        return ResponseEntity.ok(studentService.getReservations(CURRENT_USER));
    }

    // 3. Xem chi tiết một yêu cầu đặt trước
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getDetail(@PathVariable Long reservationId) {
        return ResponseEntity.ok(studentService.getReservationDetail(reservationId, CURRENT_USER));
    }

    // 4. Hủy đặt trước
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancel(@PathVariable Long reservationId) {
        studentService.cancelReservation(reservationId, CURRENT_USER);
        return ResponseEntity.ok("Hủy đặt trước thành công.");
    }
}