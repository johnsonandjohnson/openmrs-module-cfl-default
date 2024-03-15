package org.openmrs.module.cfldefault.api.activator.impl;

/**
 * The enumeration of all activator steps in order of execution.
 *
 * <p>The order in which enum constants appear in the source code directly reflects the order of
 * steps execution.
 */
public enum ModuleActivatorStepOrderEnum {

  /** @see InstallMetadataPackagesActivatorStep */
  INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP,

  /** @see InstallMetadataBundleActivatorStep */
  INSTALL_METADATA_BUNDLE_ACTIVATOR_STEP,

  /** @see FixConceptConfigurationStep */
  FIX_CONCEPT_CONFIGURATION_STEP;
}
