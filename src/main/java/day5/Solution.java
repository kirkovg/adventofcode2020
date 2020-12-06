package day5;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static utils.PathUtil.getFilePath;

public class Solution {

  private static double calculate(List<String> lines, boolean findYours) {
    double highestSeatId = -1;
    SortedSet<Double> seats;
    seats = new TreeSet<>();
    for (String line : lines) {
      int fullSeatIdentificationLength = line.length();
      double lowerRange = 0;
      double higherRange = 127;
      double resultRow = -1;

      for (int i = 0; i < fullSeatIdentificationLength - 3; i++) {
        if (line.charAt(i) == 'F') {
          if (i == fullSeatIdentificationLength - 3 - 1) {
            resultRow = lowerRange;
          } else {
            higherRange = Math.floor((higherRange + lowerRange) / 2);
          }
        }
        if (line.charAt(i) == 'B') {
          if (i == fullSeatIdentificationLength - 3 - 1) {
            resultRow = higherRange;
          } else {
            lowerRange = Math.ceil((higherRange + lowerRange) / 2);
          }
        }
      }


      lowerRange = 0;
      higherRange = 7;
      double resultColumn = -1;

      for (int i = fullSeatIdentificationLength - 3; i < line.length(); i++) {
        if (line.charAt(i) == 'L') {
          if (i == fullSeatIdentificationLength - 1) {
            resultColumn = lowerRange;
          } else {
            higherRange = Math.floor((higherRange + lowerRange) / 2);
          }
        }
        if (line.charAt(i) == 'R') {
          if (i == fullSeatIdentificationLength - 1) {
            resultColumn = higherRange;
          } else {
            lowerRange = Math.ceil((higherRange + lowerRange) / 2);
          }
        }
      }

      double id = resultRow * 8 + resultColumn;
      seats.add(id);
      if (id > highestSeatId) {
        highestSeatId = id;
      }
    }

    if (!findYours) {
      return highestSeatId;
    }

    double max = highestSeatId;
    while (seats.contains(max)) {
      max--;
    }
    return max;
  }

  private static double firstSolution(List<String> lines) {
    return calculate(lines, false);
  }

  private static double secondSolution(List<String> lines) {
    return calculate(lines, true);
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day5/puzzle-input.txt"));
    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }
}
