package day6;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.PathUtil.getFilePath;

public class Solution {
  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day6/puzzle-input.txt"));
    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }

  private static long firstSolution(List<String> lines) {
    long questionCount = 0;
    Set<Character> questions = new HashSet<>();

    for (String line : lines) {
      if (line.isEmpty()) {
        questionCount += questions.size();
        questions = new HashSet<>();
      } else {
        Set<Character> groupQuestions = line.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        questions.addAll(groupQuestions);
      }
    }
    return questionCount;
  }

  private static long secondSolution(List<String> lines) {
    long questionCount = 0;
    List<Set<Character>> questionsGroups = new ArrayList<>();

    for (String line : lines) {
      if (line.isEmpty()) {
        Set<Character> perGroupResult = questionsGroups.get(0);
        for (Set<Character> perGroup : questionsGroups) {
          perGroupResult = Sets.intersection(perGroupResult, perGroup);
        }
        questionCount += perGroupResult.size();
        questionsGroups = new ArrayList<>();
      } else {
        Set<Character> questions = line.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        questionsGroups.add(questions);
      }
    }
    return questionCount;
  }
}
