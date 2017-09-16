--data = LOAD 'test_ad_data' AS (keyword:chararray, campaign_id:chararray,
--             date:chararray, time:chararray, display_site:chararray, 
--             was_clicked:int, cpc:int);

data = LOAD 'dualcore/ad_data[12]' AS (campaign_id:chararray,
             date:chararray, time:chararray,
             keyword:chararray, display_site:chararray,
             placement:chararray, was_clicked:int, cpc:int);

filtered_data = FILTER data BY was_clicked == 1;

grouped_data = GROUP filtered_data BY display_site;

total_cost = FOREACH grouped_data GENERATE group, SUM(filtered_data.cpc) AS cost;

sorted_data = ORDER total_cost BY cost ASC;

first_four = LIMIT sorted_data 4;

DUMP first_four;
