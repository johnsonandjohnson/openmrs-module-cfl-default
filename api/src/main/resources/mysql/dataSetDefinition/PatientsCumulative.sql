SELECT DATE_FORMAT(STR_TO_DATE(CONCAT(patients.year, '-W', patients.week, '-1'), '%x-W%v-%w'), '%d %b %Y') AS 'date',
  (SELECT name
   FROM location l
   WHERE l.uuid = patients.locationUuid) AS 'locationName',
       patients.total AS 'total'
FROM
  (SELECT weeks.year AS 'year',
          weeks.week AS 'week',
          weeks.locationUuid AS 'locationUuid',
          COUNT(p.date_created) AS 'total'
   FROM
     (SELECT YEAR(dates.selected_date) AS 'year',
             WEEK(dates.selected_date, 3) AS 'week',
             l.uuid AS 'locationUuid'
      FROM DATES_LIST_10K_DAYS_TABLE dates
      INNER JOIN location AS l
      WHERE dates.selected_date > CURDATE() - INTERVAL 1 YEAR
        AND dates.selected_date <= CURDATE()
        AND l.location_id in (:locationIds)
      GROUP BY YEAR(dates.selected_date),
               WEEK(dates.selected_date, 3),
               l.uuid) weeks
   LEFT JOIN (patient p
              INNER JOIN person_attribute pa ON pa.person_id = p.patient_id
              AND pa.voided = 0
              INNER JOIN person_attribute_type pat ON pat.person_attribute_type_id = pa.person_attribute_type_id
              AND pat.name = 'LocationAttribute') ON YEAR(p.date_created) = weeks.year
   AND WEEK(p.date_created, 3) = weeks.week
   AND pa.value = weeks.locationUuid
   GROUP BY weeks.year,
            weeks.week,
            weeks.locationUuid
   ORDER BY weeks.year,
            weeks.week,
            weeks.locationUuid) patients;