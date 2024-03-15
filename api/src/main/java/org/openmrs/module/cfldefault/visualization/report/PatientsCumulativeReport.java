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

public class PatientsCumulativeReport extends CflBaseReportManager {

  private static final String DATA_SET_NAME = "PatientsCumulative";
  private static final String DATA_SET_QUERY_PATH = "mysql/dataSetDefinition/PatientsCumulative.sql";

  protected PatientsCumulativeReport() {
    super("0f52521f-0cbd-42b8-a94e-6bca27b5feda",
        DATA_SET_NAME,
        DATA_SET_QUERY_PATH);
  }

  @Override
  public String getUuid() {
    return "214005f6-cb77-4ebd-a3ab-c6701d9affcf";
  }

  @Override
  public String getName() {
    return "PatientsCumulativeReport";
  }

  @Override
  public String getDescription() {
    return "Patients Cumulative Report";
  }

  @Override
  public String getVersion() {
    return "1.0";
  }
}
