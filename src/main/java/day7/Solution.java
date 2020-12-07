package day7;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static utils.PathUtil.getFilePath;

public class Solution {
  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day7/puzzle-input.txt"));
    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }

  // TODO: refactor to use regex instead of complex parsing
  private static long firstSolution(List<String> lines) {
    Map<String, Set<String>> parentToChildColors = getParentToChildColors(lines);
    return parentToChildColors.keySet().stream()
      .filter(key -> containsShinyGold(parentToChildColors, key))
      .count();
  }

  private static long secondSolution(List<String> lines) {
    Map<String, String> bags = lines.stream().collect(toMap(Solution::getParentBag, identity()));
    return getChildBagCount(bags, "shiny gold");
  }

  private static long getChildBagCount(Map<String, String> bags, String bag) {
    String content = bags.get(bag);
    Matcher matcher = Pattern.compile("(\\d)\\s(\\w+\\s\\w+)").matcher(content);
    int count = 0;
    while (matcher.find()) {
      int amount =  parseInt(matcher.group(1));
      String bagName = matcher.group(2);
      count += amount + (amount * getChildBagCount(bags, bagName));
    }
    return count;
  }

  private static String getParentBag(String str) {
    Matcher matcher = Pattern.compile("^(\\w+\\s\\w+)").matcher(str);
    matcher.find();
    return matcher.group(0);
  }

  private static boolean containsShinyGold(Map<String, Set<String>> parentToChildColors, String key) {
    Set<String> entries = parentToChildColors.get(key);
    if (entries != null && entries.contains("shiny gold")) {
      return true;
    }
    return entries != null && entries
      .stream()
      .anyMatch(entryKey -> containsShinyGold(parentToChildColors, entryKey));
  }

  private static Map<String, Set<String>> getParentToChildColors(List<String> lines) {
    Map<String, Set<String>> parentToChildColors = new HashMap<>();
    for (String line : lines) {
      String[] parentAndChild = line.split("contain");
      String parent = parentAndChild[0].split("bags")[0].trim();
      Set<String> mappedChildren = new HashSet<>();
      String[] children = parentAndChild[1].split(",");
      if (children.length == 1 && children[0].trim().startsWith("no other")) {
        // no other bags
        parentToChildColors.put(parent, mappedChildren);
        continue;
      }
      if (children.length == 1 && !children[0].trim().startsWith("no other")) {
        String[] parts = children[0].trim().split(" ");
        // 1 and 2 are the color identifiers
        mappedChildren.add(parts[1] + " " + parts[2]);
        parentToChildColors.put(parent, mappedChildren);
        continue;
      }
      if (children.length > 1) {
        Set<String> collectedChildren = Arrays.stream(children).map(c -> {
          String[] parts = c.trim().split(" ");
          return parts[1] + " " + parts[2];
        }).collect(Collectors.toSet());
        mappedChildren.addAll(collectedChildren);
        parentToChildColors.put(parent, mappedChildren);
      }
    }
    return parentToChildColors;
  }
}
