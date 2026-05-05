package com.library.library_manager.service;

import com.library.library_manager.dto.staff.StaffResponseDTO;
import com.library.library_manager.entity.Position;
import com.library.library_manager.entity.Staff;
import com.library.library_manager.entity.User;
import com.library.library_manager.exception.AppException;
import com.library.library_manager.exception.ErrorCode;
import com.library.library_manager.repository.IPositionRepository;
import com.library.library_manager.repository.IStaffRepository;
import com.library.library_manager.repository.IUserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffService {

    IStaffRepository staffRepository;
    IUserRepository userRepository;
    IPositionRepository positionRepository;

    public List<StaffResponseDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(staff -> StaffResponseDTO.builder()
                        .staffId(staff.getStaffId())
                        .fullName(staff.getUser() != null ? staff.getUser().getFullName() : null)
                        .email(staff.getUser() != null ? staff.getUser().getEmail() : null)
                        .positionName(staff.getPosition() != null ? staff.getPosition().getPositionName() : "No Position")
                        .build())
                .collect(Collectors.toList());
    }

    // 1. Tạo nhân sự mới
    @Transactional
    public Staff createStaff(Staff staffRequest) {
        // 1. Kiểm tra xem User này đã tồn tại trong bảng Staff chưa
        if (staffRepository.existsById(staffRequest.getStaffId())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 2. Tìm User từ DB (Bắt buộc để Hibernate quản lý quản lý session của User này)
        User user = userRepository.findById(staffRequest.getStaffId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Tìm Position từ DB
        Position position = positionRepository.findById(staffRequest.getPosition().getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        // 4. Tạo đối tượng Staff mới và thiết lập quan hệ
        // KHÔNG dùng builder từ request gửi lên trực tiếp để tránh mang theo trạng thái cũ
        Staff newStaff = new Staff();
        newStaff.setUser(user);        // MapsId sẽ tự lấy ID từ User gán cho staffId
        newStaff.setPosition(position);

        // 5. Lưu Staff (Hibernate sẽ thực hiện lệnh INSERT đơn giản)
        return staffRepository.save(newStaff);
    }

    // 2. Cập nhật nhân sự
    @Transactional
    public Staff updateStaff(Long staffId, Staff staffDetails) {
        // BƯỚC 1: Tìm Staff gốc từ DB. Hibernate sẽ "quản lý" (Track) đối tượng này.
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

        // BƯỚC 2: Cập nhật Position nếu có
        if (staffDetails.getPosition() != null && staffDetails.getPosition().getPositionId() != null) {
            Position newPos = positionRepository.findById(staffDetails.getPosition().getPositionId())
                    .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
            existingStaff.setPosition(newPos);
        }

        // BƯỚC 3: Cập nhật thông tin User (nếu cần)
        if (staffDetails.getUser() != null) {
            User user = existingStaff.getUser(); // Lấy user từ staff đang quản lý
            user.setFullName(staffDetails.getUser().getFullName());
            // Không set ID cho User ở đây vì ID là bất biến
        }

        // BƯỚC 4: Chỉ save đối tượng 'existingStaff'
        return staffRepository.save(existingStaff);
    }

    // 3. Xóa nhân sự
    @Transactional
    public void deleteStaff(Long staffId) {
        // Kiểm tra tồn tại trước khi xóa, dùng AppException thay cho RuntimeException
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

        staffRepository.delete(staff);
    }
}