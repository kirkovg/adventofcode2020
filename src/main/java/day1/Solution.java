package day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static utils.PathUtil.getFilePath;

public class Solution {

  private static Integer firstSolution(List<Integer> reportEntries) {
    for (int i = 0; i < reportEntries.size(); i++) {
      for (int j = 0; j < reportEntries.size(); j++) {
        if (reportEntries.get(i) + reportEntries.get(j) == 2020) {
          return reportEntries.get(i) * reportEntries.get(j);
        }
      }
    }
    return null;
  }

  private static Integer secondSolution(List<Integer> reportEntries) {
    for (int i = 0; i < reportEntries.size(); i++) {
      for (int j = 0; j < reportEntries.size(); j++) {
        for (int k = 0; k < reportEntries.size(); k++) {
          if (reportEntries.get(i) + reportEntries.get(j) + reportEntries.get(k) == 2020) {
            return reportEntries.get(i) * reportEntries.get(j) * reportEntries.get(k);
          }
        }
      }
    }
    return null;
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    List<String> strings = Files.readAllLines(getFilePath("day1/puzzle-input.txt"));
    List<Integer> reportEntries = strings.stream().map(Integer::valueOf).collect(Collectors.toList());
    System.out.println(firstSolution(reportEntries));
    System.out.println(secondSolution(reportEntries));
  }
}
