package magpiebridge.core;

import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.util.collections.Pair;
import org.eclipse.lsp4j.DiagnosticSeverity;

/** This class defines the analysis result from SARIF file */
public class FlowAnalysisResult implements AnalysisResult {

  private Kind kind;
  private Position position;
  private String message;
  private Iterable<Pair<Position, String>> related;
  private DiagnosticSeverity severity;
  private Pair<Position, String> repair;
  private String code;

  public FlowAnalysisResult(
      Kind kind,
      Position pos,
      String msg,
      Iterable<Pair<Position, String>> relatedInfo,
      DiagnosticSeverity severity,
      Pair<Position, String> repair,
      String code) {
    this.kind = kind;
    this.position = pos;
    this.message = msg;
    this.related = relatedInfo;
    this.severity = severity;
    this.repair = repair;
    this.code = code;
  }

  @Override
  public Kind kind() {
    return kind;
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Iterable<Pair<Position, String>> related() {
    return related;
  }

  @Override
  public DiagnosticSeverity severity() {
    return severity;
  }

  @Override
  public Pair<Position, String> repair() {
    return repair;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String toString() {
    return "Result [kind="
        + kind
        + ", position="
        + position
        + ", code="
        + code
        + ", message="
        + message
        + ", related="
        + related
        + ", severity="
        + severity
        + ", repair="
        + repair
        + "]";
  }

  @Override
  public String toString(boolean useMarkdown) {
    return message;
  }
}
