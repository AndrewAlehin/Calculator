package ru.andrewalehin.calculator.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.andrewalehin.calculator.model.Expression;
import ru.andrewalehin.calculator.model.MathOperation;

@UtilityClass
public class ExpressionUtil {

  public static List<Expression> filterByOperation(Collection<Expression> calculators,
      MathOperation operation) {
    return calculators.stream()
        .filter(c -> c.getExpression().contains(operation.getOperation()))
        .collect(Collectors.toList());
  }
}
