package org.openmrs.module.cfldefault.api;

public class CfLDefaultConstants {

  public static final String MODULE_API_PACKAGE = "org.openmrs.module.cfldefault.api";

  public static final String CFL_TELEPHONE_NUMBER_PERSON_ATTRIBUTE_TYPE_UUID =
      "14d4f066-15f5-102d-96e4-000c29c2a5d7";

  public static final String CFL_EMAIL_ADDRESS_PERSON_ATTRIBUTE_TYPE_UUID =
      "58cb9e76-75a1-4f49-956f-4ff6d0e02312";

  public static final String SMS_CONFIG_DEFAULT_VALUE = "nexmo-whatsapp";

  public static final String CALL_CONFIG_DEFAULT_VALUE = "nexmo";

  public static final String DEFAULT_CALL_FLOW_NAME = "MainFlow";

  public static final String REPORTS_DATA_VISUALIZATION_CONFIGURATION_KEY =
          "cflui.reportsDataVisualizationConfiguration";
  public static final String REPORTS_DATA_VISUALIZATION_CONFIGURATION_DEFAULT_VALUE =
          "["
                + "{\"uuid\":\"66583f23-244e-46d9-892e-43e96eaefd80\","
                  + "\"name\":\"MonthlyVisitComplianceReport\","
                  + "\"title\":\"Monthly Visit Compliance\","
                  + "\"chartType\":\"Bar Chart\","
                  + "\"xAxis\":\"monthName\","
                  + "\"yAxis\":\"visitCount\","
                  + "\"legend\":\"visitStatus\","
                  + "\"description\":\"Number of visits from current location grouped by month and status\","
                  + "\"marginTop\":50,"
                  + "\"marginBottom\":100,"
                  + "\"marginRight\":30,"
                  + "\"marginLeft\":60,"
                  + "\"colors\":\"#ea5545, #f46a9b, #ef9b20, #edbf33, #ede15b, #bdcf32, #87bc45, #27aeef, #b33dc6\","
                  + "\"roles\":\"\","
                  + "\"showTableUnderGraph\":false,"
                  + "\"yAxisNumbersType\":\"Integer numbers type\","
                  + "\"configFilters\":[]},"

                + "{\"uuid\":\"f01cffd6-338a-473b-ac5c-833e59d14288\","
                  + "\"name\":\"PatientsAdherencePerMonthReport\","
                  + "\"title\":\"Patients Adherence Per Month (Values in %)\","
                  + "\"chartType\":\"Bar Chart\","
                  + "\"xAxis\":\"date\","
                  + "\"yAxis\":\"numberOfPatients\","
                  + "\"legend\":\"callStatus\","
                  + "\"description\":\"Patients adherence of calls during last month\","
                  + "\"marginTop\":50,"
                  + "\"marginBottom\":100,"
                  + "\"marginRight\":30,"
                  + "\"marginLeft\":60,"
                  + "\"colors\":\"#ea5545, #f46a9b, #ef9b20, #edbf33, #ede15b, #bdcf32, #87bc45, #27aeef, #b33dc6\","
                  + "\"roles\":\"\","
                  + "\"showTableUnderGraph\":false,"
                  + "\"yAxisNumbersType\":\"Percentage numbers type\","
                  + "\"configFilters\":[]},"

                + "{\"uuid\":\"214005f6-cb77-4ebd-a3ab-c6701d9affcf\","
                  + "\"name\":\"PatientsCumulativeReport\","
                  + "\"title\":\"Patients Cumulative\","
                  + "\"chartType\":\"Line Chart\","
                  + "\"xAxis\":\"date\","
                  + "\"yAxis\":\"total\","
                  + "\"legend\":\"locationName\","
                  + "\"description\":\"Patients Cumulative\","
                  + "\"marginTop\":50,"
                  + "\"marginBottom\":100,"
                  + "\"marginRight\":30,"
                  + "\"marginLeft\":60,"
                  + "\"colors\":\"#ea5545, #f46a9b, #ef9b20, #edbf33, #ede15b, #bdcf32, #87bc45, #27aeef, #b33dc6\","
                  + "\"roles\":\"\","
                  + "\"showTableUnderGraph\":false,"
                  + "\"yAxisNumbersType\":\"Integer numbers type\","
                  + "\"configFilters\":[]}"
          + "]";

  private CfLDefaultConstants() {
  }
}
