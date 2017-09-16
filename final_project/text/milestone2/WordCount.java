package stubs;

import java.io.File;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/* 
 * MapReduce jobs are typically implemented by using a driver class.
 * The purpose of a driver class is to set up the configuration for the
 * MapReduce job and to run the job.
 * Typical requirements for a driver class include configuring the input
 * and output data formats, configuring the map and reduce classes,
 * and specifying intermediate data formats.
 * 
 * The following is the code for the driver class:
 */
public class WordCount extends Configured implements Tool {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int exitCode = ToolRunner.run(new WordCount(), args);
		System.exit(exitCode);
		}
	
	public int run(String[] args) throws Exception {

    /*
     * Instantiate a Job object for your job's configuration.  
     */
		Job job = new Job(getConf());
		//Job job = Job.getInstance(conf2);
		
    
    /*
     * Specify the jar file that contains your driver, mapper, and reducer.
     * Hadoop will transfer this jar file to nodes in your cluster running
     * mapper and reducer tasks.
     */
    job.setJarByClass(WordCount.class);
    
    /*
     * Specify an easily-decipherable name for the job.
     * This job name will appear in reports and logs.
     */
    job.setJobName("Word Count");

    /*
     * Specify the paths to the input and output data based on the
     * command-line arguments.
     */
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    /*
     * Specify the mapper and reducer classes.
     */
    job.setMapperClass(WordMapper.class);
    job.setReducerClass(SumReducer.class);

    /*
     * For the word count application, the input file and output 
     * files are in text format - the default format.
     * 
     * In text format files, each record is a line delineated by a 
     * by a line terminator.
     * 
     * When you use other input formats, you must call the 
     * SetInputFormatClass method. When you use other 
     * output formats, you must call the setOutputFormatClass method.
     */
      
    /*
     * For the word count application, the mapper's output keys and
     * values have the same data types as the reducer's output keys 
     * and values: Text and IntWritable.
     * 
     * When they are not the same data types, you must call the 
     * setMapOutputKeyClass and setMapOutputValueClass 
     * methods.
     */

    /*
     * Specify the job's output key and value classes.
     */
    job.setMapOutputKeyClass(Text.class);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Text.class);
//job.setNumReduceTasks(0);
    boolean status = job.waitForCompletion(true);
	return status ? 0 : 1;
	}
}
