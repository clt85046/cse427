package stubs;

import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Partitioner;

public class SentimentPartitioner<Text, IntWritable> extends Partitioner<Text, IntWritable> implements
    Configurable {

  private Configuration configuration;
  Set<String> positive = new HashSet<String>();
  Set<String> negative = new HashSet<String>();

  /**
   * Set up the positive and negative hash set in the setConf method.
   */
  @Override
  public void setConf(Configuration configuration) {
     /*
     * Add the positive and negative words to the respective sets using the files 
     * positive-words.txt and negative-words.txt.
     */
    /*
     * TODO implement 
     */
	  String Text=null;

	/* used for partitionertest
String pathname="src/negative-words.txt";
File negative1=new File(pathname);
String pathname1="src/positive-words.txt";
File positive1=new File(pathname1);*/

	  File positive1=new File("positive-words.txt");
	  File negative1=new File("negative-words.txt");
	 
	  InputStreamReader inputStream = null;
	
		try {
		
			inputStream = new InputStreamReader(new FileInputStream(negative1));
	
	 BufferedReader buffReader=new BufferedReader(inputStream);
	
		while((Text=buffReader.readLine())!=null)
		 {
			 if(Text.length()>0)
			 {if(Text.charAt(0)!=';')
			 {
				 negative.add(Text);
			 }
			 }
		 }

		inputStream.close();
		buffReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader inputStream1 = null;
		
		try {
			inputStream1 = new InputStreamReader(new FileInputStream(positive1));
		
	
	 BufferedReader buffReader1=new BufferedReader(inputStream1);
	
		while((Text=buffReader1.readLine())!=null)
		 {
			 if(Text.length()>0)
			 {if(Text.charAt(0)!=';')
			 {
				 positive.add(Text);
			 }
			 }
		 }

		inputStream1.close();
		buffReader1.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	 
	
  }

  /**
   * Implement the getConf method for the Configurable interface.
   */
  @Override
  public Configuration getConf() {
    return configuration;
  }

  /**
   * You must implement the getPartition method for a partitioner class.
   * This method receives the words as keys (i.e., the output key from the mapper.)
   * It should return an integer representation of the sentiment category
   * (positive, negative, neutral).
   * 
   * For this partitioner to work, the job configuration must have been
   * set so that there are exactly 3 reducers.
   */
  public int getPartition(Text key, IntWritable value, int numReduceTasks) 
  {
	  if (positive.contains(key.toString()))
	  {
     return 0;}
	  else{if(negative.contains(key.toString()))
	  {return 1;}
	  else {return 2;}
	  
	  }
	  /*
     * TODO implement
     * Change the return 0 statement below to return the number of the sentiment 
     * category; use 0 for positive words, 1 for negative words, and 2 for neutral words. 
     * Use the sets of positive and negative words to find out the sentiment.
     *
     * Hint: use positive.contains(key.toString()) and negative.contains(key.toString())
     * If a word appears in both lists assume it is positive. That is, once you found 
     * that a word is in the positive list you do not need to check if it is in the 
     * negative list. 
     */
  }
}
