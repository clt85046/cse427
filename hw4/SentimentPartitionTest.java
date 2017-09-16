package stubs;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.junit.Test;

public class SentimentPartitionTest {

	SentimentPartitioner<Text, IntWritable> mpart;

	@Test
	public void testSentimentPartition() {

		mpart=new SentimentPartitioner<Text, IntWritable>();
		mpart.setConf(new Configuration());
		int result;		
		result=mpart.getPartition(new Text("love"),new IntWritable(1), 3);
		Assert.assertEquals(0,result );
		System.out.println("succeed!");
		result=mpart.getPartition(new Text("deadly"), new IntWritable(1), 3);
		Assert.assertEquals(1,result );
		System.out.println("succeed!");
		result=mpart.getPartition(new Text("zodiac"), new IntWritable(1), 3);
		Assert.assertEquals(2,result );
		System.out.println("succeed!");
		/*
		 * Test the words "love", "deadly", and "zodiac". 
		 * The expected outcomes should be 0, 1, and 2. 
		 */
        
 		/*
		 * TODO implement
		 */          
		
	}

}
