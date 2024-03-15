package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentryui.HtmlFormUtil;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.ui.framework.resource.ResourceFactory;

import java.util.Arrays;
import java.util.List;

/** Loads all HTML forms from `omod/src/main/resources/htmlforms`. */
public class HtmlFormsMetadata extends VersionedMetadataBundle {

  @Override
  public int getVersion() {
    return 21;
  }

  @Override
  protected void installEveryTime() {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    final ResourceFactory resourceFactory = ResourceFactory.getInstance();
    final FormService formService = Context.getFormService();
    final HtmlFormEntryService htmlFormEntryService =
        Context.getService(HtmlFormEntryService.class);

    final List<String> htmlforms =
        Arrays.asList(
            "cfldefault:htmlforms/cfl-HIV.xml",
            "cfldefault:htmlforms/cfl-check-in.xml",
            "cfldefault:htmlforms/cfl-visit-note.xml",
            "cfldefault:htmlforms/cfl-medicine-refill.xml",
            "cfldefault:htmlforms/cfl-sputum-visit-note.xml",
            "cfldefault:htmlforms/encounters-form.xml",
            "cfldefault:htmlforms/cfl-hiv-enrollment.xml",
            "cfldefault:htmlforms/cfl-hiv-intake.xml",
            "cfldefault:htmlforms/cfl-migrated-lab-result.xml",
            "cfldefault:htmlforms/cfl-migrated-medical-history.xml",
            "cfldefault:htmlforms/cfl-migrated-tb-data.xml");

    for (String htmlform : htmlforms) {
      HtmlFormUtil.getHtmlFormFromUiResource(
          resourceFactory, formService, htmlFormEntryService, htmlform);
    }
  }
}
