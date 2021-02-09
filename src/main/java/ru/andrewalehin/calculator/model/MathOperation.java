package ru.andrewalehin.calculator.model;

public enum MathOperation {
  MULTIPLY("*"),
  ADD("+"),
  DIVIDE("/"),
  SUBTRACT("-"),
  POW("^"),
  SIN("sin");
  private final String operation;

  MathOperation(String operation) {
    this.operation = operation;
  }

  public String getOperation() {
    return operation;
  }
}
