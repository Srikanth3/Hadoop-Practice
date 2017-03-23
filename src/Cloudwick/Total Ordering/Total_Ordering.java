package Cloudwick;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

public class Total_Ordering {

	public static class Order_map extends
			Mapper<Object, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		static int count = 0;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			// System.out.println(count++);
			System.out.println(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);

			}
		}
	}

	public static class Order_reducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
		//	System.out.println(key);
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			System.out.println(key.toString() + " " + sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {

		Job job = Job.getInstance(new Configuration(),
				"Total Order Sorting example");
		job.setJarByClass(Total_Ordering.class);

		Path inputPath = new Path(args[0]);
		Path partitionOutputPath = new Path(args[1]);
		Path outputPath = new Path(args[2]);

		// The following instructions should be executed before writing the
		// partition file
	//	job.setNumReduceTasks(1);
		FileInputFormat.setInputPaths(job, inputPath);
		TotalOrderPartitioner.setPartitionFile(job.getConfiguration(),
				partitionOutputPath);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		InputSampler.Sampler<Text, Text> sampler = new InputSampler.RandomSampler<>(
				0.01, 1000, 10);
		InputSampler.writePartitionFile(job, sampler);

		// Use TotalOrderPartitioner and default identity mapper and reducer
		job.setPartitionerClass(TotalOrderPartitioner.class);
		job.setMapperClass(Order_map.class);
		job.setReducerClass(Order_reducer.class);

		FileOutputFormat.setOutputPath(job, outputPath);
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
