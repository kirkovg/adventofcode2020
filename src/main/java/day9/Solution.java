package day9;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.PathUtil.getFilePath;

public class Solution {
  public static void main(String[] args) throws URISyntaxException, IOException {
    List<Long> numbers = Files.readAllLines(getFilePath("day9/puzzle-input.txt")).stream()
      .map(Long::valueOf)
      .collect(Collectors.toList());
    long x = firstSolution(numbers);
    System.out.println(x);
    System.out.println(secondSolution(numbers, x));
  }

  private static long firstSolution(List<Long> numbers) {
    int start = 0;
    int end = 25;

    while (end != numbers.size()) {
      List<Long> preamble = getPreamble(numbers, start, end);
      Set<Long> sums = calculateSums(preamble);
      if (!sums.contains(numbers.get(end))) {
        return numbers.get(end);
      }
      start++;
      end++;
    }

    return -1;
  }

  private static long secondSolution(List<Long> numbers, long invalidNum) {
    int start = 0;
    int end = 0;
    Long sum = numbers.get(0);

    while (true) {
      if (sum < invalidNum) {
        end++;
        sum += numbers.get(end);
      } else if (sum > invalidNum) {
        sum -= numbers.get(start);
        start++;
      } else {
        List<Long> subList = numbers.subList(start, end + 1);
        return Collections.min(subList) + Collections.max(subList);
      }
    }
  }

  private static Set<Long> calculateSums(List<Long> preamble) {
    Set<Long> result = new HashSet<>();
    for (int i = 0; i < preamble.size(); i++) {
      for (int j = 0; j < preamble.size(); j++) {
        if (!preamble.get(i).equals(preamble.get(j))) {
          result.add(preamble.get(i) + preamble.get(j));
        }
      }
    }
    return result;
  }

  private static List<Long> getPreamble(List<Long> numbers, int start, int end) {
    List<Long> result = new ArrayList<>();
    for (int i = start; i < end; i++) {
      result.add(numbers.get(i));
    }
    return result;
  }
}
