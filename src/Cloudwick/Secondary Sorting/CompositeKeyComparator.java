package Cloudwick;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CompositeKeyComparator extends WritableComparator {
    protected CompositeKeyComparator() {
        super(CustomText.class, true);
    }   
    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        CustomText k1 = (CustomText)w1;
        CustomText k2 = (CustomText)w2;
         
        int result = k1.getFirst().compareTo(k2.getFirst());
        if(0 == result) {
            result =  k1.getSecond().compareTo(k2.getSecond());
        }
        return result;
    }
}
