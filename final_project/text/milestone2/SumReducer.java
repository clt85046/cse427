package stubs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * To define a reduce function for your MapReduce job, subclass 
 * the Reducer class and override the reduce method.
 * The class definition requires four parameters: 
 *   The data type of the input key (which is the output key type 
 *   from the mapper)
 *   The data type of the input value (which is the output value 
 *   type from the mapper)
 *   The data type of the output key
 *   The data type of the output value
 */   
public class SumReducer extends Reducer<Text, Text,NullWritable, Text> implements Configurable{

  /*
   * The reduce method runs once for each key received from
   * the shuffle and sort phase of the MapReduce framework.
   * The method receives a key of type Text, a set of values of type
   * IntWritable, and a Context object.
   */
	 Set<String> stopwords = new HashSet<String>();
  private Configuration configuration;
  public void setConf(Configuration configuration) {
  	String movie,result1;
  	String stopword = null;
  	 File englishstop=new File("english.stop");
  	 InputStreamReader inputStream = null;
  	try{
  		
  		;
  		inputStream = new InputStreamReader(new FileInputStream(englishstop));
  		 BufferedReader reader=new BufferedReader(inputStream);
  		while((stopword=reader.readLine())!=null)
		 {
			 if(stopword.length()>0)
			 
			 {
				 stopwords.add(stopword);
			 
			 }
		 }
  	}
  	catch(FileNotFoundException e)
  	{
          e.printStackTrace();		
  	}
  	catch(IOException e)
  	{
  		e.printStackTrace();
  	}
  }
  public Configuration getConf() {
      return configuration;
    }
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		String wordCount = "";
		String title = null;
		
		/*
		 * For each value in the set of values passed to us by the mapper:
		 */
		
		for (Text value : values) {
			String valueAsString = value.toString();
			for (String word : valueAsString.split("\\W+")) {
			      if (word.length() > 0) {
			        //wordCount += 1;
			    	  if (stopwords.contains(word))
			    	  {
			         }
			    	  else{
			         wordCount = wordCount + word+" ";}
			        /*
			         * Call the write method on the Context object to emit a key
			         * and a value from the map method.
			         */
			       
			      } 
			
			//String name = tokens[0];
//			String nice = tokens[1];
			
		  /*
		   * Add the value to the word count counter for this key.
		   */
			
			//wordCount += nice;title=name;
		}
			
		
		/*
		 * Call the write method on the Context object to emit a key
		 * and a value from the reduce method. 
		 */
		
	}context.write(NullWritable.get(), new Text(key.toString().substring(2, 3)+"\t"+wordCount));
}
}