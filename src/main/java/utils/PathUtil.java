package utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathUtil {
  private PathUtil() {
  }

  public static Path getFilePath(String path) throws URISyntaxException {
    URI uri = ClassLoader.getSystemResource(path).toURI();
    return Paths.get(uri);
  }
}
