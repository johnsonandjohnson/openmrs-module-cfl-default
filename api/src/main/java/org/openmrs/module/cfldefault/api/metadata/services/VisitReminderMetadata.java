package org.openmrs.module.cfldefault.api.metadata.services;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.cfldefault.api.builder.MessageTemplateBuilder;
import org.openmrs.module.cfldefault.api.builder.MessageTemplateFieldBuilder;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisitReminderMetadata extends AbstractMessageServiceMetadata {
  private static final int VERSION = 5;

  public VisitReminderMetadata(DbSessionFactory dbSessionFactory) {
    super(dbSessionFactory, VERSION, "95573fe3-20b2-11ea-ac12-0242c0a82002");
  }

  @Override
  protected Template createTemplate() throws IOException {
    final String visitReminderServiceQuery = getVisitReminderServiceQuery();
    final String visitReminderCalendarServiceQuery = getVisitReminderCalendarServiceQuery();

    return MessageTemplateBuilder.buildMessageTemplate(
        visitReminderServiceQuery,
        SQL_QUERY_TYPE,
        visitReminderCalendarServiceQuery,
        "Visit reminder",
        true,
        templateUuid);
  }

  @Override
  protected void updateTemplate(Template template) throws IOException {
    final String visitReminderServiceQuery = getVisitReminderServiceQuery();
    final String visitReminderCalendarServiceQuery = getVisitReminderCalendarServiceQuery();

    template.setServiceQuery(visitReminderServiceQuery);
    template.setCalendarServiceQuery(visitReminderCalendarServiceQuery);
    template.setShouldUseOptimizedQuery(true);
  }

  @Override
  protected void createAndSaveTemplateFields(Template template) {
    List<TemplateField> templateFields = new ArrayList<>();

    if (isTemplateFieldNotExist("95574976-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Service type",
              true,
              "Deactivate service",
              template,
              TemplateFieldType.SERVICE_TYPE,
              "Deactivate service|SMS|WhatsApp|Call",
              "95574976-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("95575327-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Start of messages",
              false,
              "",
              template,
              TemplateFieldType.START_OF_MESSAGES,
              null,
              "95575327-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("95575cbd-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "End of messages",
              true,
              "NO_DATE|EMPTY",
              template,
              TemplateFieldType.END_OF_MESSAGES,
              null,
              "95575cbd-20b2-11ea-ac12-0242c0a82002"));
    }

    templateFields.forEach(getTemplateFieldService()::saveOrUpdate);
  }

  @Override
  protected void performAdditionalUpdate() throws IOException {
    metadataSQLScriptRunner.executeQuery(
        "DROP FUNCTION IF EXISTS GET_DAYS_BEFORE_REMINDER;");
    metadataSQLScriptRunner.executeQueryFromResource(
        SERVICES_BASE_PATH + "VisitReminder/GetDaysBeforeReminder.sql");
    metadataSQLScriptRunner.executeQuery(
        "DROP FUNCTION IF EXISTS GET_PREDICTION_START_DATE_FOR_VISIT;");
    metadataSQLScriptRunner.executeQueryFromResource(
        SERVICES_BASE_PATH + "VisitReminder/VisitReminderStartDateFunction.sql");
    metadataSQLScriptRunner.executeQuery("UPDATE messages_template_field\n"
        + "SET possible_values = 'Deactivate service|SMS|WhatsApp|Call'\n"
        + "WHERE name = 'Service type'\n"
        + "AND template_id = (select messages_template_id from messages_template where name = 'Visit reminder');");

    createRelatedGlobalProperty(
        "message.daysToCallBeforeVisit.default",
        "1,7",
        "Used to determine the how many days before visit reminder should be scheduled. "
            + "Note: if the property will store negative values then "
            + "the visit reminder will be sent after visit.");
  }

  private void createRelatedGlobalProperty(String key, String value, String description) {
    String existSetting = Context.getAdministrationService().getGlobalProperty(key);
    if (StringUtils.isBlank(existSetting)) {
      GlobalProperty gp = new GlobalProperty(key, value, description);
      Context.getAdministrationService().saveGlobalProperty(gp);
    }
  }

  private String getVisitReminderServiceQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "VisitReminder/VisitReminder.sql");
  }

  private String getVisitReminderCalendarServiceQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "VisitReminder/VisitReminderCalendarQuery.sql");
  }
}
