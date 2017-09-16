package stubs;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
* Mapper's input are read from SequenceFile and records are: (K, V)
* where
* K is a Text
* V is an Integer
*
* @author Mahmoud Parsian
*
*/
public class TopNMapper extends Mapper<Object, Text, NullWritable, Text> {
private int N; // default
private SortedMap<Double, String> top = new TreeMap<Double, String>();
@Override
public void map(Object key, Text value, Context context)
throws IOException, InterruptedException {
String keyAsString = value.toString();
String[] tokens = keyAsString.split("\t");
String nani = tokens[0];
double frequency = Double.parseDouble(tokens[1]);
String compositeValue = nani + "," + frequency;
top.put(frequency, compositeValue);
// keep only top N
if (top.size() > this.N) {
top.remove(top.firstKey());
}
}
@Override
protected void setup(Context context) throws IOException,
InterruptedException {
this.N = context.getConfiguration().getInt("N",N); // default is top 10
}
@Override
protected void cleanup(Context context) throws IOException,
InterruptedException {
for (String str : top.values()) {
context.write(NullWritable.get(), new Text(str));
}
}
}