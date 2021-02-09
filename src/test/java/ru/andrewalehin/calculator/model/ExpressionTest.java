package ru.andrewalehin.calculator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.andrewalehin.calculator.util.IllegalArgumentException;

@SpringBootTest
@AutoConfigureMockMvc
class ExpressionTest {

  @Test
  public void expression() {
    assertEquals(BigDecimal.valueOf(3).setScale(2), new Expression("2 + 1").getResult());
    assertEquals(BigDecimal.valueOf(13).setScale(2), new Expression("2 / 2 + 3 * 4").getResult());
    assertEquals(BigDecimal.valueOf(1.6).setScale(2),
        new Expression("2 / ( 2 + 3 ) * 4").getResult());
    assertEquals(BigDecimal.valueOf(8).setScale(2), new Expression("2 ^ 3").getResult());
    assertEquals(BigDecimal.valueOf(0.8414709848078965).setScale(2, RoundingMode.HALF_DOWN),
        new Expression("sin(1)").getResult());
    assertEquals(BigDecimal.valueOf(0.8414709848078965).setScale(2, RoundingMode.HALF_DOWN),
        new Expression("sin(0.5 + 0.5)").getResult());
    assertEquals(BigDecimal.valueOf(1).setScale(2), new Expression("(1)").getResult());
  }

  @Test
  public void expressionOperationsException() {

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Expression("2 & 1").getResult();
    });
    String expectedMessage = "Illegal argument - operations";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));

    assertThrows(IllegalArgumentException.class, () -> {
      new Expression("2 + 1)").getResult();
    });
  }

  @Test
  public void expressionParenthesesException() {

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Expression("2 + 1)").getResult();
    });
    String expectedMessage = "Illegal argument - parentheses";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}