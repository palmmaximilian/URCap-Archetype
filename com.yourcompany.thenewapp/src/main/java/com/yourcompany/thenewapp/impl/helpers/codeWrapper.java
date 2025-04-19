package com.yourcompany.thenewapp.impl.helpers;

import com.ur.urcap.api.domain.script.ScriptWriter;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class codeWrapper {

  public static void wrapFileLines(
    ScriptCommand writer,
    String resourcePath,
    Map<String, String> variables
  ) throws IOException {
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader reader = null;

    try {
      // Normalize resource path
      resourcePath = "code/" + resourcePath;

      // Get the resource stream
      is = codeWrapper.class.getClassLoader().getResourceAsStream(resourcePath);
      if (is == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }

      isr = new InputStreamReader(is, Charset.forName("UTF-8"));
      reader = new BufferedReader(isr);

      String line;
      while ((line = reader.readLine()) != null) {
        // Replace variables
        if (line.trim().startsWith("$")) {
          continue;
        }
        for (Map.Entry<String, String> entry : variables.entrySet()) {
          line =
            line.replace(
              "${" + entry.getKey() + "}",
              entry.getValue() != null ? entry.getValue() : ""
            );
        }

        // Wrap the line
        // writer.appendLine(escapeJavaString(line));
        writer.appendLine(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // Close resources
      closeQuietly(reader);
      closeQuietly(isr);
      closeQuietly(is);
    }
    // return writer;
  }

  public static void wrapFileLines(
    ScriptCommand writer,
    String resourcePath,
    Map<String, String> variables
  ) throws IOException {
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader reader = null;

    try {
      // Normalize resource path

      resourcePath = "code/" + resourcePath;

      // Get the resource stream
      is = codeWrapper.class.getClassLoader().getResourceAsStream(resourcePath);
      if (is == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }

      isr = new InputStreamReader(is, Charset.forName("UTF-8"));
      reader = new BufferedReader(isr);

      String line;
      while ((line = reader.readLine()) != null) {
        // Replace variables
        if (line.trim().startsWith("$")) {
          continue;
        }
        for (Map.Entry<String, String> entry : variables.entrySet()) {
          line =
            line.replace(
              "${" + entry.getKey() + "}",
              entry.getValue() != null ? entry.getValue() : ""
            );
        }

        // Wrap the line
        // writer.appendLine(escapeJavaString(line));
        writer.appendLine(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // Close resources
      closeQuietly(reader);
      closeQuietly(isr);
      closeQuietly(is);
    }
    // return writer;
  }

  private static String escapeJavaString(String str) {
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      switch (c) {
        // case '\"':
        //   sb.append("\\\"");
        //   break;
        case '\\':
          sb.append("\\\\");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\t':
          sb.append("\\t");
          break;
        // case '(':
        //   sb.append("\\(");
        //   break;
        // case ')':
        //   sb.append("\\)");
        //   break;
        case '{':
          sb.append("\\{");
          break;
        case '}':
          sb.append("\\}");
          break;
        default:
          sb.append(c);
      }
    }
    return sb.toString();
  }

  private static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        // Ignore
      }
    }
  }
}
