package com.example.starter_backend.service;

import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FineService {

    private static final double DAILY_FINE = 0.5;
    private static final double MAX_FINE = 20.0;

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Scheduled task that runs daily at midnight to calculate overdue fines.
     * It updates the fine for all overdue loans.
     */
    @Scheduled(cron = "0 0 0 * * ?")  // Every day at midnight
    public void calculateOverdueFines() {
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loanRepository.findByDueDateBeforeAndReturnDateIsNull(today);

        for (Loan loan : overdueLoans) {
            long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate().plusDays(1), today);
            if (overdueDays > 0) {
                double calculatedFine = overdueDays * DAILY_FINE;
                if (calculatedFine > MAX_FINE) {
                    calculatedFine = MAX_FINE;
                }
                loan.setFine(calculatedFine);
                loanRepository.save(loan);
            }
        }
    }

    /**
     * Calculate total fines for a member (optional helper method).
     */
    public double getTotalFinesForMember(Long memberId) {
        List<Loan> loans = loanRepository.findByMemberIdAndFineGreaterThan(memberId, 0.0);
        return loans.stream().mapToDouble(Loan::getFine).sum();
    }
}
