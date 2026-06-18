package com.library.library_manager.controller;

import com.library.library_manager.dto.returns.ReturnRequestDTO;
import com.library.library_manager.dto.returns.ReturnResponseDTO;
import com.library.library_manager.dto.returns.ReturnScanResponseDTO;
import com.library.library_manager.service.impl.ReturnTransactionService; // 🌟 Gọi đúng Service này
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnController {

    //Đổi sang dùng trực tiếp ReturnTransactionService của bro
    private final ReturnTransactionService returnService;

    // Bước 1: Quét mã sách để lấy thông tin phiếu mượn & phạt dự kiến
    @PostMapping("/scan")
    public ResponseEntity<ReturnScanResponseDTO> scanBookForReturn(@RequestParam Long copyId) {
        return ResponseEntity.ok(returnService.scanBookForReturn(copyId));
    }

    // Bước 2 + 3: Xác nhận trả sách dứt điểm đơn, trả về ReturnResponseDTO chứa chi tiết biên lai trả sách
    @PostMapping
    public ResponseEntity<ReturnResponseDTO> processReturnBook(@RequestBody ReturnRequestDTO request) {
        return ResponseEntity.ok(returnService.processReturn(request));
    }
}