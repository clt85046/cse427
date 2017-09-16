SELECT prod_id, FORMAT_NUMBER(avg_rating, 2) AS
avg_rating
FROM (SELECT prod_id, AVG(rating) AS avg_rating,
COUNT(*) AS num
FROM ratings
GROUP BY prod_id) rated
WHERE num >= 50
ORDER BY avg_rating ASC
LIMIT 1;