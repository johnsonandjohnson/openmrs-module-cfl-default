package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.module.patientflags.Flag;
import org.openmrs.module.patientflags.Priority;
import org.openmrs.module.patientflags.Tag;
import org.openmrs.module.patientflags.evaluator.SQLFlagEvaluator;

import java.util.HashSet;

import static java.util.Collections.singleton;

/** Adds Patient flags. */
@SuppressWarnings("PMD.ExcessiveMethodLength")
public class PatientFlagsMetadata extends VersionedMetadataBundle {
  private Tag savedCflTag;
  private Priority savedCflWarningPriority;

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
    savedCflTag = install(newCFLTag());
    savedCflWarningPriority = install(newCFLWarningPriority());
    installCFLFlags();
  }

  private Tag newCFLTag() {
    final Tag cflTag = new Tag();
    cflTag.setName("CFL tag");
    cflTag.setUuid("ef1a1b2d-c7a5-44d7-a518-ef78087abb23");
    cflTag.setRoles(new HashSet<>(Context.getUserService().getAllRoles()));
    return cflTag;
  }

  private Priority newCFLWarningPriority() {
    final Priority cflPriority = new Priority();
    cflPriority.setName("CFL warning priority");
    cflPriority.setStyle(
            "border: 1px solid #fb8500; color: #fb8500; padding: 1px 2px; border-radius: 4px;");
    cflPriority.setRank(2);
    cflPriority.setUuid("00841430-5f32-4baa-9f65-ba8a829d423f");
    return cflPriority;
  }

  private void installCFLFlags() {
    install(
        newSQLFlag(
            "CFL flag - missed last 3 visits",
            "Missed last 3 visits",
            "SELECT\n"
                + "\tt.patient_id\n"
                + "FROM\n"
                + "\t(\n"
                + "\t\tSELECT\n"
                + "\t\t\tv.patient_id,\n"
                + "\t\t\tva.visit_id,\n"
                + "\t\t\tva.value_reference,\n"
                + "\t\t\tv.date_started\n"
                + "\t\tFROM\n"
                + "\t\t\tvisit_attribute va\n"
                + "\t\t\tINNER JOIN openmrs.visit v ON v.visit_id = va.visit_id\n"
                + "\t\tWHERE\n"
                + "\t\t\tva.voided = 0\n"
                + "\t\t\tAND (\n"
                + "\t\t\t\tSELECT\n"
                + "\t\t\t\t\tCOUNT(*)\n"
                + "\t\t\t\tFROM\n"
                + "\t\t\t\t\tvisit_attribute va2\n"
                + "\t\t\t\t\tINNER JOIN openmrs.visit v2 ON v2.visit_id = va2.visit_id\n"
                + "\t\t\t\tWHERE\n"
                + "\t\t\t\t\tva2.attribute_type_id = (\n"
                + "\t\t\t\t\t\tSELECT\n"
                + "\t\t\t\t\t\t\tvat.visit_attribute_type_id\n"
                + "\t\t\t\t\t\tFROM\n"
                + "\t\t\t\t\t\t\tvisit_attribute_type vat\n"
                + "\t\t\t\t\t\tWHERE\n"
                + "\t\t\t\t\t\t\tuuid = '70ca70ac-53fd-49e4-9abe-663d4785fe62'\n"
                + "          )\n"
                + "\t\t\t\t\tAND va2.voided = 0\n"
                + "\t\t\t\t\tAND v2.patient_id = v.patient_id\n"
                + "\t\t\t\t\tAND v2.date_started >= v.date_started\n"
                + "\t\t\t\t\tAND v2.date_started < (SELECT NOW())\n"
                + "     ) <= 3\n"
                + "\t\tORDER BY\n"
                + "\t\t\tv.patient_id,\n"
                + "\t\t\tv.date_started DESC\n"
                + "  ) AS t\n"
                + "\tINNER JOIN (\n"
                + "\t\tSELECT\n"
                + "\t\t\tDISTINCT pa.person_id\n"
                + "\t\tFROM\n"
                + "\t\t\tperson_attribute pa\n"
                + "\t\tWHERE\n"
                + "\t\t\tpa.voided = 0\n"
                + "\t\t\tAND pa.person_attribute_type_id = (\n"
                + "        (\n"
                + "\t\t\t\t\tSELECT\n"
                + "\t\t\t\t\t\tpat.person_attribute_type_id\n"
                + "\t\t\t\t\tFROM\n"
                + "\t\t\t\t\t\tperson_attribute_type pat\n"
                + "\t\t\t\t\tWHERE\n"
                + "\t\t\t\t\t\tuuid = 'dda246c6-c806-402a-9b7c-e2c1574a6441'\n"
                + "        )\n"
                + "      )\n"
                + "\t\t\tAND pa.value = \"ACTIVATED\"\n"
                + "\t) AS p ON t.patient_id = p.person_id\n"
                + "WHERE\n"
                + "\tt.value_reference = \"MISSED\"\n"
                + "GROUP BY\n"
                + "\tt.patient_id\n"
                + "HAVING\n"
                + "\tCOUNT(*) >= 3;",
                savedCflWarningPriority));
    install(
        newSQLFlag(
            "Consent Pending",
            "Consent Pending",
            "SELECT\n"
                + "\ta.patient_id\n"
                + "FROM\n"
                + "\tpatient a\n"
                + "WHERE\n"
                + "\ta.patient_id in (\n"
                + "\tSELECT\n"
                + "\t\tperson_id\n"
                + "\tFROM\n"
                + "\t\tperson_attribute\n"
                + "\tWHERE\n"
                + "\t\tperson_attribute_type_id IN (\n"
                + "\t\tSELECT\n"
                + "\t\t\tpat.person_attribute_type_id\n"
                + "\t\tFROM\n"
                + "\t\t\tperson_attribute_type pat\n"
                + "\t\tWHERE\n"
                + "\t\t\tpat.name = 'Person status')\n"
                + "\t\tAND voided = 0\n"
                + "\t\tAND value = 'NO_CONSENT');",
            savedCflWarningPriority));
    install(
        newSQLFlag(
            "Incorrect pincode",
            "Incorrect pincode entered last 3 times",
            "SELECT DISTINCT p.patient_id\n"
                + "FROM patient AS p\n"
                + "INNER JOIN (SELECT \n"
                + "    cr.actorId,\n"
                + "    MAX(cr.refKey) as refKey,\n"
                + "    MAX(cr.playedMessages) as playedMessages,\n"
                + "    MAX(cr.date_created) as date_created\n"
                + "FROM (\n"
                + "    SELECT \n"
                + "        c1.actorId, \n"
                + "        c1.direction, \n"
                + "        c1.status, \n"
                + "        c1.refKey, \n"
                + "        c1.playedMessages, \n"
                + "        c1.date_created\n"
                + "    FROM cfl_calls c1\n"
                + "    JOIN (\n"
                + "        SELECT actorId, MAX(date_created) as max_date_created\n"
                + "        FROM cfl_calls\n"
                + "        GROUP BY actorId\n"
                + "    ) c2 ON c1.actorId = c2.actorId AND c1.date_created = c2.max_date_created\n"
                + ") cr\n"
                + "WHERE cr.playedMessages LIKE '%PinFlow_blocked-pin%'\n"
                + "GROUP BY cr.actorId\n"
                + "ORDER BY cr.actorId DESC) AS subquery \n"
                + "WHERE subquery.actorId = p.patient_id",
            savedCflWarningPriority));
  }

  private Flag newSQLFlag(String name, String message, String sql, Priority priority) {
    final Flag sqlFlag = new Flag();
    sqlFlag.setEnabled(true);
    sqlFlag.setEvaluator(SQLFlagEvaluator.class.getName());
    sqlFlag.setPriority(priority);
    sqlFlag.setTags(singleton(savedCflTag));
    sqlFlag.setName(name);
    sqlFlag.setCriteria(sql);
    sqlFlag.setMessage(message);
    return sqlFlag;
  }
}
