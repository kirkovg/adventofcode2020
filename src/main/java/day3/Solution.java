package day3;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static utils.PathUtil.getFilePath;




public class Solution {

  private static long treeCounter(List<String> lines, int positionIncrement, int lineIncrement) {
    int treeCount = 0;
    int position = positionIncrement;
    for (int i = lineIncrement; i < lines.size(); i += lineIncrement) {
      char c = lines.get(i).charAt(position);
      if (c == '#') {
        treeCount++;
      }
      position += positionIncrement;
      if (position >= lines.get(i).length()) {
        position = position - lines.get(i).length();
      }
    }
    return treeCount;
  }

  private static long firstSolution(List<String> lines) {
    return treeCounter(lines, 3, 1);
  }

  private static long secondSolution(List<String> lines) {
    long result = treeCounter(lines, 1, 1);
    result *= treeCounter(lines, 3, 1);
    result *= treeCounter(lines, 5, 1);
    result *= treeCounter(lines, 7, 1);
    result *= treeCounter(lines, 1, 2);
    return result;
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day3/puzzle-input.txt"));

    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }
}
