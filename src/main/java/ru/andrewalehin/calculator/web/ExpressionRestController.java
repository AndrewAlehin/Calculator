package ru.andrewalehin.calculator.web;


import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.andrewalehin.calculator.model.Expression;
import ru.andrewalehin.calculator.model.MathOperation;
import ru.andrewalehin.calculator.repository.ExpressionRepository;
import ru.andrewalehin.calculator.util.ExpressionUtil;
import ru.andrewalehin.calculator.util.IllegalArgumentException;

@RestController
@RequestMapping(value = ExpressionRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ExpressionRestController {

  static final String REST_URL = "/rest/calculator";

  private final ExpressionRepository repository;

  @GetMapping("/{expression}")
  public BigDecimal getResultExpression(Principal principal, @PathVariable String expression) {
    Expression calculator = new Expression(expression);
    calculator.setUsername(principal.getName());
    repository.save(calculator);
    return calculator.getResult();
  }

  @GetMapping("/findByUser/{username}")
  public List<Expression> findByUserName(@PathVariable String username) {
    return repository.getAll(username);
  }

  @GetMapping("/findByOperation/{operation}")
  public List<Expression> findByOperation(@PathVariable MathOperation operation) {
    return ExpressionUtil.filterByOperation(repository.findAll(), operation);
  }

  @GetMapping("/filter")
  public List<Expression> getBetween(
      @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
    return repository.getBetweenHalfOpen(start, end);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IllegalArgumentException.class})
  public JSONObject handleValidationExceptions(IllegalArgumentException ex) {
    JSONObject errors = new JSONObject();
    errors.put("Invalid value", ex.getMessage());
    return errors;
  }
}