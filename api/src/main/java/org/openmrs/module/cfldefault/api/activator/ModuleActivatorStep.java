package org.openmrs.module.cfldefault.api.activator;

import org.apache.commons.logging.Log;
import org.openmrs.module.cfldefault.api.activator.impl.ModuleActivatorStepOrderEnum;

/**
 * The ModuleActivatorStep Class.
 *
 * <p>The ModuleActivatorSteps are beans which performs single logical step during module startup.
 * E.g.: fix concepts, import necessary data, validate db state.
 *
 * <p>Each implementation has its order value which determines the order in which these steps are
 * executed, the lower the order value the sooner it gets executed.
 *
 * <p>Each step is executed only once during the module startup.
 */
public interface ModuleActivatorStep {
  /**
   * <b>Impl note:</b> Use {@link ModuleActivatorStepOrderEnum} to generate an actual value to
   * return by this method.
   *
   * @return the integer used to determine when this step has to execute, lower value goes before
   *     higher
   */
  int getOrder();

  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  void startup(Log log) throws Exception;
}
