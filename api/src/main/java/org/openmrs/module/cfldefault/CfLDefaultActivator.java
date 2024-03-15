/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfldefault;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleException;
import org.openmrs.module.cfldefault.api.activator.ModuleActivatorStep;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class CfLDefaultActivator extends BaseModuleActivator {

  private static final Log LOGGER = LogFactory.getLog(CfLDefaultActivator.class);

  @Override
  public void started() {
    LOGGER.info("Started CfL Default module");
    try {
      final List<ModuleActivatorStep> sortedSteps =
          Context.getRegisteredComponents(ModuleActivatorStep.class).stream()
              .sorted(Comparator.comparing(ModuleActivatorStep::getOrder))
              .collect(Collectors.toList());

      for (ModuleActivatorStep step : sortedSteps) {
        step.startup(LOGGER);
      }
    } catch (Exception e) {
      throw new ModuleException("Failed to start CfL Default module.", e);
    }
  }

  @Override
  public void stopped() {
    LOGGER.info("Stopped CfL Default module");
  }
}
