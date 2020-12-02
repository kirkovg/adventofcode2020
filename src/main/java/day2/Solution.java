package day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static utils.PathUtil.getFilePath;

public class Solution {

  private static final class PasswordPolicy {
    int firstBoundary;
    int secondBoundary;
    char letterToCheck;
    String password;

    PasswordPolicy(String line) {
      String[] parts = line.split(" ");
      String[] boundaries = parts[0].split("-");
      this.firstBoundary = Integer.valueOf(boundaries[0]);
      this.secondBoundary = Integer.valueOf(boundaries[1]);
      this.letterToCheck = parts[1].charAt(0);
      this.password = parts[2];
    }
  }

  private static long getLetterCount(String password, char letterToCheck) {
    return password.chars().filter(c -> c == letterToCheck).count();
  }

  private static int firstSolution(List<String> lines) {
    int validPasswordsCount = 0;
    for (String line: lines) {
      PasswordPolicy policy = new PasswordPolicy(line);
      long letterCount = getLetterCount(policy.password, policy.letterToCheck);
      if (letterCount >= policy.firstBoundary && letterCount <= policy.secondBoundary) {
        validPasswordsCount++;
      }
    }

    return validPasswordsCount;
  }

  private static int secondSolution(List<String> lines) {
    int validPasswordsCount = 0;
    for (String line: lines) {
      PasswordPolicy policy = new PasswordPolicy(line);
      boolean isValid =
        (policy.password.charAt(policy.firstBoundary - 1) == policy.letterToCheck
          && policy.password.charAt(policy.secondBoundary - 1) != policy.letterToCheck)
        || (policy.password.charAt(policy.firstBoundary - 1) != policy.letterToCheck
          && policy.password.charAt(policy.secondBoundary - 1) == policy.letterToCheck);
      if (isValid) {
        validPasswordsCount++;
      }
    }

    return validPasswordsCount;
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day2/puzzle-input.txt"));

    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }
}
