package Cloudwick;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class CustomText implements Writable, WritableComparable<CustomText>{
	
	private Text first;
	private Text second;
	
	public CustomText(){
	set(new Text(),new Text());	
	}
	
	public CustomText(Text first, Text second)
	{
		set(first,second);
	}
	
	public CustomText(String first, String second)
	{
		set(new Text(first),new Text(second));
	}
	
	public void set(Text first, Text second){
		this.first = first;
		this.second = second;
	}
	public Text getFirst() {
		return first;
	}
	public void setFirst(Text first) {
		this.first = first;
	}
	public Text getSecond() {
		return second;
	}
	public void setSecond(Text second) {
		this.second = second;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		first.readFields(in);
		second.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		first.write(out);
		second.write(out);
	}

	@Override
	public int compareTo(CustomText ct){ 
		// TODO Auto-generated method stub
		int cmp = first.compareTo(ct.first);
		if (cmp != 0) {
		return cmp;
		}
		return second.compareTo(ct.second);
	}

	@Override
	public String toString() {
		return "CustomText [first=" + first + ", second=" + second + "]";
	}
	
	
	

}
