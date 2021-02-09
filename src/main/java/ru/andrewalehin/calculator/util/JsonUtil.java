package ru.andrewalehin.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

  private static ObjectMapper mapper;

  public static void setMapper(ObjectMapper mapper) {
    JsonUtil.mapper = mapper;
  }

  public static <T> List<T> readValues(String json, Class<T> clazz) {
    ObjectReader reader = mapper.readerFor(clazz);
    try {
      return reader.<T>readValues(json).readAll();
    } catch (IOException e) {
      throw new java.lang.IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'",
          e);
    }
  }
}