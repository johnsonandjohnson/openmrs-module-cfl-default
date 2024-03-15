package org.openmrs.module.cfldefault.api.activator.impl;

import org.apache.commons.logging.Log;
import org.openmrs.ConceptSource;
import org.openmrs.Duration;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfldefault.api.activator.ModuleActivatorStep;

/**
 * This step ensures the configuration related to Concepts is correct. This is needed because the
 * OCL import doesn't create 100% correct data.
 */
public class FixConceptConfigurationStep implements ModuleActivatorStep {
  private static final String SNOMED_CT_SOURCE = "SNOMED-CT";

  @Override
  public int getOrder() {
    return ModuleActivatorStepOrderEnum.FIX_CONCEPT_CONFIGURATION_STEP.ordinal();
  }

  @Override
  public void startup(Log log) {
    final ConceptService conceptService = Context.getConceptService();

    // see org.openmrs.Duration.getCode
    final ConceptSource snomed = conceptService.getConceptSourceByName(SNOMED_CT_SOURCE);
    snomed.setHl7Code(Duration.SNOMED_CT_CONCEPT_SOURCE_HL7_CODE);
    conceptService.saveConceptSource(snomed);
  }
}
