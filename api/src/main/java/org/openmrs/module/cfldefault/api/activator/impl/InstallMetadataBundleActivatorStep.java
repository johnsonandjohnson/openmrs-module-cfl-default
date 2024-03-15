package org.openmrs.module.cfldefault.api.activator.impl;

import org.apache.commons.logging.Log;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfldefault.api.CfLDefaultConstants;
import org.openmrs.module.cfldefault.api.activator.ModuleActivatorStep;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatadeploy.bundle.MetadataBundle;

import java.util.List;
import java.util.stream.Collectors;

import static org.openmrs.api.context.Context.getRegisteredComponent;
import static org.openmrs.module.cfldefault.api.activator.impl.ModuleActivatorStepOrderEnum.INSTALL_METADATA_BUNDLE_ACTIVATOR_STEP;

/**
 * The Metadata Bundles are Java classes (beans) which add metadata to the system.
 *
 * <p>The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class InstallMetadataBundleActivatorStep implements ModuleActivatorStep {
  @Override
  public int getOrder() {
    return INSTALL_METADATA_BUNDLE_ACTIVATOR_STEP.ordinal();
  }

  @Override
  public void startup(Log log) {
    final MetadataDeployService service =
        getRegisteredComponent("metadataDeployService", MetadataDeployService.class);
    final List<MetadataBundle> cflDefaultBundles =
        Context.getRegisteredComponents(MetadataBundle.class).stream()
            .filter(
                component ->
                    component
                        .getClass()
                        .getName()
                        .startsWith(CfLDefaultConstants.MODULE_API_PACKAGE))
            .collect(Collectors.toList());

    service.installBundles(cflDefaultBundles);
  }
}
