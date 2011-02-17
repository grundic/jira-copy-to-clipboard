package ru.mail.plugins.clipcopier;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.util.collect.MapBuilder;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;

/**
 * @author grundic
 */
public class CopyToClipboardCFType extends CalculatedCFType {

  public String getStringFromSingularObject(Object o) {
    return null;  //TODO: autoimplemented stub
  }

  public Object getSingularObjectFromString(String s) throws FieldValidationException {
    return null;  //TODO: autoimplemented stub
  }

  public Object getValueFromIssue(CustomField customField, Issue issue) {

    int type_id = Integer.parseInt(issue.getIssueTypeObject().getId());
    String typeName = issue.getIssueTypeObject().getName();
    String prefix;

    if ( IssueFieldConstants.BUG_TYPE_ID == type_id ){
      prefix = "bugfix";
    }
    else if ( IssueFieldConstants.NEWFEATURE_TYPE_ID == type_id ||
        typeName.compareToIgnoreCase("question") == 0 ||
        typeName.compareToIgnoreCase("suggestion") == 0 ||
        typeName.compareToIgnoreCase("task") == 0 ||
        typeName.compareToIgnoreCase("Subtask") == 0 ||
        typeName.compareToIgnoreCase("goal") == 0
        ) {
      prefix = "feature";
    }
    else {
      prefix = "internal";
    }

    return String.format("%s %s: %s", prefix, issue.getKey(), StringEscapeUtils.escapeJavaScript(issue.getSummary()));
  }

  public Map<String, Object> getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {

    MapBuilder params = MapBuilder.<String, Object> newBuilder();
    params.addAll(super.getVelocityParameters(issue, field, fieldLayoutItem));
    params.add("issue_id", issue.getId());


    return params.toMutableMap();
  }
}