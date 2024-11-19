package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import order.Order;

public class Client {
    private String username;
    private String password;
    private String email;
    private LocalDate birth_date;
    private int orderCount;
    private List<Order> orders;

    public Client() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.birth_date = null;
        this.orderCount = 0;
        this.orders = new ArrayList<>();
    }

    public Client(String username, String password, String email, LocalDate birth_date,int orderCount, List<Order> orders) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birth_date = birth_date;
        this.orderCount = orderCount;
        this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public List<Order> getOrderList() {
        return new ArrayList<>(orders);
    }

    public int getOrderCount(){
        return this.orderCount;
    }

    public void setOrderList(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }
        this.orderCount++;
        this.orders = new ArrayList<>(orders);
    }
    
    public static Client deserialize(DataInputStream in) {
        try {
            String username = in.readUTF();
            String password = in.readUTF();
            String email = in.readUTF();
            String birthDateString = in.readUTF();
            LocalDate birth_date = LocalDate.parse(birthDateString);
    
            int ordersSize = in.readInt();
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < ordersSize; i++) {
                Order o = Order.deserialize(in);
                orders.add(o);
            }
    
            return new Client(username,password,email, birth_date, ordersSize, orders);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.getUsername());
        out.writeUTF(this.getPassword());
        out.writeUTF(this.getEmail());
        out.writeUTF(this.getBirth_date() != null ? this.getBirth_date().toString() : "");
    
        List<Order> orders = this.getOrderList();
        out.writeInt(orders.size());
        for (Order o : orders) {
            o.serialize(out);
        }
    }


}
