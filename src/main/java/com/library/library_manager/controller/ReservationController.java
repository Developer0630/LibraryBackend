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

    // 1. Tạo yêu cầu đặt trước (Sử dụng ReservationRequestDTO)
    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationRequestDTO dto,
            @RequestParam("username") String username) {
        return ResponseEntity.ok(studentService.createReservation(dto, username));
    }

    // 2. Xem danh sách đặt trước
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll(@RequestParam("username") String username) {
        return ResponseEntity.ok(studentService.getReservations(username));
    }

    // 3. Xem chi tiết một yêu cầu đặt trước
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getDetail(
            @PathVariable Long reservationId,
            @RequestParam("username") String username) { 
        return ResponseEntity.ok(studentService.getReservationDetail(reservationId, username));
    }

    // 4. Hủy đặt trước
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancel(
            @PathVariable Long reservationId,
            @RequestParam("username") String username) { 
        studentService.cancelReservation(reservationId, username);
        return ResponseEntity.ok("Hủy đặt trước thành công.");
    }
}