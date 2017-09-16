package stubs;
import java.io.BufferedReader;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/**
* Reducer's input are local top N from all mappers.
* We have a single reducer, which creates the final top N.
*
* @author Mahmoud Parsian
*
*/
public class TopNReducer extends
Reducer<NullWritable, Text, IntWritable, Text> implements Configurable{
private int N; // default
private Configuration configuration;
public void setConf(Configuration configuration) {
	String movie,result1;
	String thisLine = null;
	 File positive1=new File("movie_titles.txt");
	 InputStreamReader inputStream = null;
	try{
		
		;
		inputStream = new InputStreamReader(new FileInputStream(positive1));
		 BufferedReader reader=new BufferedReader(inputStream);
		while ((movie = reader.readLine()) != null)
		{
			String splitarray[] = movie.split(",");
			top1.put(splitarray[0].trim(),splitarray[2].trim());
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
private SortedMap<Integer, String> top = new TreeMap<Integer, String>();
private SortedMap<String, String> top1 = new TreeMap<String, String>();
@Override
public void reduce(NullWritable key, Iterable<Text> values, Context context)
throws IOException, InterruptedException {
	
for (Text value : values) {
String valueAsString = value.toString().trim();
String[] tokens = valueAsString.split(",");
String name = tokens[0];
double frequency = Double.parseDouble(tokens[1]);
int jimo = (int) frequency;
//int frequency = Integer.parseInt(tokens[1]);
top.put(jimo, name);
// keep only top N
if (top.size() > N) {
top.remove(top.firstKey());
}
}
// emit final top N
List<Integer> keys = new ArrayList<Integer>(top.keySet());
for(int i=keys.size()-1; i>=0; i--){
context.write(new IntWritable(keys.get(i)), new Text(top1.get(top.get(keys.get(i)))));
}
}
@Override
protected void setup(Context context)
throws IOException, InterruptedException {
this.N = context.getConfiguration().getInt("N", N); // default is top 10
}
}