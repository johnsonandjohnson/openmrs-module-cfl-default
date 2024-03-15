SELECT
  date,
  callStatus,
  numberOfPatients,
  ROUND(
    (
      numberOfPatients / totalPatients * 100
    ),
    0
  ) AS percentage
FROM
  (
    SELECT
      subquery.date,
      subquery.callStatus,
      subquery.numberOfPatients,
      totalPatients.totalPatients
    FROM
      (
        SELECT
          DATE_FORMAT(c.date_created, "%d %b %Y") AS date,
          'Adherent' AS callStatus,
          SUM(
            c.status = 'ANSWERED'
            OR (
              c.endNode <> ''
              AND c.externalId IS NOT NULL
              AND c.status NOT IN (
                'OPENMRS_INITIATED', 'FAILED', 'IN_PROGRESS',
                'STARTED', 'CANCELLED', 'REJECTED',
                'RINGING', 'TIMEOUT', 'UNANSWERED',
                'BUSY'
              )
            )
          ) AS numberOfPatients
        FROM
          cfl_calls c
          JOIN patient p ON c.actorId = p.patient_id
          AND p.voided = 0
          JOIN person_attribute pa ON p.patient_id = pa.person_id
          JOIN location l ON pa.value = l.uuid
        WHERE
          c.cfl_calls_id IN (
            SELECT
              MAX(c1.cfl_calls_id)
            FROM
              cfl_calls c1
              JOIN patient p1 ON c1.actorId = p1.patient_id
              AND p1.voided = 0
              JOIN person_attribute pa1 ON p1.patient_id = pa1.person_id
              AND pa1.voided = 0
              JOIN location l1 ON pa1.value = l1.uuid
            WHERE
              c1.date_created >= CURDATE() - INTERVAL 30 DAY
              AND l1.location_id IN (: locationIds)
            GROUP BY
              c1.refKey
          )
        GROUP BY
          DATE_FORMAT(c.date_created, "%d %b %Y")
        UNION ALL
        SELECT
          DATE_FORMAT(c.date_created, "%d %b %Y") AS date,
          'Not adherent' AS callStatus,
          SUM(
            c.status IN (
              'STARTED', 'CANCELLED', 'REJECTED',
              'RINGING', 'TIMEOUT', 'UNANSWERED',
              'BUSY'
            )
            OR (c.endNode = '')
          ) AS numberOfPatients
        FROM
          cfl_calls c
          JOIN patient p ON c.actorId = p.patient_id
          AND p.voided = 0
          JOIN person_attribute pa ON p.patient_id = pa.person_id
          AND pa.voided = 0
          JOIN location l ON pa.value = l.uuid
        WHERE
          c.cfl_calls_id IN (
            SELECT
              MAX(c1.cfl_calls_id)
            FROM
              cfl_calls c1
              JOIN patient p1 ON c1.actorId = p1.patient_id
              AND p1.voided = 0
              JOIN person_attribute pa1 ON p1.patient_id = pa1.person_id
              AND pa1.voided = 0
              JOIN location l1 ON pa1.value = l1.uuid
            WHERE
              c1.date_created >= CURDATE() - INTERVAL 30 DAY
              AND l1.location_id IN (: locationIds)
            GROUP BY
              c1.refKey
          )
        GROUP BY
          DATE_FORMAT(c.date_created, "%d %b %Y")
        UNION ALL
        SELECT
          DATE_FORMAT(c.date_created, "%d %b %Y") AS date,
          'Technical issue' AS callStatus,
          SUM(
            c.status IN (
              'OPENMRS_INITIATED', 'FAILED', 'IN_PROGRESS'
            )
            OR (c.externalId IS NULL)
          ) AS numberOfPatients
        FROM
          cfl_calls c
          JOIN patient p ON c.actorId = p.patient_id
          AND p.voided = 0
          JOIN person_attribute pa ON p.patient_id = pa.person_id
          AND pa.voided = 0
          JOIN location l ON pa.value = l.uuid
        WHERE
          c.cfl_calls_id IN (
            SELECT
              MAX(c1.cfl_calls_id)
            FROM
              cfl_calls c1
              JOIN patient p1 ON c1.actorId = p1.patient_id
              AND p1.voided = 0
              JOIN person_attribute pa1 ON p1.patient_id = pa1.person_id
              AND pa1.voided = 0
              JOIN location l1 ON pa1.value = l1.uuid
            WHERE
              c1.date_created >= CURDATE() - INTERVAL 30 DAY
              AND l1.location_id IN (: locationIds)
            GROUP BY
              c1.refKey
          )
        GROUP BY
          DATE_FORMAT(c.date_created, "%d %b %Y")
      ) AS subquery
      JOIN (
        SELECT
          DATE_FORMAT(c.date_created, "%d %b %Y") AS date,
          SUM(
            CASE WHEN c.status = 'ANSWERED'
            OR (
              c.endNode <> ''
              AND c.externalId IS NOT NULL
              AND c.status NOT IN (
                'OPENMRS_INITIATED', 'FAILED', 'IN_PROGRESS',
                'STARTED', 'CANCELLED', 'REJECTED',
                'RINGING', 'TIMEOUT', 'UNANSWERED',
                'BUSY'
              )
            ) THEN 1 ELSE 0 END
          ) + SUM(
            CASE WHEN c.status IN (
              'STARTED', 'CANCELLED', 'REJECTED',
              'RINGING', 'TIMEOUT', 'UNANSWERED',
              'BUSY'
            )
            OR (c.endNode = '') THEN 1 ELSE 0 END
          ) + SUM(
            CASE WHEN c.status IN (
              'OPENMRS_INITIATED', 'FAILED', 'IN_PROGRESS'
            )
            OR (c.externalId IS NULL) THEN 1 ELSE 0 END
          ) AS totalPatients
        FROM
          cfl_calls c
          JOIN patient p ON c.actorId = p.patient_id
          AND p.voided = 0
          JOIN person_attribute pa ON p.patient_id = pa.person_id
          JOIN location l ON pa.value = l.uuid
        WHERE
          c.cfl_calls_id IN (
            SELECT
              MAX(c1.cfl_calls_id)
            FROM
              cfl_calls c1
              JOIN patient p1 ON c1.actorId = p1.patient_id
              AND p1.voided = 0
              JOIN person_attribute pa1 ON p1.patient_id = pa1.person_id
              AND pa1.voided = 0
              JOIN location l1 ON pa1.value = l1.uuid
            WHERE
              c1.date_created >= CURDATE() - INTERVAL 30 DAY
              AND l1.location_id IN (: locationIds)
            GROUP BY
              c1.refKey
          )
        GROUP BY
          DATE_FORMAT(c.date_created, "%d %b %Y")
      ) AS totalPatients ON subquery.date = totalPatients.date
  ) AS result
ORDER BY
  STR_TO_DATE(date, '%d %b %Y') ASC;