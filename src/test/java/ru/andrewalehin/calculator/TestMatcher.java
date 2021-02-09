package ru.andrewalehin.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.BiConsumer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.andrewalehin.calculator.util.JsonUtil;

public class TestMatcher<T> {

  private final Class<T> clazz;
  private final BiConsumer<T, T> assertion;
  private final BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion;

  private TestMatcher(Class<T> clazz, BiConsumer<T, T> assertion,
      BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
    this.clazz = clazz;
    this.assertion = assertion;
    this.iterableAssertion = iterableAssertion;
  }

  public static <T> TestMatcher<T> usingIgnoringFieldsComparator(Class<T> clazz,
      String... fieldsToIgnore) {
    return new TestMatcher<>(clazz,
        (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore)
            .isEqualTo(e),
        (a, e) -> assertThat(a).usingElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(e));
  }

  public static <T> List<T> readListFromJsonMvcResult(
      MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
    return JsonUtil.readValues(getContent(result), clazz);
  }

  public static String getContent(MvcResult result) throws UnsupportedEncodingException {
    return result.getResponse().getContentAsString();
  }

  public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
    iterableAssertion.accept(actual, expected);
  }

  @SafeVarargs
  public final ResultMatcher contentJson(T... expected) {
    return contentJson(List.of(expected));
  }

  public ResultMatcher contentJson(Iterable<T> expected) {
    return result -> assertMatch(readListFromJsonMvcResult(result, clazz), expected);
  }
}
