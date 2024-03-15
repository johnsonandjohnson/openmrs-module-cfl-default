/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.module.cfldefault.visualization.report.util.CflBaseReportManager;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;

public class CommonReportBundle extends VersionedMetadataBundle {

  private final CflBaseReportManager reportManager;

  CommonReportBundle(CflBaseReportManager reportManager) {
    this.reportManager = reportManager;
  }

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() throws Exception {
    ReportManagerUtil.setupReport(reportManager);
  }

  @Override
  protected void installNewVersion() throws Exception {
    //nothing to do
  }
}