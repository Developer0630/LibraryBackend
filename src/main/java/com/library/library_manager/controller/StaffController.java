package com.library.library_manager.controller;

import com.library.library_manager.dto.staff.StaffResponseDTO;
import com.library.library_manager.entity.Staff;
import com.library.library_manager.service.impl.StaffService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    StaffService staffService;

    @GetMapping
    public List<StaffResponseDTO> getStaffList() {
        return staffService.findAll();
    }

    // POST: Tạo mới
    @PostMapping
    public ResponseEntity<StaffResponseDTO> createStaff(@RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.createStaff(staff));
    }

    // PUT: Cập nhật theo staffId
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable("staffId") Long staffId, @RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, staff));
    }

    // DELETE: Xóa theo staffId
    @DeleteMapping("/{staffId}")
    public ResponseEntity<String> deleteStaff(@PathVariable("staffId") Long staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok("Deleted staff with id: " + staffId); //khng cần trả về
    }
}