package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflcore.CFLConstants;
import org.openmrs.module.cflcore.api.util.GlobalPropertiesConstants;
import org.openmrs.module.cfldefault.api.CfLDefaultConstants;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/**
 * Updates Global Parameter values, it's responsible for updating Global Properties to CFL
 * distribution defaults.
 *
 * <p>The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class UpdateGlobalParametersMetadata extends VersionedMetadataBundle {

  @Override
  public int getVersion() {
    return 10;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    updateGlobalProperties();
  }

  private void updateGlobalProperties() {
    // Enable and keep enabled patient dashboard redirection
    updateGlobalPropertyIfExists(
        CFLConstants.PATIENT_DASHBOARD_REDIRECT_GLOBAL_PROPERTY_NAME, Boolean.TRUE.toString());
    updateGlobalPropertyIfExists(
        GlobalPropertiesConstants.VISIT_FORM_URIS.getKey(),
        "{\n"
            + //
            "  \"Medicine refill\": {\n"
            + //
            "    \"create\": \"/htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&visitId={{visitId}}&formUuid=9819ecd1-bf3e-4258-80c7-881feb1cb638\",\n"
            + //
            "    \"edit\": \"/htmlformentryui/htmlform/editHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&encounterId={{encounterId}}\"\n"
            + //
            "  },\n"
            + //
            "  \"Sputum collection\": {\n"
            + //
            "    \"create\": \"/htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&visitId={{visitId}}&formUuid=aa5f120a-04ec-11e3-8780-2b40bef9a44b\",\n"
            + //
            "    \"edit\": \"/htmlformentryui/htmlform/editHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&encounterId={{encounterId}}\"\n"
            + //
            "  },\n"
            + //
            "  \"default\": {\n"
            + //
            "    \"create\": \"/htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&visitId={{visitId}}&formUuid=k65f120a-04ec-11e3-8780-2b40bef9a44b\",\n"
            + //
            "    \"edit\": \"/htmlformentryui/htmlform/editHtmlFormWithStandardUi.page?patientId={{patientId}}"
            + //
            "&encounterId={{encounterId}}\"\n"
            + //
            "  }\n"
            + //
            "}");
    updateGlobalPropertyIfExists(
        " visits.visit-times",
        "7:00,7:30,8:00,8:30,9:00,9:30,10:00,10:30,11:00,11:30,12:00,12:30,13:00,13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30,18:00,18:30,19:00");
    updateGlobalPropertyIfExists(
        ConfigConstants.SMS_CONFIG, CfLDefaultConstants.SMS_CONFIG_DEFAULT_VALUE);
    updateGlobalPropertyIfExists(
        ConfigConstants.CALL_CONFIG, CfLDefaultConstants.CALL_CONFIG_DEFAULT_VALUE);
    updateGlobalPropertyIfExists(
        ConfigConstants.CALL_DEFAULT_FLOW, CfLDefaultConstants.DEFAULT_CALL_FLOW_NAME);
    updateGlobalPropertyIfExists(
        org.openmrs.module.visits.api.util.GlobalPropertiesConstants.ENCOUNTER_DATETIME_VALIDATION
            .getKey(),
        Boolean.FALSE.toString());
    updateGlobalPropertyIfExists(
            CfLDefaultConstants.REPORTS_FOR_DATA_VISUALIZATION_CONFIGURATION_UUID_LIST_KEY,
            CfLDefaultConstants.REPORTS_FOR_DATA_VISUALIZATION_CONFIGURATION_UUID_LIST_DEFAULT_VALUE);
    updateGlobalPropertyIfExists(
            CfLDefaultConstants.REPORTS_DATA_VISUALIZATION_CONFIGURATION_KEY,
            CfLDefaultConstants.REPORTS_DATA_VISUALIZATION_CONFIGURATION_DEFAULT_VALUE);
  }

  private void updateGlobalPropertyIfExists(String property, String value) {
    final AdministrationService administrationService = Context.getAdministrationService();
    GlobalProperty gp = administrationService.getGlobalPropertyObject(property);
    if (gp != null) {
      administrationService.setGlobalProperty(property, value);
    }
  }
}
