package ru.andrewalehin.calculator.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andrewalehin.calculator.model.Expression;

public interface ExpressionRepository extends JpaRepository<Expression, Integer> {

  @Query("SELECT c from Expression c WHERE c.date >= :startDate AND c.date < :endDate ORDER BY c.date DESC")
  List<Expression> getBetweenHalfOpen(LocalDate startDate, LocalDate endDate);

  @Query("SELECT c FROM Expression c WHERE c.username=:userName ORDER BY c.date DESC")
  List<Expression> getAll(String userName);
}
