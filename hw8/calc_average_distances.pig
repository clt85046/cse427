-- Register DataFu and define an alias for the function
REGISTER '/usr/lib/pig/datafu-*.jar';
DEFINE DIST datafu.pig.geo.HaversineDistInMiles;


cust_locations = LOAD '/dualcore/distribution/cust_locations/'
                   AS (zip:chararray,
                       lat:double,
                       lon:double);

warehouses = LOAD '/dualcore/distribution/warehouses.tsv'
                   AS (zip:chararray,
                       lat:double,
                       lon:double);
             


-- Create a record for every combination of customer and
-- proposed distribution center location.
records = CROSS cust_locations,warehouses;

-- Calculate the distance from the customer to the warehouse
distance = foreach records generate warehouses::zip as zip,DIST(cust_locations::lat,cust_locations::lon,warehouses::lat,warehouses::lon) as dist;

-- Calculate the average distance for all customers to each warehouse
grouped = group distance by zip;
averdist = foreach grouped generate group as zip,AVG(distance.dist) as average;

-- Display the result to the screen
dump averdist;
