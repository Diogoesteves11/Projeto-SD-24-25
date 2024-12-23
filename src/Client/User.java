package Client;

import java.util.List;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import Order.Order;

public class User extends Client {
    private int orderCount;
    private List<Order> orders;

    public User() {
        super();
        this.orderCount = 0;
        this.orders = new ArrayList<>();
    }

    public User(String username, String password, String email, LocalDate birth_date, int orderCount, List<Order> orders) {
        super(username, password, email, birth_date);
        this.orderCount = orderCount;
        this.orders = new ArrayList<>(orders);
    }

    public User(User u) {
        super(u.getUsername(), u.getPassword(), u.getEmail(), u.getBirth_date());
        this.orderCount = u.getOrderCount();
        this.orders = new ArrayList<>(u.getOrderList());
    }

    public List<Order> getOrderList() {
        return new ArrayList<>(orders);
    }

    public int getOrderCount() {
        return this.orderCount;
    }

    public void setOrderList(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }
        this.orders = new ArrayList<>(orders);
        this.orderCount = orders.size();
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.orders.add(order);
        this.orderCount++;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out); // Serializa os campos do `Client`
        out.writeInt(this.orderCount);
        for (Order order : this.orders) {
            order.serialize(out);
        }
    }

    public static User deserialize(DataInputStream in) {
        try {
            Client client = Client.deserialize(in);
            int orderCount = in.readInt();
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < orderCount; i++) {
                orders.add(Order.deserialize(in));
            }
            return new User(client.getUsername(), client.getPassword(), client.getEmail(), client.getBirth_date(), orderCount, orders);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


