package stubs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;


public class SequenceFileWriter  {

public static void main(String[] args) throws IOException {

if (args.length != 2) {
      System.out.printf("Usage: SequenceFileWriter <input dir> <output dir>\n");
      return;
}

Configuration conf = new Configuration();
//FileSystem fs = FileSystem.get(URI.create(args[1]), conf); 
Path path = new Path(args[1]);
Text key = new Text(); 
Text value = new Text(); 
SequenceFile.Writer writer = null;
BufferedReader br = new BufferedReader(new FileReader(args[0]));

try {
writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),Writer.keyClass(key.getClass()), Writer.valueClass(value.getClass()));
    String line = br.readLine();
    while (line != null) {
     // Match IP address from input file
     String IPADDRESS_PATTERN = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
Matcher matcher = pattern.matcher(line);
if (matcher.find()) {
key.set(matcher.group());
value.set(line);
// Write to sequence file
System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value); 
writer.append(key, value);
}
        line = br.readLine();
    }
} finally {
IOUtils.closeStream(writer);
    br.close();
}
} 
} 