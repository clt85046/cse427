package stubs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class TopNMovie extends Configured implements Tool {

public static void main(String[] args) throws Exception {
// TODO Auto-generated method stub
int exitCode = ToolRunner.run(new TopNMovie(), args);
System.exit(exitCode);
}
public int run(String[] args) throws Exception {
	Configuration conf1 = new Configuration();
Job tnJob = Job.getInstance(conf1, "Top N List");

tnJob.setJarByClass(getClass());

tnJob.setMapperClass(AggregateMapper.class);
tnJob.setReducerClass(AggregateReducer.class);

tnJob.setOutputKeyClass(Text.class);
tnJob.setOutputValueClass(DoubleWritable.class);
Path outputPath= new Path("Firstoutput");
FileInputFormat.addInputPath(tnJob, new Path(args[0]));
FileOutputFormat.setOutputPath(tnJob, outputPath);
//outputPath.getFileSystem(conf1).delete(outputPath);
tnJob.waitForCompletion(true);
Configuration conf2= new Configuration();
Job job = new Job(getConf());
//Job job = Job.getInstance(conf2);
job.setJarByClass(TopNMovie.class);
job.setJobName("Top N");
int N = Integer.parseInt(args[1]); // top N
job.getConfiguration().setInt("N", N);

job.setMapperClass(TopNMapper.class);
job.setReducerClass(TopNReducer.class);
job.setNumReduceTasks(1);
//map()'s output (K,V)
job.setMapOutputKeyClass(NullWritable.class);
job.setMapOutputValueClass(Text.class);
//reduce()'s output (K,V)
job.setOutputKeyClass(IntWritable.class);
job.setOutputValueClass(Text.class);
//args[1] = input directory
//args[2] = output directory
FileInputFormat.setInputPaths(job, outputPath);
FileOutputFormat.setOutputPath(job, new Path(args[2]));


boolean status = job.waitForCompletion(true);
return status ? 0 : 1;
}
}