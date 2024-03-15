package org.openmrs.module.cfldefault.api.metadata.services;

import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.cfldefault.api.builder.MessageTemplateBuilder;
import org.openmrs.module.cfldefault.api.builder.MessageTemplateFieldBuilder;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdherenceFeedbackMetadata extends AbstractMessageServiceMetadata {
  private static final int VERSION = 6;

  public AdherenceFeedbackMetadata(DbSessionFactory dbSessionFactory) {
    super(dbSessionFactory, VERSION, "9556a62d-20b2-11ea-ac12-0242c0a82002");
  }

  @Override
  protected Template createTemplate() throws IOException {
    final String adherenceFeedbackServiceQuery = getAdherenceFeedbackServiceQuery();
    final String adherenceFeedbackCalendarQuery = getAdherenceFeedbackCalendarQuery();

    return MessageTemplateBuilder.buildMessageTemplate(
        adherenceFeedbackServiceQuery,
        SQL_QUERY_TYPE,
        adherenceFeedbackCalendarQuery,
        "Adherence feedback",
        true,
        templateUuid);
  }

  @Override
  protected void updateTemplate(Template template) throws IOException {
    final String adherenceFeedbackServiceQuery = getAdherenceFeedbackServiceQuery();
    final String adherenceFeedbackCalendarQuery = getAdherenceFeedbackCalendarQuery();

    template.setServiceQuery(adherenceFeedbackServiceQuery);
    template.setCalendarServiceQuery(adherenceFeedbackCalendarQuery);
    template.setShouldUseOptimizedQuery(true);
  }

  @Override
  protected void createAndSaveTemplateFields(Template template) {
    List<TemplateField> templateFields = new ArrayList<>();

    if (isTemplateFieldNotExist("9556b39a-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Service type",
              true,
              "Deactivate service",
              template,
              TemplateFieldType.SERVICE_TYPE,
              "Deactivate service|SMS|WhatsApp|Call",
              "9556b39a-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("9556c330-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Frequency of the message",
              true,
              "Weekly",
              template,
              TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY,
              null,
              "9556c330-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("9556d458-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Week day of delivering message",
              true,
              "Monday",
              template,
              TemplateFieldType.DAY_OF_WEEK,
              null,
              "9556d458-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("9556e428-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Start of messages",
              false,
              "",
              template,
              TemplateFieldType.START_OF_MESSAGES,
              null,
              "9556e428-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("9556f070-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "End of messages",
              true,
              "NO_DATE|EMPTY",
              template,
              TemplateFieldType.END_OF_MESSAGES,
              null,
              "9556f070-20b2-11ea-ac12-0242c0a82002"));
    }

    templateFields.forEach(getTemplateFieldService()::saveOrUpdate);
  }

  @Override
  protected void performAdditionalUpdate() throws IOException {
    metadataSQLScriptRunner.executeQuery(
        "DROP FUNCTION IF EXISTS GET_PREDICTION_START_DATE_FOR_ADHERENCE_FEEDBACK;");
    metadataSQLScriptRunner.executeQueryFromResource(
        SERVICES_BASE_PATH + "AdherenceFeedback" + "/AdherenceFeedbackStartDateFunction.sql");
    metadataSQLScriptRunner.executeQuery("UPDATE messages_template_field\n"
        + "SET possible_values = 'Deactivate service|SMS|WhatsApp|Call'\n"
        + "WHERE name = 'Service type'\n"
        + "AND template_id = (select messages_template_id from messages_template where name = 'Adherence feedback');");
  }

  private String getAdherenceFeedbackServiceQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "AdherenceFeedback/AdherenceFeedback.sql");
  }

  private String getAdherenceFeedbackCalendarQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "AdherenceFeedback/AdherenceFeedbackCalendar.sql");
  }
}
