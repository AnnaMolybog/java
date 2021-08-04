package homework;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class CustomerReverseOrder {

    private Queue<Customer> customers = Collections.asLifoQueue(new LinkedList<>());

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        return customers.poll();
    }
}
