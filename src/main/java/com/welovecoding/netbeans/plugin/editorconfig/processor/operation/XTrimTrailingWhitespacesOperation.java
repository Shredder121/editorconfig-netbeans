package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Michael Koppen
 */
public class XTrimTrailingWhitespacesOperation {

  private static final Logger LOG = Logger.getLogger(XTrimTrailingWhitespacesOperation.class.getName());

  public static boolean doTrimTrailingWhitespaces(StringBuilder content, final String trimwhitespaces, final String lineEnding) throws Exception {

    return new XTrimTrailingWhitespacesOperation().apply(content, trimwhitespaces, lineEnding);
  }

  public boolean apply(StringBuilder content, final String trimwhitespaces, final String lineEnding) {
    boolean bTrimwhitespaces = Boolean.valueOf(trimwhitespaces);
    boolean changed = false;
    LOG.log(Level.INFO, "Executing ApplyTestTask");

    if (bTrimwhitespaces) {
      LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE = true");
      String tempContent = content.toString();
      LOG.log(Level.INFO, "OLDCONTENT: {0}.", tempContent);
      content = trimWhitespaces(content, lineEnding);

      if (tempContent.equals(content.toString())) {
        LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : No changes");
        changed = false;
      } else {
        LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : trimmed trailing whitespaces");
        changed = true;
      }
      LOG.log(Level.INFO, "NEWCONTENT: {0}.", content);
    }

    return changed;
  }

  private StringBuilder trimWhitespaces(StringBuilder content, String lineEnding) {
    BufferedReader reader = new BufferedReader(new StringReader(content.toString()));

    /**
     * Note: As a side effect this will strip a final newline!
     */
    String tempContent = reader.lines().
            map((String t) -> {
              return t.replaceAll("\\s+$", "");
            }).
            collect(Collectors.joining(lineEnding));

    /**
     * appending lineending only if that was the case in the old content.
     */
    if (content.toString().endsWith("\n") || content.toString().endsWith("\r")) {
      content.delete(0, content.length());
      content.append(tempContent).append(lineEnding);
    } else {
      content.delete(0, content.length());
      content.append(tempContent);
    }
    return content;
  }
}