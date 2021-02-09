package ru.andrewalehin.calculator;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.andrewalehin.calculator.model.Expression;

public class TestData {

  public static final Expression calculator1 = new Expression("1 + 3");
  public static final Expression calculator2 = new Expression("1 + 2");
  public static final Expression calculator3 = new Expression("5 - 6");
  public static final User user = new User("user", "user", new ArrayList<>());
  public static final User admin = new User("admin", "admin", new ArrayList<>());
  public static TestMatcher<Expression> EXPRESSION_MATCHER = TestMatcher
      .usingIgnoringFieldsComparator(Expression.class, "id", "username");

  public static RequestPostProcessor userHttpBasic(User user) {
    return SecurityMockMvcRequestPostProcessors.httpBasic(user.getUsername(), user.getPassword());
  }
}
