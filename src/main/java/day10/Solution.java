package day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static utils.PathUtil.getFilePath;

public class Solution {

  public static void main(String[] args) throws URISyntaxException, IOException {
    LinkedList<Long> numbers = Files.readAllLines(getFilePath("day10/puzzle-input.txt")).stream()
      .map(Long::valueOf)
      .sorted()
      .collect(Collectors.toCollection(LinkedList::new));
    numbers.push(0L);
    System.out.println(firstSolution(numbers));
    System.out.println(secondSolution(numbers, new HashMap<>()));
  }

  private static long secondSolution(LinkedList<Long> numbers, HashMap<String, Long> resultContainer) {
    String key = numbers.stream().map(Object::toString).collect(Collectors.joining(","));
    if (resultContainer.containsKey(key)) {
      return resultContainer.get(key);
    }

    long result = 1;
    for (int i = 1; i < numbers.size() - 1; i++) {
      if (numbers.get(i + 1) - numbers.get(i - 1) <= 3) {
        LinkedList<Long> newList = new LinkedList<>();
        newList.add(numbers.get(i - 1));
        newList.addAll(getSublist(numbers, i + 1));
        result += secondSolution(newList, resultContainer);
      }
    }

    resultContainer.put(key, result);
    return result;
  }

  private static Long firstSolution(LinkedList<Long> numbers) {
    int oneJoltDifferences = 0;
    int threeJoltsDifference = 0;

    for (Long num : numbers) {
      if (numbers.contains(num + 1)) {
        oneJoltDifferences++;
      } else if (numbers.contains(num + 3)) {
        threeJoltsDifference++;
      }
    }

    return (oneJoltDifferences * (threeJoltsDifference + 1L));
  }

  private static LinkedList<Long> getSublist(LinkedList<Long> numbers, int start) {
    LinkedList<Long> result = new LinkedList<>();
    for (int i = start; i < numbers.size(); i++) {
      result.add(numbers.get(i));
    }
    return result;
  }
}
