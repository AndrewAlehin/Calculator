package ru.andrewalehin.calculator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.andrewalehin.calculator.util.IllegalArgumentException;

@Entity
@Table(name = "expression", uniqueConstraints = {@UniqueConstraint(columnNames = {"expression",
    "date"}, name = "expression_unique_expression_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Expression {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Integer id;

  @Column(name = "expression", nullable = false)
  @NotBlank
  private String expression;

  @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
  @NotNull
  private LocalDate date = LocalDate.now();

  @Column(name = "result", nullable = false)
  @NotNull
  private BigDecimal result;

  @Column(name = "user_name", nullable = false)
  @NotBlank
  private String username;

  public Expression(String expression) {
    this.expression = expression;
    this.result = evaluate(expression);
  }

  private BigDecimal evaluate(String expression) {
    expression = replaceParentheses(expression);

    List<String> list = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\d+\\.\\d+|\\d+|--\\d+\\.\\d+|--\\d|-\\d+\\.\\d+|-\\d|\\S");
    Matcher matcher = pattern.matcher(expression);
    while (matcher.find()) {
      String s = matcher.group();
      if (s.contains("--")) {
        s = s.replace("--", "-");
        list.add("-");
      }
      list.add(s);
    }
    return getResultMathOperation(list).setScale(2, RoundingMode.HALF_DOWN);
  }

  private String replaceParentheses(String expression) {
    while (true) {
      int i = expression.indexOf(")");
      if (i == -1) {
        break;
      }
      int j = expression.lastIndexOf("(", i);
      if (j == -1) {
        throw new IllegalArgumentException("Illegal argument - parentheses");
      }
      if (j - 3 >= 0 && expression.substring(j - 3, j).equalsIgnoreCase(MathOperation.SIN
          .getOperation())) {
        expression = expression.substring(0, j - 3)
            + Math.sin(evaluate(expression.substring(j + 1, i).trim()).doubleValue())
            + expression.substring(i + 1);
      } else {
        expression =
            expression.substring(0, j) + evaluate(expression.substring(j + 1, i).trim())
                + expression.substring(i + 1);
      }
    }
    return expression;
  }

  private BigDecimal getResultMathOperation(List<String> list) {
    Stack<BigDecimal> s = new Stack<>();

    for (int i = 0; i < list.size(); i += 2) {
      try {
        if (i == 0 || list.get(i - 1).equals(MathOperation.ADD.getOperation())) {
          s.push(new BigDecimal(list.get(i)));
        } else if (list.get(i - 1).equals(MathOperation.SUBTRACT.getOperation())) {
          s.push(new BigDecimal(list.get(i)).negate());
        } else if (list.get(i - 1).equals(MathOperation.POW.getOperation())) {
          s.push(s.pop().pow(Integer.parseInt(list.get(i))));
        } else if (list.get(i - 1).equals(MathOperation.MULTIPLY.getOperation())) {
          s.push(s.pop().multiply(new BigDecimal(list.get(i))));
        } else if (list.get(i - 1).equals(MathOperation.DIVIDE.getOperation())) {
          s.push(s.pop().divide(new BigDecimal(list.get(i))));
        } else {
          throw new IllegalArgumentException("Illegal argument - operations");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Illegal argument - Number Format");
      }
    }
    BigDecimal result = BigDecimal.ZERO;
    while (!s.isEmpty()) {
      result = result.add(s.pop());
    }
    return result;
  }
}
