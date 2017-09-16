-- Define an alias for the supplied Python script we 
-- use to parse the metadata from the MP3 files
DEFINE tagreader `readtags.py` SHIP('readtags.py');

-- load the list of MP3 files to analyze
calls = LOAD 'call_list.txt' AS (file:chararray);
--calls = LOAD 'test.txt' AS (file:chararray);

-- Use the STREAM keyword to invoke our script, and parse
-- MP3 metadata, returning the fields shown here
metadata = STREAM calls THROUGH tagreader AS (path:chararray,
           category:chararray,
           agent_id:chararray,
           customer_id:chararray,
           timestamp:chararray);
--dump metadata;

-- TODO (A): Replace the hardcoded '2013-04' with
-- a parameter specified on the command line
by_month = FILTER metadata BY SUBSTRING(timestamp, 0, 7) == '$date';


-- TODO (B): Count calls by category
grouped = group by_month by category;
countgrouped = foreach grouped generate group as category,COUNT(by_month) as totalnum;

-- TODO (C): Display the top three categories to the screen
--ordered = order countgrouped by totalnum DESC;
top3 = limit countgrouped 3;
dump top3;
