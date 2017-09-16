orders = LOAD '/dualcore/orders' AS (order_id,cust_id,order_dtm);
details = LOAD '/dualcore/order_details' AS (order_id,prod_id);

joined = JOIN details BY order_id, orders BY order_id;
-- DUMP joined;

grouped = GROUP joined BY orders::order_id;
DESCRIBE grouped;

counted = FOREACH grouped GENERATE group, COUNT(joined) AS num_items, 
					FLATTEN(joined.orders::order_dtm) AS order_dtm;
counted = DISTINCT counted;
counted_limit = LIMIT counted 10;
DUMP counted_limit;
filtered_counted = FILTER counted by order_dtm MATCHES '^2013-05-.*$';

grouped_all = GROUP filtered_counted ALL;
average = FOREACH grouped_all GENERATE AVG(filtered_counted.num_items);
DUMP average;
