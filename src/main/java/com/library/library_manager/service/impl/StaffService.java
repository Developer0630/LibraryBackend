package com.library.library_manager.service.impl;

import com.library.library_manager.dto.staff.StaffResponseDTO;
import com.library.library_manager.entity.Position;
import com.library.library_manager.entity.Staff;
import com.library.library_manager.entity.User;
import com.library.library_manager.exception.AppException;
import com.library.library_manager.exception.ErrorCode;
import com.library.library_manager.repository.IPositionRepository;
import com.library.library_manager.repository.IStaffRepository;
import com.library.library_manager.repository.IUserRepository;
import com.library.library_manager.service.IStaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffService implements IStaffService {

    IStaffRepository staffRepository;
    IUserRepository userRepository;
    IPositionRepository positionRepository;

    @Override
    public List<StaffResponseDTO> findAll() {
        return staffRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StaffResponseDTO createStaff(Staff staffRequest) {
        if (staffRepository.existsById(staffRequest.getStaffId())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userRepository.findById(staffRequest.getStaffId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Position position = positionRepository.findById(staffRequest.getPosition().getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        Staff newStaff = new Staff();
        newStaff.setUser(user);
        newStaff.setPosition(position);

        return mapToResponseDTO(staffRepository.save(newStaff));
    }

    @Override
    @Transactional
    public StaffResponseDTO updateStaff(Long staffId, Staff staffDetails) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

        if (staffDetails.getPosition() != null && staffDetails.getPosition().getPositionId() != null) {
            Position newPos = positionRepository.findById(staffDetails.getPosition().getPositionId())
                    .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
            existingStaff.setPosition(newPos);
        }

        if (staffDetails.getUser() != null) {
            User user = existingStaff.getUser();
            user.setFullName(staffDetails.getUser().getFullName());
        }

        return mapToResponseDTO(staffRepository.save(existingStaff));
    }

    @Override
    @Transactional
    public void deleteStaff(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
        staffRepository.delete(staff);
    }

    // Hàm chuyển đổi sang DTO để ngắt đệ quy JSON
    private StaffResponseDTO mapToResponseDTO(Staff staff) {
        return StaffResponseDTO.builder()
                .staffId(staff.getStaffId())
                .fullName(staff.getUser().getFullName())
                .positionName(staff.getPosition().getPositionName())
                .username(staff.getUser().getUserName())
                .build();
    }
}