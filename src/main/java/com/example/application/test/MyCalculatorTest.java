package com.example.application.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.application.view.MyCalculator;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MyCalculatorTest {

  private MyCalculator myCalculator;

  @BeforeEach
  public void setUp() {
    myCalculator = new MyCalculator();
  }

  @ParameterizedTest
  @MethodSource("provideIntsForAdd")
  public void add(int a, int b, int exp) {
    int res = myCalculator.add(a,b);
    assertEquals(exp, res);
  }

  @ParameterizedTest
  @MethodSource("provideIntsForTimes")
  public void times(int a, int b, int exp) {
    int res = myCalculator.times(a, b);
    assertEquals(exp, res);
  }

  @ParameterizedTest
  @MethodSource("provideIntsForDivide")
  public void divide(int a, int b, int exp) {
    int res = myCalculator.divide(a,b);
    assertEquals(exp, res);
  }

  private static Stream<Arguments> provideIntsForTimes() {
    return Stream.of(
        Arguments.of(2, 2, 4),
        Arguments.of(5, 2, 10),
        Arguments.of(6, 6, 36),
        Arguments.of(1000, 1000, 1000000)
    );
  }

  private static Stream<Arguments> provideIntsForAdd() {
    return Stream.of(
        Arguments.of(2, 2, 4),
        Arguments.of(5, 2, 7),
        Arguments.of(6, 6, 12),
        Arguments.of(1000, 1000, 2000)
    );
  }

  private static Stream<Arguments> provideIntsForDivide() {
    return Stream.of(
        Arguments.of(2, 2, 1),
        Arguments.of(10, 2, 5),
        Arguments.of(36, 6, 6),
        Arguments.of(10000, 1000, 10)
    );
  }
}
