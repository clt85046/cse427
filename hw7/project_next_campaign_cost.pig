-- Load only the ad_data1 and ad_data2 directories
--data = LOAD 'dualcore/ad_data[1-2]/part*' 
data = LOAD 'dualcore/ad_data[1-2]/part*' AS (campaign_id:chararray,
             date:chararray, time:chararray,
             keyword:chararray, display_site:chararray,
             placement:chararray, was_clicked:int, cpc:int);

-- Include only records where the ad was clicked
--clicked = FILTER data BY was_clicked != 1;
--clicked = FILTER clicked BY was_clicked != 0;
--dump clicked;
-- A: Group everything so we can call the aggregate function
group1 = group data all;

-- B: Count the records 
total = foreach group1 generate MAX(data.cpc)*50000;

-- C: Display the result to the screen
dump total;

