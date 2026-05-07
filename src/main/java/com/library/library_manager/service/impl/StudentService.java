package com.library.library_manager.service.impl;

import com.library.library_manager.dto.*;
import com.library.library_manager.dto.book.ReviewRequestDTO;
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


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    ILoanRepository loanRepository;
    IViolationRepository violationRepository;

    final IBorrowHistoryRepository borrowHistoryRepository;
    final IReservationRepository reservationRepository;
    final IFinePaymentRepository finePaymentRepository;
    final INotificationRepository notificationRepository;

    final IBookReviewRepository bookReviewRepository;
    final IBookCopyRepository bookCopyRepository;
    private final IBookRepository bookRepository;

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
                .stream().filter(v -> !v.getIsPaid()).toList();

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

    // --- LOGIC ĐÁNH GIÁ SÁCH ---
    @Transactional
    public void postReview(Long bookId, ReviewRequestDTO dto, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Student student = studentRepository.findByUser_UserName(username)
                .orElseThrow(() -> new RuntimeException("Student is not valid"));

        BookReview review = BookReview.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .book(book)
                .student(student)
                .createdAt(LocalDateTime.now())
                .build();

        bookReviewRepository.save(review);
    }

    // Logic tính 3 ngày làm việc (bỏ qua T7, CN)
    private LocalDateTime calculateExpirationDate(LocalDateTime start) {
        int addedDays = 0;
        LocalDateTime result = start;
        while (addedDays < 3) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        return result;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequestDTO dto, String username) {
        // 1. Kiểm tra hạn mức (Ví dụ: Tổng mượn + đặt không quá 5)
        long currentLoans = loanRepository.countByUser_UserNameAndReturnedAtIsNull(username);
        long currentRes = reservationRepository.countByStudent_User_UserNameAndStatus(username, "Đang giữ");

        if (currentLoans + currentRes >= 5) {
            throw new RuntimeException("Bạn đã đạt hạn mức mượn và đặt sách (tối đa 5 cuốn).");
        }

        // 2. Tìm bản sao sách (Copy)
        BookCopy copy = bookCopyRepository.findById(dto.getCopy_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản sao sách này."));

        Student student = studentRepository.findByUser_UserName(username)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại."));

        // 3. Tạo Reservation
        Reservation res = new Reservation();
        res.setBook(copy.getBook());
        res.setBookCopy(copy);
        res.setStudent(student);
        res.setRequestDate(LocalDateTime.now());
        res.setExpirationDate(calculateExpirationDate(LocalDateTime.now()));
        res.setStatus("Đang giữ");

        reservationRepository.save(res);

        return new ReservationResponse(copy.getBook().getTitle(), res.getRequestDate(), res.getStatus());
    }

    // API Hủy đặt trước
    @Transactional
    public void cancelReservation(Long id, String username) {
        Reservation res = reservationRepository.findById(id).orElseThrow();
        if (!res.getStudent().getUser().getUserName().equals(username)) {
            throw new RuntimeException("Bạn không có quyền hủy yêu cầu này.");
        }
        res.setStatus("Bị hủy");
        reservationRepository.save(res);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDTO dto, String username) {
        // 1. Tìm đánh giá cũ dựa trên ID
        BookReview review = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá với ID: " + reviewId));

        // 2. Kiểm tra quyền sở hữu: Sinh viên chỉ được sửa đánh giá của chính mình
        // So sánh username của người đang đăng nhập với username của người tạo review
        if (!review.getStudent().getUser().getUserName().equals(username)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa đánh giá này!");
        }

        // 3. Cập nhật dữ liệu mới từ DTO
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now()); // Cập nhật lại thời gian chỉnh sửa

        // 4. Lưu lại (Hibernate sẽ tự động update nhờ @Transactional)
        bookReviewRepository.save(review);
    }

    public ReservationResponse getReservationDetail(Long id, String username) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu đặt trước"));

        // Kiểm tra bảo mật (chỉ cho phép chủ nhân xem)
        if (!res.getStudent().getUser().getUserName().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xem chi tiết yêu cầu này");
        }

        return new ReservationResponse(res.getBook().getTitle(), res.getRequestDate(), res.getStatus());
    }
}