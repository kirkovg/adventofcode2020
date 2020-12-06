package day4;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.PathUtil.getFilePath;

public class Solution {
  private static final Set<String> MANDATORY_FIELDS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
  private static final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

  private static class Passport {
    private Set<String> fields = new HashSet<>();

    boolean validate(String key, String value) {
      switch (key) {
        case "byr": {
          boolean valid = false;
          int year = Integer.valueOf(value);
          if (year >= 1920 && year <= 2002) {
            valid = true;
          }
          return valid;
        }
        case "iyr": {
          boolean valid = false;
          int year = Integer.valueOf(value);
          if (year >= 2010 && year <= 2020) {
            valid = true;
          }
          return valid;
        }
        case "eyr": {
          boolean valid = false;
          int year = Integer.valueOf(value);
          if (year >= 2020 && year <= 2030) {
            valid = true;
          }
          return valid;
        }
        case "hgt": {
          if (value.endsWith("cm")) {
            String height = value.split("cm")[0];
            return Integer.valueOf(height) >= 150 && Integer.valueOf(height) <= 193;
          }
          if (value.endsWith("in")) {
            String height = value.split("in")[0];
            return Integer.valueOf(height) >= 59 && Integer.valueOf(height) <= 76;
          }
          return false;
        }
        case "hcl": {
          Pattern compile = Pattern.compile("^#([0-9]{1,6}|[a-f]{1,6}){6}$");
          Matcher matcher = compile.matcher(value);
          return matcher.find();
        }
        case "ecl": {
          return EYE_COLORS.contains(value);
        }
        case "pid": {
          try {
            Integer.valueOf(value);
            return value.length() == 9;
          } catch (Exception e) {
            return false;
          }
        }
        case "cid": {
          return true;
        }
        default: {
          throw new IllegalStateException("Unknown field: " + key);
        }
      }
    }

    void parse(String line, boolean useValidation) {
      String[] parts = line.split(" ");
      for (String part : parts) {
        String[] pair = part.split(":");
        boolean valid = validate(pair[0], pair[1]);
        if (useValidation) {
          if (valid) {
            fields.add(pair[0]);
          }
        } else {
          fields.add(pair[0]);
        }
      }
    }

    boolean isValid() {
      Set<String> nonMatchingFields = Sets.difference(MANDATORY_FIELDS, fields);
      if (nonMatchingFields.size() == 1 && nonMatchingFields.contains("cid")) {
        return true;
      }
      return nonMatchingFields.isEmpty();
    }
  }


  private static int executeSolution(List<String> lines, boolean useValueValidation) {
    int validPasswords = 0;
    Passport passport = new Passport();
    for (String line : lines) {
      if (line.isEmpty()) {
        if (passport.isValid()) {
          validPasswords++;
        }
        passport = new Passport();
        continue;
      }
      passport.parse(line, useValueValidation);
    }

    return validPasswords;
  }


  private static int firstSolution(List<String> lines) {
    return executeSolution(lines, false);
  }

  private static int secondSolution(List<String> lines) {
    return executeSolution(lines, true);
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(getFilePath("day4/puzzle-input.txt"));

    System.out.println(firstSolution(lines));
    System.out.println(secondSolution(lines));
  }
}
