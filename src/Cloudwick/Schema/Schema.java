package Cloudwick;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.util.Tool;

public class Schema {

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {
		private Text word = new Text();
		static int count = 0;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			
			String[] line_token = value.toString().split(",");
		//	System.out.println(line_token.length+" "+line_token[0]+"---- "+line_token[1]+"----"+line_token[2]);
		//	System.out.println(line_token[2]+" "+(count++));
			word.set(line_token[2]);
			String s = line_token[0] + "," + line_token[1];
			context.write(word, new Text(s));
				
		}
	}

	public static class TokenizerMapper2 extends
			Mapper<Object, Text, Text, Text> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		static int count = 0;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			
			String[] line_token = value.toString().split(",");

			word.set(line_token[0]);
			String s = line_token[1];
			context.write(word, new Text(s));
		}		
	}

	public static class IntSumReducer extends
			Reducer<Text, Text, Text, Text> {
		private Text result = new Text();

		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			//System.out.println(key);
			String word = "";
			for (Text val : values) {
				if(val.toString().contains(",")){
					word = val.toString()+","+word;
				}
				else
					word = word + val.toString();
			}
			System.out.println(word);
			result.set(word);
			context.write(new Text(),result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Schema");
		job.setJarByClass(Schema.class);
		job.setMapperClass(TokenizerMapper.class);
		// job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		MultipleInputs.addInputPath(job, new Path(args[0]),
				TextInputFormat.class, TokenizerMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),
				TextInputFormat.class, TokenizerMapper2.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}