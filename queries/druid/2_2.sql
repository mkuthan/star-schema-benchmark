SELECT
    SUM(LO_REVENUE),
    EXTRACT(YEAR FROM __time) AS "year",
    P_BRAND
FROM "star_schema_benchmark"
WHERE P_BRAND >= 'MFGR#2221' AND P_BRAND <= 'MFGR#2228' AND S_REGION = 'ASIA'
GROUP BY 2, P_BRAND
ORDER BY 2, P_BRAND
