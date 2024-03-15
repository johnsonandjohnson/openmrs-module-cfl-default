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

public class HealthTipMetadata extends AbstractMessageServiceMetadata {
  private static final int VERSION = 6;

  public HealthTipMetadata(DbSessionFactory dbSessionFactory) {
    super(dbSessionFactory, VERSION, "9556f9ab-20b2-11ea-ac12-0242c0a82002");
  }

  @Override
  protected Template createTemplate() throws IOException {
    final String healthTipServiceQuery = getHealthTipServiceQuery();
    final String healthTipCalendarQuery = getHealthTipCalendarQuery();

    return MessageTemplateBuilder.buildMessageTemplate(
        healthTipServiceQuery,
        SQL_QUERY_TYPE,
        healthTipCalendarQuery,
        "Health tip",
        true,
        templateUuid);
  }

  @Override
  protected void updateTemplate(Template template) throws IOException {
    final String healthTipServiceQuery = getHealthTipServiceQuery();
    final String healthTipCalendarQuery = getHealthTipCalendarQuery();

    template.setServiceQuery(healthTipServiceQuery);
    template.setCalendarServiceQuery(healthTipCalendarQuery);
    template.setShouldUseOptimizedQuery(true);
  }

  @Override
  protected void createAndSaveTemplateFields(Template template) {
    List<TemplateField> templateFields = new ArrayList<>();

    if (isTemplateFieldNotExist("95570356-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Service type",
              true,
              "Deactivate service",
              template,
              TemplateFieldType.SERVICE_TYPE,
              "Deactivate service|SMS|WhatsApp|Call",
              "95570356-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("95570d01-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Frequency of the message",
              true,
              "Daily",
              template,
              TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY,
              null,
              "95570d01-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("955716a6-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Week day of delivering message",
              true,
              "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday",
              template,
              TemplateFieldType.DAY_OF_WEEK,
              null,
              "955716a6-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("9557232f-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Start of messages",
              false,
              "",
              template,
              TemplateFieldType.START_OF_MESSAGES,
              null,
              "9557232f-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("95572cd9-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "Categories of the message",
              true,
              "HT_PREVENTION",
              template,
              TemplateFieldType.CATEGORY_OF_MESSAGE,
              null,
              "95572cd9-20b2-11ea-ac12-0242c0a82002"));
    }

    if (isTemplateFieldNotExist("95573791-20b2-11ea-ac12-0242c0a82002")) {
      templateFields.add(
          MessageTemplateFieldBuilder.buildMessageTemplateField(
              "End of messages",
              true,
              "NO_DATE|EMPTY",
              template,
              TemplateFieldType.END_OF_MESSAGES,
              null,
              "95573791-20b2-11ea-ac12-0242c0a82002"));
    }

    templateFields.forEach(getTemplateFieldService()::saveOrUpdate);
  }

  @Override
  protected void performAdditionalUpdate() throws IOException {
    metadataSQLScriptRunner.executeQuery(
        "DROP FUNCTION IF EXISTS GET_PREDICTION_START_DATE_FOR_HEALTH_TIP;");
    metadataSQLScriptRunner.executeQueryFromResource(
        SERVICES_BASE_PATH + "HealthTip/HealthTipStartDateFunction.sql");
    metadataSQLScriptRunner.executeQuery("UPDATE messages_template_field\n"
        + "SET possible_values = 'Deactivate service|SMS|WhatsApp|Call'\n"
        + "WHERE name = 'Service type'\n"
        + "AND template_id = (select messages_template_id from messages_template where name = 'Health tip');");
  }

  private String getHealthTipServiceQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "HealthTip/HealthTip.sql");
  }

  private String getHealthTipCalendarQuery() throws IOException {
    return metadataSQLScriptRunner.getQueryFromResource(
        SERVICES_BASE_PATH + "HealthTip/HealthTipCalendar.sql");
  }
}
