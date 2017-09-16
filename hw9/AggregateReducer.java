package stubs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class AggregateReducer extends Reducer<Text,DoubleWritable, Text ,DoubleWritable > {





protected void reduce(Text key, Iterable<DoubleWritable> values,
Context context)
throws IOException, InterruptedException {
	double sum = 0;

// TODO Auto-generated method stub
for(DoubleWritable rate :values)
{
sum += rate.get();
}
context.write(key, new DoubleWritable(sum));
}
}