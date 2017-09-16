package stubs;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TestProcessLogs {

  
  MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
  ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
  MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

  @Before
  public void setUp() {

    /*
     * Set up the mapper test harness.
     */
    LogFileMapper mapper = new LogFileMapper();
    mapDriver = new MapDriver<LongWritable, Text, Text, IntWritable>();
    mapDriver.setMapper(mapper);

    /*
     * Set up the reducer test harness.
     */
    SumReducer reducer = new SumReducer();
    reduceDriver = new ReduceDriver<Text, IntWritable, Text, IntWritable>();
    reduceDriver.setReducer(reducer);

    /*
     * Set up the mapper/reducer test harness.
     */
    mapReduceDriver = new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>();
    mapReduceDriver.setMapper(mapper);
    mapReduceDriver.setReducer(reducer);
  }

  /*
   * Test the mapper.
   */
  @Test
  public void testMapper() {
	  mapDriver.withInput(new LongWritable(1), new Text("96.7.4.14 - - [24/Apr/2011:04:20:11 -0400] \"GET /cat.jpg HTTP/1.1\" 200 12433"));
	  mapDriver.withOutput(new Text("96.7.4.14"), new IntWritable(1));
	  mapDriver.runTest();
  }

  /*
   * Test the reducer.
   */
  @Test
  public void testReducer() {
	  List<IntWritable> values = new ArrayList<IntWritable>();
	  values.add(new IntWritable(1));
	  values.add(new IntWritable(1));
	  reduceDriver.withInput(new Text("96.7.4.14"), values);
	  reduceDriver.withOutput(new Text("96.7.4.14"), new IntWritable(2));
	  reduceDriver.runTest();
  }


  /*
   * Test the mapper and reducer working together.
   */
  @Test
  public void testMapReduce() {
	  mapReduceDriver.withInput(new LongWritable(1), new Text("96.7.4.14 - - [24/Apr/2011:04:20:11 -0400] \"GET /cat.jpg HTTP/1.1\" 200 12433"));
	  mapReduceDriver.withOutput(new Text("96.7.4.14"), new IntWritable(1));
	  mapReduceDriver.runTest();
  }
}