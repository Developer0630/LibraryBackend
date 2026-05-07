package com.library.library_manager.service.impl;

import com.library.library_manager.dto.*;
import com.library.library_manager.dto.student.StudentProfileResponseDTO;
import com.library.library_manager.dto.student.StudentRequestDTO;
import com.library.library_manager.dto.student.StudentResponseDTO;
import com.library.library_manager.entity.*;
import com.library.library_manager.exception.AppException;
import com.library.library_manager.exception.ErrorCode;
import com.library.library_manager.repository.*;
import com.library.library_manager.service.IStudentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements IStudentService {
    IStudentRepository studentRepository;
    IUserRepository userRepository;
    IRoleRepository roleRepository;

    private ILoanRepository loanRepository;
    private IViolationRepository violationRepository;

    private final IBorrowHistoryRepository borrowHistoryRepository;
    private final IReservationRepository reservationRepository;
    private final IFinePaymentRepository finePaymentRepository;
    private final INotificationRepository notificationRepository;

    @Override
    public PageResponse<StudentResponseDTO> getAll(int page, int size, String studentCode, String status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Student> studentPage = studentRepository.findAllWithFilters(studentCode, status, pageable);
        Page<StudentResponseDTO> responsePage = studentPage.map(this::mapToResponse);
        return new PageResponse<>(responsePage);
    }

    @Override
    public StudentResponseDTO getById(Long id) {
        return studentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        // 1. Tìm Role Entity từ database
        Role studentRole = roleRepository.findByRoleName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found in database"));

        // 2. Tạo User (Auth)
        Set<Role> roles = new HashSet<>();
        roles.add(studentRole);

        User user = User.builder()
                .userName(request.getStudentCode()) // Đảm bảo đúng tên field trong User entity (username/userName)
                .password("123456")
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roles(roles) // Gán Set roles (n-n)
                .build();
        user = userRepository.save(user);

        // 3. Tạo Student profile
        Student student = Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .major(request.getMajor())
                .clazz(request.getClazz())
                .status("ACTIVE")
                .totalDebt(0.0)
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        student.setMajor(request.getMajor());
        student.setClazz(request.getClazz());

        User user = student.getUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (!List.of("ACTIVE", "LOCKED").contains(status.toUpperCase())) {
            throw new AppException(ErrorCode.INVALID_STUDENT_STATUS);
        }

        student.setStatus(status.toUpperCase());
        studentRepository.save(student);
    }

    public void resetPassword(Long studentId, String newPassword) {
        // 1. Kiểm tra sinh viên tồn tại
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        // 2. Lấy User và cập nhật mật khẩu tùy chỉnh
        User user = student.getUser();

        user.setPassword(newPassword);

        // 3. Lưu vào DB
        userRepository.save(user);
    }

    @Override
    public List<BorrowHistoryDTO> getBorrowHistory(Long id) {
        return Collections.emptyList();
    }

    @Override
    public List<ViolationDTO> getViolations(Long id) {
        return Collections.emptyList();
    }

    // 1. Xem hồ sơ cá nhân
    private StudentResponseDTO mapToResponse(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phoneNumber(student.getUser().getPhoneNumber())
                .major(student.getMajor())
                .clazz(student.getClazz())
                .status(student.getStatus())
                .balance(student.getTotalDebt())
                .build();
    }

    public StudentProfileResponseDTO getProfileByUsername(String username) {
        // 1. Tìm sinh viên dựa trên username của tài khoản liên kết
        // findByUser_UserName là phương thức chúng ta đã thêm vào StudentRepository
        Student student = studentRepository.findByUser_UserName(username)
                .orElseThrow(() -> new RuntimeException("Student information not found for the account: " + username));

        // 2. Lấy thông tin User liên kết với sinh viên đó
        User user = student.getUser();

        // 3. Chuyển đổi sang DTO để trả về cho Controller
        // Đảm bảo thứ tự tham số khớp với Constructor trong StudentProfileResponse của bạn
        return new StudentProfileResponseDTO(
                user.getFullName(),
                student.getStudentCode(),
                user.getEmail(),
                user.getPhoneNumber(),
                student.getClazz(),
                student.getMajor(),
                student.getStatus()
        );
    }

    // 2. Xem sách đang mượn
    public List<BorrowedItemResponse> getBorrowedItems(String username) {
        return loanRepository.findByUser_UserNameAndReturnedAtIsNull(username).stream()
                .map(loan -> {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate().toLocalDate());
                    String status = (daysLeft < 0) ? "QUÁ HẠN" : (daysLeft <= 3 ? "SẮP ĐẾN HẠN" : "ĐANG MƯỢN");
                    return new BorrowedItemResponse(loan.getBookCopy().getBook().getTitle(),
                            loan.getBorrowDate(), loan.getDueDate(), status);
                }).collect(Collectors.toList());
    }

    // 3. Xem vi phạm
    public List<ViolationResponse> getViolations(String username) {
        return violationRepository.findByLoan_User_UserName(username).stream()
                .map(v -> new ViolationResponse(v.getType(), v.getFineAmount(),
                        v.getIsPaid(), v.getLoan().getBookCopy().getBook().getTitle()))
                .collect(Collectors.toList());
    }

    // Xem lịch sử mượn trả
    public List<HistoryResponse> getBorrowHistory(String username) {
        return borrowHistoryRepository.findByUser_UserNameOrderByBorrowDateDesc(username).stream()
                .map(l -> new HistoryResponse(l.getBookCopy().getBook().getTitle(),
                        l.getBorrowDate(), l.getReturnedAt(), l.getStatus(), l.getStaffNote()))
                .collect(Collectors.toList());
    }

    // Xem đặt trước
    public List<ReservationResponse> getReservations(String username) {
        // Gọi đúng tên hàm dài hơn một chút nhưng cực kỳ chính xác
        return reservationRepository.findByStudent_User_UserNameOrderByRequestDateDesc(username).stream()
                .map(r -> new ReservationResponse(
                        r.getBook().getTitle(),
                        r.getRequestDate(), // Trong Entity bạn đặt là requestDate chứ không phải createdAt
                        r.getStatus()
                ))
                .collect(Collectors.toList());
    }

    // Tổng tiền phạt cần đóng
    public FineResponse getTotalFines(String username) {
        List<Violation> unpaid = violationRepository.findByLoan_User_UserName(username)
                .stream().filter(v -> !v.getIsPaid()).collect(Collectors.toList());

        Double total = unpaid.stream().mapToDouble(Violation::getFineAmount).sum();

        List<ViolationResponse> details = unpaid.stream()
                .map(v -> new ViolationResponse(v.getType(), v.getFineAmount(),
                        v.getIsPaid(), v.getLoan().getBookCopy().getBook().getTitle()))
                .collect(Collectors.toList());

        return new FineResponse(total, details);
    }

    // Lịch sử thanh toán
    public List<PaymentHistoryResponse> getPaymentHistory(String username) {
        return finePaymentRepository.findByStudent_User_UserNameOrderByPaymentDateDesc(username)
                .stream()
                .map(p -> new PaymentHistoryResponse(
                        p.getPaymentDate(),
                        p.getAmountPaid(),
                        p.getPaymentMethod()
                ))
                .collect(Collectors.toList());
    }

    // Nhận thông báo
    public List<Notification> getNotifications(String username) {
        return notificationRepository.findByUser_UserNameOrderByCreatedAtDesc(username);
    }
}