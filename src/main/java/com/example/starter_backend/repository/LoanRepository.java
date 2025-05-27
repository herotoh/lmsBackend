package com.example.starter_backend.repository;

import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberAndStatus(Member member, String status);
    List<Loan> findByStatus(String status);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
    List<Loan> findByMemberIdAndFineGreaterThan(Long memberId, Double fineThreshold);

}
