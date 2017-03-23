package Cloudwick;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class NaturalKeyGroupingComparator extends WritableComparator {
    protected NaturalKeyGroupingComparator() {
        super(CustomText.class, true);
    }   
    @SuppressWarnings("rawtypes")
    public int compare(WritableComparable w1, WritableComparable w2) {
        CustomText k1 = (CustomText)w1;
        CustomText k2 = (CustomText)w2;
         
        return k1.getFirst().compareTo(k2.getFirst());
    }
}
