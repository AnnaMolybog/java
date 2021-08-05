package homework;

import java.util.Comparator;

public class ScoresComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        return Long.compare(o1.getScores(), o2.getScores());
    }
}
