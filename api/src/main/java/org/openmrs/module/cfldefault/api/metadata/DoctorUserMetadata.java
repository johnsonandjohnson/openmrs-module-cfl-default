/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfldefault.api.metadata;

import org.openmrs.Person;

public class DoctorUserMetadata extends AbstractUserMetadata {

  private static final String GIVEN_NAME = "CfL";
  private static final String FAMILY_NAME = "Doctor";
  private static final String OTHER_GENDER = "O";
  private static final String EMAIL_VALUE = "cfl@cfl.xyz";
  private static final String PHONE_NUMBER_VALUE = "+15555551234";
  private static final String USERNAME = "cfldoctor1";
  private static final String PASSWORD = "Cfldoctor1";
  private static final String DOCTOR_ROLE = "Privilege Level: Doctor";

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  public void installNewVersion() throws Exception {
    if (isUserDoesNotExist(USERNAME)) {
      setUpDoctorUser();
    }
  }

  private void setUpDoctorUser() {
    Person doctor = createDoctorPerson();
    createUserProvider(doctor, USERNAME);

    userService.createUser(createUserObject(doctor, USERNAME, DOCTOR_ROLE), PASSWORD);
  }

  private Person createDoctorPerson() {
    Person doctor = new Person();
    doctor.setGender(OTHER_GENDER);
    doctor.setNames(createUserNames(doctor, GIVEN_NAME, FAMILY_NAME));
    doctor.setAttributes(createUserAttributes(doctor, EMAIL_VALUE, PHONE_NUMBER_VALUE));

    return personService.savePerson(doctor);
  }
}