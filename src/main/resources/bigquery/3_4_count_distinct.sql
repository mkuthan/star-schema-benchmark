SELECT
    C_CITY,
    S_CITY,
    EXTRACT(YEAR FROM LO_ORDERDATE) AS year,
    SUM(LO_REVENUE) AS revenue,
    COUNT(DISTINCT LO_CUSTKEY) AS unique_customers
FROM `lineorder_flat`
WHERE (C_CITY = 'UNITED KI1' OR C_CITY = 'UNITED KI5') AND (S_CITY = 'UNITED KI1' OR S_CITY = 'UNITED KI5')
  AND EXTRACT(YEAR FROM LO_ORDERDATE) = 1997 AND EXTRACT(MONTH FROM LO_ORDERDATE) = 12
GROUP BY C_CITY, S_CITY, year
ORDER BY year ASC, revenue DESC
