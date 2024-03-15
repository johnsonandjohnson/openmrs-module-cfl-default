/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfldefault.visualization.report.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.module.reporting.report.util.ReportUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

public abstract class CflBaseReportManager extends BaseReportManager {

  private final String sqlDataDefinitionUuid;
  private final String dataSetName;
  private final String dataSetQueryPath;
  private static final Log LOGGER = LogFactory.getLog(CflBaseReportManager.class);

  protected CflBaseReportManager(final String sqlDataDefinitionUuid, final String dataSetName, final String dataSetQueryPath) {
    this.sqlDataDefinitionUuid = sqlDataDefinitionUuid;
    this.dataSetName = dataSetName;
    this.dataSetQueryPath = dataSetQueryPath;
  }

  @Override
  public List<Parameter> getParameters() {
    Parameter locationIdsParameter = new Parameter(
        "locationIds",
        "Location",
        Location.class,
        Set.class,
        null
    );

    return Collections.singletonList(locationIdsParameter);
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    final ReportDefinition reportDefinition = new ReportDefinition();
    reportDefinition.setUuid(getUuid());
    reportDefinition.setName(getName());
    reportDefinition.setDescription(getDescription());
    reportDefinition.setParameters(getParameters());
    reportDefinition.addDataSetDefinition(getName(),
        Mapped.mapStraightThrough(getOrCreateSqlDataSetDefinition(sqlDataDefinitionUuid)));
    return reportDefinition;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    final ReportDesign design = createExcelTemplateDesign();
    design.setReportDefinition(reportDefinition);
    design.addPropertyValue("repeatingSections", "sheet:2,row:2,dataset:" + dataSetName);

    final ReportDesignResource resource = createXLSXReportDesignResource();
    resource.setName(dataSetName.concat("ReportTemplate"));
    resource.setContents(ReportUtil.readByteArrayFromResource("reportTemplate/".concat(dataSetName).concat("ReportTemplate").concat("xlsx")));
    resource.setReportDesign(design);
    design.addResource(resource);

    return singletonList(design);
  }

  protected ReportDesignResource createXLSXReportDesignResource() {
    final ReportDesignResource reportDesignResource = new ReportDesignResource();
    reportDesignResource.setExtension("xlsx");
    reportDesignResource.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    return reportDesignResource;
  }

  protected ReportDesign createExcelTemplateDesign() {
    final ReportDesign reportDesign = new ReportDesign();
    reportDesign.setName("ExcelTemplate");
    reportDesign.setRendererType(ExcelTemplateRenderer.class);
    return reportDesign;
  }

  protected static String getQueryFromResource(String resourcesPath) throws IOException {
    String query = null;
    InputStream in = CflBaseReportManager.class.getClassLoader().getResourceAsStream(resourcesPath);
    if (in != null) {
      query = IOUtils.toString(in);
    }
    return query;
  }

  private SqlDataSetDefinition getOrCreateSqlDataSetDefinition(String sqlDataDefinitionUuid) {
    final DataSetDefinitionService dataSetDefinitionService = Context.getService(DataSetDefinitionService.class);
    SqlDataSetDefinition sqlDataSetDefinition =
        (SqlDataSetDefinition) dataSetDefinitionService.getDefinitionByUuid(sqlDataDefinitionUuid);


    if (sqlDataSetDefinition == null) {
      try {
        sqlDataSetDefinition = new SqlDataSetDefinition();
        sqlDataSetDefinition.setUuid(sqlDataDefinitionUuid);
        sqlDataSetDefinition.setName(dataSetName);
        sqlDataSetDefinition.setDescription("SQL DataSet created for Report: " + getName());
        sqlDataSetDefinition.addParameters(getParameters());
        sqlDataSetDefinition.setSqlQuery(getQueryFromResource(dataSetQueryPath));
        sqlDataSetDefinition = dataSetDefinitionService.saveDefinition(sqlDataSetDefinition);
      } catch (IOException e) {
        LOGGER.error("Error during trying to get dataset SQL query from: " + dataSetQueryPath, e);
      }
    }

    return sqlDataSetDefinition;
  }
}
