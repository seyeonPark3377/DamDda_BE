INSERT INTO admin_approvals (approval, project_id, approval_text)
SELECT
    r AS approval,
    project_id,
    CASE
        WHEN r = 0 THEN '승인 대기 중...'
        WHEN r = 1 THEN '승인'
        ELSE '거절사유texttexttext거절사유'
        END AS approval_text
FROM (
         SELECT
             CASE
                 WHEN RAND() < 2.0 / 7 THEN 0 -- 2/7 확률로 0
                 WHEN RAND() < 6.0 / 7 THEN 1 -- 4/7 확률로 1
                 ELSE 2                       -- 1/7 확률로 2
                 END AS r,
             project_id
         FROM (
                  SELECT 1 AS project_id UNION ALL
                  SELECT 2 UNION ALL
                  SELECT 3 UNION ALL
                  SELECT 4 UNION ALL
                  SELECT 5 UNION ALL
                  SELECT 6 UNION ALL
                  SELECT 7 UNION ALL
                  SELECT 8 UNION ALL
                  SELECT 9 UNION ALL
                  SELECT 10 UNION ALL
                  SELECT 11 UNION ALL
                  SELECT 12 UNION ALL
                  SELECT 13 UNION ALL
                  SELECT 14 UNION ALL
                  SELECT 15 UNION ALL
                  SELECT 16 UNION ALL
                  SELECT 17 UNION ALL
                  SELECT 18 UNION ALL
                  SELECT 19 UNION ALL
                  SELECT 20 UNION ALL
                  SELECT 21 UNION ALL
                  SELECT 22 UNION ALL
                  SELECT 23 UNION ALL
                  SELECT 24 UNION ALL
                  SELECT 25 UNION ALL
                  SELECT 26 UNION ALL
                  SELECT 27 UNION ALL
                  SELECT 28 UNION ALL
                  SELECT 29 UNION ALL
                  SELECT 30 UNION ALL
                  SELECT 31 UNION ALL
                  SELECT 32 UNION ALL
                  SELECT 33 UNION ALL
                  SELECT 34 UNION ALL
                  SELECT 35 UNION ALL
                  SELECT 36 UNION ALL
                  SELECT 37 UNION ALL
                  SELECT 38 UNION ALL
                  SELECT 39 UNION ALL
                  SELECT 40 UNION ALL
                  SELECT 41 UNION ALL
                  SELECT 42 UNION ALL
                  SELECT 43 UNION ALL
                  SELECT 44 UNION ALL
                  SELECT 45 UNION ALL
                  SELECT 46 UNION ALL
                  SELECT 47 UNION ALL
                  SELECT 48 UNION ALL
                  SELECT 49 UNION ALL
                  SELECT 50 UNION ALL
                  SELECT 51 UNION ALL
                  SELECT 52 UNION ALL
                  SELECT 53 UNION ALL
                  SELECT 54 UNION ALL
                  SELECT 55 UNION ALL
                  SELECT 56 UNION ALL
                  SELECT 57 UNION ALL
                  SELECT 58 UNION ALL
                  SELECT 59 UNION ALL
                  SELECT 60 UNION ALL
                  SELECT 61 UNION ALL
                  SELECT 62 UNION ALL
                  SELECT 63 UNION ALL
                  SELECT 64 UNION ALL
                  SELECT 65 UNION ALL
                  SELECT 66 UNION ALL
                  SELECT 67 UNION ALL
                  SELECT 68 UNION ALL
                  SELECT 69 UNION ALL
                  SELECT 70
              ) AS project_ids
     ) AS random_values;
