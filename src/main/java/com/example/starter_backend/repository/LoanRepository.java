package com.example.starter_backend.repository;

import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByUserAndStatus(User user, Loan.LoanStatus status);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
     List<Loan> findByMemberIdAndFineGreaterThan(Long memberId, double fine);
     List<Loan> findByBookId(Long bookId);
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByStatus(Loan.LoanStatus status);
    List<Loan> findByUserAndStatusAndDueDateBefore(User user, Loan.LoanStatus status, LocalDate date);
    List<Loan> findByUserAndDueDateBefore(User user, LocalDate date);
    List<Loan> findByUserAndStatusAndDueDateAfter(User user, Loan.LoanStatus status, LocalDate date);
    List<Loan> findByUserAndStatusAndDueDateBetween(User user, Loan.LoanStatus status, LocalDate startDate, LocalDate endDate);
}