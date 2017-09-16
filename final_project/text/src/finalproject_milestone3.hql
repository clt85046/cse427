--This are the scripts for step1
create table negwords
(word STRING
)
ROW format delimited
fields terminated by '\n';
load data inpath 'neg-words.txt' into table negwords;

create table poswords
(word STRING
)
ROW format delimited
fields terminated by '\n';
load data inpath 'pos-words.txt' into table poswords;

create table test
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test1
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test2
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test3
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test4
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test5
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test6
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test7
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table test8
(hw_number INT,
 review STRING
)
ROW format delimited
fields terminated by '\t';

--load data local inpath 'test.txt' into table test;


create table sample
(hw_number int,
 word array<string>
)
ROW format delimited
fields terminated by '\t'
collection items terminated by ' ';



create table wordlist 
(hw_number int,
 wordlist string
)
ROW format delimited
fields terminated by '\t';


create table poswordlist 
(hw_number int,
 wordlist string
)
ROW format delimited
fields terminated by '\t';


create table negwordlist 
(hw_number int,
 wordlist string
)
ROW format delimited
fields terminated by '\t';

create table negwordnum 
(hw_number int,
 wordnum int
)
ROW format delimited
fields terminated by '\t';

create table poswordnum 
(hw_number int,
wordnum int
)
ROW format delimited
fields terminated by '\t';

create table npwordnum 
(hw_number int,
 poswordnum int,
negwordnum int
)
ROW format delimited
fields terminated by '\t';


create table negwordfreq 
(
 word string,
wordfreq int
)
ROW format delimited
fields terminated by '\t';




create table poswordfreq 
(
 word string,
wordfreq int
)
ROW format delimited
fields terminated by '\t';

load data inpath 'out/part-r-00000' into table test;

load data inpath 'out/hw1/part-r-00000' into table test1;
load data inpath 'out/hw2/part-r-00000' into table test2;
load data inpath 'out/hw3/part-r-00000' into table test3;
load data inpath 'out/hw4/part-r-00000' into table test4;
load data inpath 'out/hw5/part-r-00000' into table test5;
load data inpath 'out/hw6/part-r-00000' into table test6;
load data inpath 'out/hw7/part-r-00000' into table test7;
load data inpath 'out/hw8/part-r-00000' into table test8;


insert overwrite table sample
select hw_number,word
from (select hw_number,split(review,' ') as word from test8) tt;

insert overwrite table wordlist
select hw_number,wordlist 
from sample lateral view explode(word) xx as wordlist;

insert overwrite table poswordlist
select hw_number,wordlist
from (select hw_number, wordlist from wordlist left outer join poswords
on (wordlist.wordlist = poswords.word)
where word is not null) k; 

insert overwrite table negwordlist
select hw_number,wordlist
from (select hw_number, wordlist from wordlist left outer join negwords
on (wordlist.wordlist = negwords.word)
where word is not null) k; 

insert overwrite table negwordnum
select hw_number,COUNT(wordlist) 
from negwordlist group by hw_number;

insert overwrite table poswordnum
select hw_number,COUNT(wordlist) 
from poswordlist group by hw_number;

insert overwrite table npwordnum
select p.hw_number,p.wordnum,n.wordnum
from poswordnum p
join negwordnum n
on (p.hw_number = n.hw_number)
where p.hw_number == n.hw_number;

insert overwrite table negwordfreq
select wordlist,COUNT(hw_number)as wordfreq from negwordlist group by wordlist order by wordfreq desc limit 5;

insert overwrite table poswordfreq
select wordlist,COUNT(hw_number)as wordfreq from poswordlist group by wordlist order by wordfreq desc limit 5;

--This are the scripts for step2
create table ng
(
 review STRING
)
ROW format delimited
fields terminated by '\t';

create table nga
(
 review array<struct<ngram:array<string>,estfrequency:double>>
)
ROW format delimited
fields terminated by '\t';


create table ngaer
(
ngram array<string>,
estfrequency double
)
ROW format delimited
fields terminated by '\t'
collection items terminated by ' ';


create table ngaers
(
ngram array<string>,
estfrequency double
)
ROW format delimited
fields terminated by '\t'
collection items terminated by ' ';

create table ngaers3
(
ngram array<string>,
estfrequency double
)
ROW format delimited
fields terminated by '\t'
collection items terminated by ' ';



create table ngaers2
(
ngram array<string>,
estfrequency double
)
ROW format delimited
fields terminated by '\t'
collection items terminated by ' ';

create table ngaers4
(
ngram string,
estfrequency double
)
ROW format delimited
fields terminated by '\t';

create table ngaers5
(
ngram string,
estfrequency double
)
ROW format delimited
fields terminated by '\t';

create table negnum
(num int
)
ROW format delimited
fields terminated by '\n';

create table posnum
(num int
)
ROW format delimited
fields terminated by '\n';

create table ex
(
hw_number INT
review STRING
)
ROW format delimited
fields terminated by '\t';

load data inpath '${hiveconf:path}' into table ex;

insert overwrite table ng 
select review as answer from ex;

insert overwrite table ng 
select lower(review) as answer from ng;


insert overwrite table nga 
select ngrams(sentences(lower(review)),${hiveconf:N},100000) as answer from ng;

insert overwrite table ngaer 
select X.ngram,X.estfrequency from
(select explode(review) as X from nga)Z order by estfrequency;


insert overwrite table ngaers
select tw.ngram,tw.estfrequency from ngaer tw left outer join poswords sw on(tw.ngram[1]=sw.word )
where sw.word is not null order by tw.estfrequency desc;
insert overwrite table ngaers3
select ngram,estfrequency from ngaers where ngram[0] == 'not';
insert overwrite table ngaers
select ngram,estfrequency from ngaers where ngram[0] != 'not';

insert overwrite table ngaers2 
select tw.ngram,tw.estfrequency from ngaer tw left outer join negwords sw on(tw.ngram[1]=sw.word )
where sw.word is not null order by tw.estfrequency desc;
insert into table ngaers
select ngram,estfrequency from ngaers2 where ngram[0] == 'not';
insert overwrite table ngaers2 
select ngram,estfrequency from ngaers2 where ngram[0] != 'not';
insert into table ngaers2
select ngram,estfrequency from ngaers3;

insert overwrite table ngaers2
select ngram,estfrequency from ngaers2 order by estfrequency desc;
insert overwrite table ngaers
select ngram,estfrequency from ngaers order by estfrequency desc;

insert overwrite table negnum
select sum(estfrequency) from ngaers2; 
insert overwrite table posnum
select sum(estfrequency) from ngaers; 



insert overwrite table ngaers4
select ngram[1],sum(estfrequency) as est
from ngaers group by ngram[1] order by est desc limit 3;

insert overwrite table ngaers5
select ngram[1],sum(estfrequency) as est
from ngaers2 group by ngram[1] order by est desc limit 3;



