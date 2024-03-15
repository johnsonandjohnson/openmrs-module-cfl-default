CREATE FUNCTION GET_DAYS_BEFORE_REMINDER(patientId long)
RETURNS varchar(255)
BEGIN
DECLARE result varchar(255);
DECLARE projectSlug varchar(255);
DECLARE basicGPName varchar(255);
DECLARE basicGPValue varchar(255);
DECLARE projectGPName varchar(255);

SET projectSlug = (
  SELECT
    mp.slug
  FROM
    multiproject_project mp
    INNER JOIN location_attribute la ON mp.uuid = la.value_reference
    INNER JOIN location_attribute_type lat ON lat.location_attribute_type_id = la.attribute_type_id
    INNER JOIN location l ON l.location_id = la.location_id
    INNER JOIN person_attribute pa ON pa.value = l.uuid
    INNER JOIN person_attribute_type pat ON pat.person_attribute_type_id = pa.person_attribute_type_id
    INNER JOIN global_property gp ON gp.property_value = pat.name
  WHERE
    mp.retired = 0
    AND la.voided = 0
    AND lat.name = 'Project'
    AND lat.retired = 0
    AND l.retired = 0
    AND pa.person_id = patientId
    AND pa.voided = 0
    AND pat.retired = 0
    AND gp.property = 'cfl.person.attribute.location'
);

SET basicGPName = 'message.daysToCallBeforeVisit.default';
SET basicGPValue = (SELECT property_value from global_property where property = basicGPName);

IF (ISNULL(projectSlug) > 0)
THEN
 SET result = basicGPValue;
END IF;

SET projectGPName = CONCAT(basicGPName, ".", projectSlug);
SET result = (SELECT property_value from global_property where property = projectGPName);
IF (ISNULL(result) > 0)
THEN
 SET result = basicGPValue;
END IF;

RETURN result;

END