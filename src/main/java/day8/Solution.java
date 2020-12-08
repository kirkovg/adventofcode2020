package day8;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.PathUtil.getFilePath;


enum Operation {
  NO_OP("nop"),
  ACCUMULATE("acc"),
  JUMP("jmp");

  final String operationName;

  Operation(String operationName) {
    this.operationName = operationName;
  }

  static Operation fromKey(String operationName) {
    return Arrays.stream(Operation.values()).filter(op -> op.operationName.equals(operationName))
      .findFirst().orElseThrow(() -> new IllegalStateException("Null passed for operation name"));
  }
}

final class Instruction {
  private Operation operation;
  private final Integer value;

  Instruction(String line) {
    String[] values = line.split(" ");
    this.operation = Operation.fromKey(values[0]);
    this.value = Integer.valueOf(values[1]);
  }

  Integer getValue() {
    return value;
  }

  Operation getOperation() {
    return operation;
  }

  void setOperation(Operation operation) {
    this.operation = operation;
  }
}

final class ProgramResult {
  private final long accumulator;
  private final boolean infiniteLoop;

  ProgramResult(long accumulator, boolean infiniteLoop) {
    this.accumulator = accumulator;
    this.infiniteLoop = infiniteLoop;
  }

  long getAccumulator() {
    return accumulator;
  }

  boolean isInfiniteLoop() {
    return infiniteLoop;
  }
}

public class Solution {

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<Instruction> instructions = Files.readAllLines(getFilePath("day8/puzzle-input.txt")).stream()
      .map(Instruction::new)
      .collect(Collectors.toList());

    System.out.println(firstSolution(instructions).getAccumulator());
    System.out.println(secondSolution(instructions));
  }

  private static ProgramResult firstSolution(List<Instruction> instructions) {

    List<Integer> pastPositions = new ArrayList<>();
    long accumulator = 0;
    int i = 0;
    boolean infiniteLoop = false;
    while (i < instructions.size()) {
      if (pastPositions.contains(i)) {
        infiniteLoop = true;
        break;
      }

      pastPositions.add(i);

      if (instructions.get(i).getOperation() == Operation.NO_OP) {
        i++;
      } else if (instructions.get(i).getOperation() == Operation.ACCUMULATE) {
        accumulator += instructions.get(i).getValue();
        i++;
      } else if (instructions.get(i).getOperation() == Operation.JUMP) {
        i += instructions.get(i).getValue();
      }
    }
    return new ProgramResult(accumulator, infiniteLoop);
  }

  private static long secondSolution(List<Instruction> instructions) {
    for (int i = 0; i < instructions.size(); i++) {
      Instruction instruction = instructions.get(i);

      if (instruction.getOperation() == Operation.ACCUMULATE) {
        continue;
      }

      Operation backup = instruction.getOperation();
      instruction.setOperation(instruction.getOperation() == Operation.JUMP ? Operation.NO_OP : Operation.JUMP);

      ProgramResult programResult = firstSolution(instructions);
      if (!programResult.isInfiniteLoop()) {
        return programResult.getAccumulator();
      } else {
        instruction.setOperation(backup);
      }
    }
    return 0;
  }
}
