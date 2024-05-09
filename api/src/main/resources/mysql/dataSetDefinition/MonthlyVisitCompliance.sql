SELECT CONCAT(MONTHNAME(v.date_started), ' ', YEAR(v.date_started)) AS monthName,
       va.value_reference AS visitStatus,
       COUNT(DISTINCT v.visit_id) AS visitCount
FROM visit v
INNER JOIN visit_attribute va ON v.visit_id = va.visit_id
INNER JOIN visit_attribute_type vat ON vat.visit_attribute_type_id = va.attribute_type_id
INNER JOIN patient_identifier pi ON v.patient_id = pi.patient_id
WHERE vat.name = 'Visit Status'
  AND v.date_started >= (SELECT DATE_FORMAT(DATE(DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 YEAR), INTERVAL 1 MONTH)),'%Y-%m-01'))
  AND v.date_started <= (SELECT DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH), '%Y-%m-01'))
  AND v.voided = 0
  AND vat.retired = 0
  AND va.voided = 0
  AND pi.location_id in (:locationIds)
GROUP BY monthName,
         visitStatus
ORDER BY v.date_started;