package homework;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private Map<Customer, String> customersData = new TreeMap<>(new ScoresComparator());

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestScoreCustomer = ((TreeMap<Customer, String>) customersData).firstEntry();
        return Map.entry(smallestScoreCustomer.getKey().clone(), smallestScoreCustomer.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextCustomer = ((TreeMap<Customer, String>) customersData).higherEntry(customer);
        return nextCustomer != null ? Map.entry(nextCustomer.getKey().clone(), nextCustomer.getValue()) : null;
    }

    public void add(Customer customer, String data) {
        customersData.put(customer, data);
    }
}
