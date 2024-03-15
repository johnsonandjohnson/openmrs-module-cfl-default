package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/** Adds Programs. */
public class ProgramsMetadata extends VersionedMetadataBundle {

  private static final String HIV_PROGRAM_UUID = "06c596e7-53dd-44c1-9609-f1406fd9e76d";

  @Override
  public int getVersion() {
    return 2;
  }

  @Override
  protected void installEveryTime() {
    // nothing to do
  }

  @Override
  protected void installNewVersion() {
    if (isHIVProgramDoesNotExist()) {
      install(newHIVProgram());
    }
  }

  private Program newHIVProgram() {
    final Concept hivConcept = Context.getConceptService().getConceptByName("HIV");
    final Program hivProgram = new Program();
    // Hardcoded because we reference it in HTML forms
    hivProgram.setUuid(HIV_PROGRAM_UUID);
    hivProgram.setName("HIV");
    hivProgram.setConcept(hivConcept);
    return hivProgram;
  }

  private boolean isHIVProgramDoesNotExist() {
    return Context.getProgramWorkflowService().getProgramByUuid(HIV_PROGRAM_UUID) == null;
  }
}
