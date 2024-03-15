package org.openmrs.module.cfldefault.api.activator.impl;

import org.apache.commons.logging.Log;
import org.openmrs.module.cfldefault.api.activator.ModuleActivatorStep;
import org.openmrs.module.emrapi.utils.MetadataUtil;

import static org.openmrs.module.cfldefault.api.activator.impl.ModuleActivatorStepOrderEnum.INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP;

/**
 * The Metadata Package are ZIP files in the classpath, the import mode defined by packages.xml
 *
 * <p>The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class InstallMetadataPackagesActivatorStep implements ModuleActivatorStep {
  @Override
  public int getOrder() {
    return INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP.ordinal();
  }

  @Override
  public void startup(Log log) throws Exception {
    MetadataUtil.setupStandardMetadata(getClass().getClassLoader());
  }
}
