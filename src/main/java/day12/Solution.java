package day12;

import utils.PathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<Action> actions = Files.readAllLines(PathUtil.getFilePath("day12/puzzle-input.txt")).stream()
      .map(Action::new)
      .collect(Collectors.toList());
    System.out.println(firstSolution(actions));
    System.out.println(secondSolution(actions));
  }

  private static long firstSolution(List<Action> actions) {
    Accumulator accumulator = new Accumulator();
    Map<DirectionIdentification, Direction> directionTable = getDirectionUpdateTable();

    for (Action action : actions) {
      if (action.action == 'N') {
        updateValue(accumulator, Direction.NORTH, action.value);
      }
      if (action.action == 'S') {
        updateValue(accumulator, Direction.SOUTH, action.value);
      }
      if (action.action == 'E') {
        updateValue(accumulator, Direction.EAST, action.value);
      }
      if (action.action == 'W') {
        updateValue(accumulator, Direction.WEST, action.value);
      }
      if (action.action == 'L') {
        accumulator.direction = directionTable.get(new DirectionIdentification('L', action.value, accumulator.direction));
      }
      if (action.action == 'R') {
        accumulator.direction = directionTable.get(new DirectionIdentification('R', action.value, accumulator.direction));
      }
      if (action.action == 'F') {
        updateValue(accumulator, accumulator.direction, action.value);
      }
    }

    return Math.abs(accumulator.horizontal) + Math.abs(accumulator.vertical);
  }

  private static long secondSolution(List<Action> actions) {
    Accumulator shipAccumulator = new Accumulator();
    Accumulator wayPointAccumulator = new Accumulator(10L, 1L);

    for (Action action : actions) {
      if (action.action == 'F') {
        shipAccumulator.horizontal += (wayPointAccumulator.horizontal * action.value);
        shipAccumulator.vertical += (wayPointAccumulator.vertical * action.value);
      }
      if (action.action == 'N') {
        updateValue(wayPointAccumulator, Direction.NORTH, action.value);
      }
      if (action.action == 'S') {
        updateValue(wayPointAccumulator, Direction.SOUTH, action.value);
      }
      if (action.action == 'E') {
        updateValue(wayPointAccumulator, Direction.EAST, action.value);
      }
      if (action.action == 'W') {
        updateValue(wayPointAccumulator, Direction.WEST, action.value);
      }
      if (action.action == 'L' || action.action == 'R') {
        Long tempHorizontal = wayPointAccumulator.horizontal;
        Long tempVertical = wayPointAccumulator.vertical;
        wayPointAccumulator.vertical = updateVertical(action.action, action.value, tempHorizontal, tempVertical);
        wayPointAccumulator.horizontal = updateHorizontal(action.action, action.value, tempHorizontal, tempVertical);
      }
    }

    return Math.abs(shipAccumulator.horizontal) + Math.abs(shipAccumulator.vertical);
  }

  private static void updateValue(Accumulator accumulator, Direction direction, Long value) {
    switch (direction) {
      case EAST:
        accumulator.horizontal += value;
        break;
      case WEST:
        accumulator.horizontal -= value;
        break;
      case NORTH:
        accumulator.vertical += value;
        break;
      case SOUTH:
        accumulator.vertical -= value;
        break;
      default:
        break;
    }
  }

  private static Long updateVertical(Character face, Long degrees, Long horizontal, Long vertical) {
    if (face == 'R') {
      if (degrees == 90) {
        vertical = -horizontal;
      } else if (degrees == 180) {
        vertical = -vertical;
      } else if (degrees == 270) {
        vertical = horizontal;
      }
    } else if (face == 'L') {
      if (degrees == 90) {
        vertical = horizontal;
      } else if (degrees == 180) {
        vertical = -vertical;
      } else if (degrees == 270) {
        vertical = -horizontal;
      }
    }
    return vertical;
  }

  private static Long updateHorizontal(Character face, Long degrees, Long horizontal, Long vertical) {
    if (face == 'R') {
      if (degrees == 90) {
        horizontal = vertical;
      } else if (degrees == 180) {
        horizontal = -horizontal;
      } else if (degrees == 270) {
        horizontal = -vertical;
      }
    } else if (face == 'L') {
      if (degrees == 90) {
        horizontal = -vertical;
      } else if (degrees == 180) {
        horizontal = -horizontal;
      } else if (degrees == 270) {
        horizontal = vertical;
      }
    }
    return horizontal;
  }

  private static Map<DirectionIdentification, Direction> getDirectionUpdateTable() {
    Map<DirectionIdentification, Direction> directionMap = new HashMap<>();
    directionMap.put(new DirectionIdentification('L', 90L, Direction.EAST), Direction.NORTH);
    directionMap.put(new DirectionIdentification('L', 90L, Direction.NORTH), Direction.WEST);
    directionMap.put(new DirectionIdentification('L', 90L, Direction.WEST), Direction.SOUTH);
    directionMap.put(new DirectionIdentification('L', 90L, Direction.SOUTH), Direction.EAST);

    directionMap.put(new DirectionIdentification('L', 180L, Direction.EAST), Direction.WEST);
    directionMap.put(new DirectionIdentification('L', 180L, Direction.NORTH), Direction.SOUTH);
    directionMap.put(new DirectionIdentification('L', 180L, Direction.WEST), Direction.EAST);
    directionMap.put(new DirectionIdentification('L', 180L, Direction.SOUTH), Direction.NORTH);

    directionMap.put(new DirectionIdentification('L', 270L, Direction.EAST), Direction.SOUTH);
    directionMap.put(new DirectionIdentification('L', 270L, Direction.NORTH), Direction.EAST);
    directionMap.put(new DirectionIdentification('L', 270L, Direction.WEST), Direction.NORTH);
    directionMap.put(new DirectionIdentification('L', 270L, Direction.SOUTH), Direction.WEST);

    directionMap.put(new DirectionIdentification('R', 90L, Direction.EAST), Direction.SOUTH);
    directionMap.put(new DirectionIdentification('R', 90L, Direction.SOUTH), Direction.WEST);
    directionMap.put(new DirectionIdentification('R', 90L, Direction.WEST), Direction.NORTH);
    directionMap.put(new DirectionIdentification('R', 90L, Direction.NORTH), Direction.EAST);

    directionMap.put(new DirectionIdentification('R', 180L, Direction.EAST), Direction.WEST);
    directionMap.put(new DirectionIdentification('R', 180L, Direction.SOUTH), Direction.NORTH);
    directionMap.put(new DirectionIdentification('R', 180L, Direction.WEST), Direction.EAST);
    directionMap.put(new DirectionIdentification('R', 180L, Direction.NORTH), Direction.SOUTH);

    directionMap.put(new DirectionIdentification('R', 270L, Direction.EAST), Direction.NORTH);
    directionMap.put(new DirectionIdentification('R', 270L, Direction.SOUTH), Direction.EAST);
    directionMap.put(new DirectionIdentification('R', 270L, Direction.WEST), Direction.SOUTH);
    directionMap.put(new DirectionIdentification('R', 270L, Direction.NORTH), Direction.WEST);

    return directionMap;
  }
}

enum Direction {
  SOUTH,
  WEST,
  NORTH,
  EAST
}

final class Action {
  final char action;
  final long value;

  Action(String line) {
    this.action = line.charAt(0);
    this.value = Long.valueOf(line.substring(1));
  }
}

final class Accumulator {
  Long horizontal;
  Long vertical;
  Direction direction;

  Accumulator() {
    horizontal = 0L;
    vertical = 0L;
    direction = Direction.EAST;
  }

  Accumulator(Long horizontal, Long vertical) {
    this.horizontal = horizontal;
    this.vertical = vertical;
    direction = Direction.EAST;
  }
}

final class DirectionIdentification {
  private Character face;
  private Long degrees;
  private Direction direction;

  DirectionIdentification(Character face, Long degrees, Direction direction) {
    this.face = face;
    this.degrees = degrees;
    this.direction = direction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectionIdentification that = (DirectionIdentification) o;
    return face.equals(that.face) &&
      degrees.equals(that.degrees) &&
      direction == that.direction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(face, degrees, direction);
  }
}

