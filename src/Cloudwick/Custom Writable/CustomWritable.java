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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CustomWritable {

	public static class custom_mapper extends Mapper<Object,Text,CustomText,IntWritable>{
		
		public void map(Object key,Text value, Context context) throws IOException, InterruptedException{
			System.out.println(value.toString());
			String[] s = value.toString().split(":");
			System.out.println(s[0]+" "+s[1]);
				context.write(new CustomText(s[0],s[1]), new IntWritable(1));
		
		}
	}
	
	public static class custom_reducer extends Reducer<CustomText,IntWritable,CustomText,IntWritable>{
		
		public void reduce(CustomText key, Iterable<IntWritable> value, Context context) throws IOException, InterruptedException{
			int sum =0;
			for(IntWritable val : value){
				sum+=val.get();
			}
			String keyvalue = key.toString();
			System.out.println("Hello"+ keyvalue);
			context.write(key, new IntWritable(sum));
		}
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		Job job = Job.getInstance(new Configuration(), "word_count");
		job.setJarByClass(CustomWritable.class);
		
		job.setMapperClass(custom_mapper.class);
		job.setReducerClass(custom_reducer.class);
		
		job.setMapOutputKeyClass(CustomText.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);


		

	}

}
