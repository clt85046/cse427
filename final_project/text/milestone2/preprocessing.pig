set pig.splitCombination false;
data = LOAD 'hw_reviews/hw*' USING PigStorage('\n', '-tagsource') as (filename:chararray,word:chararray);
data = filter data by word != '	';
describe data;
data2 = foreach data generate filename, flatten(TOKENIZE(word,'\t,., ')) as word;
data2 = filter data2 by (word != ' ');
num = group data2 by filename;
num1 = foreach num generate group,COUNT(data2) as nums;
num1 = filter num1 by nums >=  50;
data3 = join num1 by group,data by filename;
data3 = foreach data3 generate filename,word;
describe data3;
split data3 into 
hw1_review IF SUBSTRING(filename,2,3) == '1',
hw2_review IF SUBSTRING(filename,2,3)=='2',
hw3_review IF SUBSTRING(filename,2,3)=='3',
hw4_review IF SUBSTRING(filename,2,3)=='4',
hw5_review IF SUBSTRING(filename,2,3)=='5',
hw6_review IF SUBSTRING(filename,2,3)=='6',
hw7_review IF SUBSTRING(filename,2,3)=='7',
hw8_review IF SUBSTRING(filename,2,3)=='8';

STORE hw1_review INTO 'outputs/hw1';
STORE hw2_review INTO 'outputs/hw2';
STORE hw3_review INTO 'outputs/hw3';
STORE hw4_review INTO 'outputs/hw4';
STORE hw5_review INTO 'outputs/hw5';
STORE hw6_review INTO 'outputs/hw6';
STORE hw7_review INTO 'outputs/hw7';
STORE hw8_review INTO 'outputs/hw8';
