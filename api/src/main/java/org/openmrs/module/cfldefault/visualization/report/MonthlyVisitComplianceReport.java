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

public class MonthlyVisitComplianceReport extends CflBaseReportManager {

  private static final String DATA_SET_NAME = "MonthlyVisitCompliance";
  private static final String DATA_SET_QUERY_PATH = "mysql/dataSetDefinition/MonthlyVisitCompliance.sql";

  protected MonthlyVisitComplianceReport() {
    super("1f8cba7c-38ed-4078-9ee4-550da80a894b",
        DATA_SET_NAME,
        DATA_SET_QUERY_PATH);
  }

  @Override
  public String getUuid() {
    return "66583f23-244e-46d9-892e-43e96eaefd80";
  }

  @Override
  public String getName() {
    return "MonthlyVisitComplianceReport";
  }

  @Override
  public String getDescription() {
    return "Monthly Visit Compliance Report";
  }

  @Override
  public String getVersion() {
    return "1.0";
  }
}


