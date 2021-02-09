package ru.andrewalehin.calculator.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.andrewalehin.calculator.TestData.EXPRESSION_MATCHER;
import static ru.andrewalehin.calculator.TestData.admin;
import static ru.andrewalehin.calculator.TestData.calculator1;
import static ru.andrewalehin.calculator.TestData.calculator2;
import static ru.andrewalehin.calculator.TestData.calculator3;
import static ru.andrewalehin.calculator.TestData.user;
import static ru.andrewalehin.calculator.TestData.userHttpBasic;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/dataTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ExpressionRestControllerTest {

  private static final String REST_URL = ExpressionRestController.REST_URL + "/";

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getResult() throws Exception {
    this.mockMvc.perform(get(REST_URL + "200 - 13")
        .with(userHttpBasic(user)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("187")));
  }

  @Test
  void getUnauth() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "200 - 1"))
        .andExpect(status().isUnauthorized());
  }


  @Test
  public void getExpressionNoValidParentheses() throws Exception {
    this.mockMvc.perform(get(REST_URL + "-20)")
        .with(userHttpBasic(user)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content()
            .string(containsString("{\"Invalid value\":\"Illegal argument - parentheses\"}")));
  }

  @Test
  public void getExpressionNoValidOperations() throws Exception {
    this.mockMvc.perform(get(REST_URL + "-20 = 2")
        .with(userHttpBasic(user)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content()
            .string(containsString("{\"Invalid value\":\"Illegal argument - operations\"}")));
  }

  @Test
  public void findByOperation() throws Exception {
    this.mockMvc.perform(get(REST_URL + "/findByOperation/ADD")
        .with(userHttpBasic(admin)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(EXPRESSION_MATCHER.contentJson(calculator1, calculator2));
  }

  @Test
  public void findByOperationUnauth() throws Exception {
    this.mockMvc.perform(get(REST_URL + "/findByOperation/ADD")
        .with(userHttpBasic(user)))
        .andExpect(status().isForbidden());
  }

  @Test
  void findByOperationNotFound() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/findByOperation/opp")
        .with(userHttpBasic(admin)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void findByUser() throws Exception {
    this.mockMvc.perform(get(REST_URL + "/findByUser/admin")
        .with(userHttpBasic(admin)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(EXPRESSION_MATCHER.contentJson(calculator1, calculator3));
  }

  @Test
  public void findByUserUnauth() throws Exception {
    this.mockMvc.perform(get(REST_URL + "/findByUser/admin")
        .with(userHttpBasic(user)))
        .andExpect(status().isForbidden());
  }

  @Test
  void getBetween() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
        .param("start", LocalDate.now().minusDays(1).toString())
        .param("end", LocalDate.now().plusDays(1).toString())
        .with(userHttpBasic(admin)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(EXPRESSION_MATCHER.contentJson(calculator1, calculator2, calculator3));
  }

  @Test
  public void getBetweenUnauth() throws Exception {
    this.mockMvc.perform(get(REST_URL + "filter")
        .with(userHttpBasic(user)))
        .andExpect(status().isForbidden());
  }
}