-- load the data sets
orders = LOAD '/dualcore/orders' AS (order_id:int,
             cust_id:int,
             order_dtm:chararray);

details = LOAD '/dualcore/order_details' AS (order_id:int,
             prod_id:int);

products = LOAD '/dualcore/products' AS (prod_id:int,
             brand:chararray,
             name:chararray,
             price:int,
             cost:int,
             shipping_wt:int);
order2012 = FILTER orders by order_dtm matches '^2012.*$';

grouped1 = group order2012 by cust_id;
grouped2 = foreach grouped1 generate group,COUNT(order2012) as ordernums;

grouped3 = filter grouped2 by ordernums >= 5;
result = join grouped3 by group, order2012 by cust_id;
result = join result by order_id,details by order_id;

customer = join result by details::prod_id,products by prod_id;
customercost = foreach customer generate grouped3::group as cust_id,products::price as price;

totals = group customercost by cust_id;
custcost = foreach totals generate group,(SUM(customercost.price)/100) as total;

SPLIT custcost INTO
platinum IF total >= 10000,
gold IF total >= 5000 AND total < 10000,
silver IF total >=2500 And total <5000;

STORE platinum INTO 'dualcore/loyalty/platinum';
STORE gold INTO 'dualcore/loyalty/gold';
STORE silver INTO 'dualcore/loyalty/silver';

--
/*
 * TODO: Find all customers who had at least five orders
 *       during 2012. For each such customer, calculate 
 *       the total price of all products he or she ordered
 *       during that year. Split those customers into three
 *       new groups based on the total amount:
 *
 *        platinum: At least $10,000
 *        gold:     At least $5,000 but less than $10,000
 *        silver:   At least $2,500 but less than $5,000
 *
 *       Write the customer ID and total price into a new
 *       directory in HDFS. After you run the script, use
 *       'hadoop fs -getmerge' to create a local text file
 *       containing the data for each of these three groups.
 *       Finally, use the 'head' or 'tail' commands to check 
 *       a few records from your results, and then use the  
 *       'wc -l' command to count the number of customers in 
 *       each group.
 */
