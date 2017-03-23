package Cloudwick;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class NaturalKeyPartitioner extends Partitioner<CustomText, DoubleWritable> {
	 
    @Override
    public int getPartition(CustomText key, DoubleWritable val, int numPartitions) {
        int hash = key.getFirst().hashCode();
        int partition = hash % numPartitions;
        return partition;
    }
 
}
