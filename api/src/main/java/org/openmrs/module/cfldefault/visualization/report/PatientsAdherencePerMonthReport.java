/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfldefault.visualization.report;

import org.openmrs.module.cfldefault.visualization.report.util.CflBaseReportManager;

public class PatientsAdherencePerMonthReport extends CflBaseReportManager {

  private static final String DATA_SET_NAME = "PatientsAdherencePerMonth";
  private static final String DATA_SET_QUERY_PATH = "mysql/dataSetDefinition/PatientsAdherencePerMonth.sql";

  protected PatientsAdherencePerMonthReport() {
    super("39b25068-ee7d-4c6a-8497-7909d6492f7c",
        DATA_SET_NAME,
        DATA_SET_QUERY_PATH);
  }

  @Override
  public String getUuid() {
    return "f01cffd6-338a-473b-ac5c-833e59d14288";
  }

  @Override
  public String getName() {
    return "PatientsAdherencePerMonthReport";
  }

  @Override
  public String getDescription() {
    return "Patients Adherence Per Month Report";
  }

  @Override
  public String getVersion() {
    return "1.0";
  }
}
