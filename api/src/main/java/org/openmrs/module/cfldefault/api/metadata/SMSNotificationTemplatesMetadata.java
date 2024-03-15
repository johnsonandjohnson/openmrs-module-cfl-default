package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

public class SMSNotificationTemplatesMetadata extends VersionedMetadataBundle {

  private static final String ADHERENCE_DAILY_NOTIFICATION_TEMPLATE =
      "#set ($uuidClazz = $openmrsContext.loadClass('java.util.UUID'))\n"
          + "#set ($openmrs_id = $uuidClazz.randomUUID().toString())\n"
          + "#set ($patientName = $patient.getPersonName().toString().replaceAll(\"[^A-Za-z0-9 ']\",\" \"))\n"
          + "#set ($caregiverName = $actor.getPersonName().toString().replaceAll(\"[^A-Za-z0-9 ']\",\" \"))\n"
          + "#set($textToRead = \"\")\n"
          + "#if($patient.getId().equals($actor.getId()))\n"
          + " #set($textToRead = \"Hello $patientName, Your dosage is scheduled to be taken now. Please take your dosage.\")\n"
          + "#else\n"
          + " #set($textToRead = \"Hello $caregiverName, Your patient's dosage is scheduled to be taken now.\")\n"
          + "#end\n"
          + "{ message:\"$textToRead\", openmrs_id:\"$openmrs_id\" }";

  private static final String ADHERENCE_WEEKLY_NOTIFICATION_TEMPLATE =
      "#set ($uuidClazz = $openmrsContext.loadClass('java.util.UUID'))\n"
          + "#set ($openmrs_id = $uuidClazz.randomUUID().toString())\n"
          + "#set ($patientName = $patient.getPersonName().toString().replaceAll(\"[^A-Za-z0-9 ']\",\" \"))\n"
          + "#set ($caregiverName = $actor.getPersonName().toString().replaceAll(\"[^A-Za-z0-9 ']\",\" \"))\n"
          + "#set($textToRead = \"\")\n"
          + "#if($patient.getId().equals($actor.getId()))\n"
          + " #set($textToRead = \"Hello $patientName, Your dosage is scheduled to be taken. Please take your dosage as prescribed.\")\n"
          + "#else\n"
          + " #set($textToRead = \"Hello $caregiverName, Your patient's dosage is scheduled to be taken.\")\n"
          + "#end\n"
          + "{ message:\"$textToRead\", openmrs_id:\"$openmrs_id\" }";

  private static final String ADHERENCE_FEEDBACK_NOTIFICATION_TEMPLATE =
      "#set ($uuidClazz = $openmrsContext.loadClass('java.util.UUID'))\n"
          + "#set ($openmrs_id = $uuidClazz.randomUUID().toString())\n"
          + "#set($logFactory = $openmrsContext.loadClass(\"org.apache.commons.logging.LogFactory\"))\n"
          + "#set($log = $logFactory.getLog(\"org.openmrs.scheduler.tasks.TaskThreadedInitializationWrapper\"))\n"
          + "\n"
          + "#set($af = $adherenceFeedbackService.getAdherenceFeedback($patient.getId(), $actor.getId()))\n"
          + "#set($daf = $af.get(\"Adherence report daily\"))\n"
          + "#set($waf = $af.get(\"Adherence report weekly\"))\n"
          + "\n"
          + "#set($adhLevel = '')\n"
          + "#set($adhTrend = '')\n"
          + "#if($daf.getNumberOfDays() > 0)\n"
          + "  ##daily adherence\n"
          + "  #set($adhLevel = $daf.getAdherenceLevel())\n"
          + "  #set($adhTrend = $daf.getAdherenceTrend())\n"
          + "#else\n"
          + "  ##weekly adherence\n"
          + "  #set($adhLevel = $waf.getAdherenceLevel())\n"
          + "  #set($adhTrend = $waf.getAdherenceTrend())\n"
          + "#end\n"
          + "\n"
          + "#set($textToRead = \"\")\n"
          + "#if($patient.getId().equals($actor.getId()))\n"
          + "  #set($textToRead = \"Your adherence for past week is $adhLevel and compare to last week your adherence trend is $adhTrend\")\n"
          + "#else\n"
          + "  #set($textToRead = \"Your patient's adherence for past week is $adhLevel and compare to last week your patient's adherence trend is $adhTrend\")\n"
          + "#end\n"
          + "{ message:\"$textToRead\", openmrs_id:\"$openmrs_id\" }";

  private static final String HEALTH_TIP_NOTIFICATION_TEMPLATE =
      "#set ($uuidClazz = $openmrsContext.loadClass('java.util.UUID'))\n"
          + "#set ($openmrs_id = $uuidClazz.randomUUID().toString())\n"
          + "#set ($healthTip = $healthTipService.getNextHealthTipToPlay($patient.getId(), $actor.getId(), \"\"))\n"
          + "#set ($localeClass = $conceptDAO.getClass().forName(\"java.util.Locale\"))\n"
          + "#set ($healthTipText = $healthTip.getDescription().getDescription())\n"
          + "{ message:\"$healthTipText\", openmrs_id:\"$openmrs_id\"}\n"
          + "#set ($dummy = $messagesService.registerResponse($actor.getId(), $patient.getId(), $message_group_id, 'SCHEDULED_SERVICE_GROUP', $healthTip.getId(), '', 55191, '', $DateUtil.now()))";

  private static final String VISIT_REMINDER_NOTIFICATION_TEMPLATE =
      "#set ($uuidClazz = $openmrsContext.loadClass('java.util.UUID'))\n"
          + "#set ($openmrs_id = $uuidClazz.randomUUID().toString())\n"
          + "#set ($integerClazz = $openmrsContext.loadClass('java.lang.Integer'))\n"
          + "#set ($stringClazz = $openmrsContext.loadClass('java.lang.String'))\n"
          + "#set ($simpleDateFormat = $openmrsContext.loadClass('java.text.SimpleDateFormat').getDeclaredConstructor($stringClazz).newInstance('yyyy-MM-dd'))\n"
          + "#set ($visitTypeIdInteger = $integerClazz.parseInt($visitTypeId))\n"
          + "#set ($visitPurpose = $openmrsContext.getVisitService().getVisitType($visitTypeIdInteger).getName())\n"
          + "#set ($patientName = $patient.getPersonName().toString().replaceAll(\"[^A-Za-z0-9 ']\",\" \"))"
          + "#set($textToRead1 = \"\")\n"
          + "#if($patient.getId().equals($actor.getId()))\n"
          + "  #set($textToRead1 = \"Hello $patientName, You have a\")\n"
          + "#else\n"
          + "  #set($textToRead1 = \"Hello $patientName, Your patient has a\")\n"
          + "#end\n"
          + "#if($timeStarted)\n"
          + "  #set($visitTime = \" at $timeStarted\")\n"
          + "#else\n"
          + "  #set($visitTime = \"\")\n"
          + "#end\n"
          + "#set($textToRead2 = \"visit scheduled for $simpleDateFormat.format($simpleDateFormat.parse($dateStarted))$visitTime for the purpose of $visitPurpose.\")\n"
          + "{ message:\"$textToRead1 $textToRead2\", openmrs_id:\"$openmrs_id\" }\n";

  private static final String NOTIFICATION_TEMPLATE_INJECTED_SERVICES =
      "patientService:patientService,cflPersonService:cflPersonService,messagesService:messages.messagingService,personService:personService,personDAO:personDAO,conceptDAO:conceptDAO,locationService:locationService,messagingGroupService:messages.messagingGroupService,openmrsContext:context,patientTemplateService:messages.patientTemplateService,healthTipService:messages.healthTipService,adherenceFeedbackService:messages.adherenceFeedbackService";

  @Override
  public int getVersion() {
    return 5;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    createOrUpdateGP(
        "messages.notificationTemplate.adherence-report-daily",
        ADHERENCE_DAILY_NOTIFICATION_TEMPLATE,
        "The notification template for adherence report daily message type.");
    createOrUpdateGP(
        "messages.notificationTemplate.adherence-report-weekly",
        ADHERENCE_WEEKLY_NOTIFICATION_TEMPLATE,
        "The notification template for adherence report weekly message type.");
    createOrUpdateGP(
        "messages.notificationTemplate.adherence-feedback",
        ADHERENCE_FEEDBACK_NOTIFICATION_TEMPLATE,
        "The notification template for adherence feedback message type.");
    createOrUpdateGP(
        "messages.notificationTemplate.health-tip",
        HEALTH_TIP_NOTIFICATION_TEMPLATE,
        "The notification template for health tip message type.");
    createOrUpdateGP(
        "messages.notificationTemplate.visit-reminder",
        VISIT_REMINDER_NOTIFICATION_TEMPLATE,
        "The notification template for visit reminder message type.");
    createOrUpdateGP(
        "messages.notificationTemplate.injectedServices",
        NOTIFICATION_TEMPLATE_INJECTED_SERVICES,
        "Comma-separated values which represent the map of services which should be injected into the messaging notification template.");
    createOrUpdateGP(
        "messages.cutOffScoreForHighMediumAdherenceLevel",
        "90",
        "Used to specify a percentage cut-off score for High and Medium adherence level");
    createOrUpdateGP(
        "messages.cutOffScoreForMediumLowAdherenceLevel",
        "70",
        "Used to specify a percentage cut-off score for Medium and Low adherence level");
    createOrUpdateGP(
        "messages.cutOffScoreForAdherenceTrend",
        "5",
        "Used to specify a percentage cut-off score for adherence trend");
    createOrUpdateGP(
        "messages.benchmarkPeriod",
        "7",
        "Used to specify the number of days from which adherence trend is calculated");
    createOrUpdateGP(
        "messages.dailyAdherenceQuestion.uuid", "e387f386-15fd-4ddd-8460-effa47cd4a11", "");
    createOrUpdateGP(
        "messages.weeklyAdherenceQuestion.uuid", "e1b9b42d-5901-4f34-b1c7-af53e238cba2", "");

  }

  private void createOrUpdateGP(String property, String value, String description) {
    AdministrationService administrationService = Context.getAdministrationService();
    GlobalProperty gp = new GlobalProperty(property, value, description);
    administrationService.saveGlobalProperty(gp);
  }
}
