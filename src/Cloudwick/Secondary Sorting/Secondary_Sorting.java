package Cloudwick;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Secondary_Sorting {

	public static class sort_mapper extends Mapper<Object,Text,CustomText,LongWritable>{
		
		public void map(Object key, Text value,Context context) throws IOException, InterruptedException{
			System.out.println(value.toString());
			String[] s = value.toString().split(",");
			System.out.println(s[0]+"--"+s[1]+"--"+s[2]);
			context.write(new CustomText(s[0],s[1]),new LongWritable(Integer.parseInt(s[2])));
					
		}
	}
	
	public static class sort_reducer extends Reducer<CustomText, LongWritable,CustomText,LongWritable>{
		public void reduce(CustomText key, Iterable<LongWritable> value,Context context) throws IOException, InterruptedException{
			
			
			int sum=0;
			
			for(LongWritable val : value)
			{
				sum+=val.get();
				System.out.println(key.toString()+"-- "+val+"--"+sum);

			}
			System.out.println(key.toString()+"--"+sum);
			context.write(key, new LongWritable(sum));
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
	{
		Job job = Job.getInstance(new Configuration(),"Secondary_sorting");
		job.setJarByClass(Secondary_Sorting.class);
		
		job.setMapperClass(sort_mapper.class);
		job.setReducerClass(sort_reducer.class);
		  
		job.setMapOutputKeyClass(CustomText.class);
		job.setMapOutputValueClass(LongWritable.class);
		
	//	job.setPartitionerClass(NaturalKeyPartitioner.class);
		job.setGroupingComparatorClass(NaturalKeyGroupingComparator.class);
	    job.setSortComparatorClass(CompositeKeyComparator.class);
	    
	    job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}
}
