package Client;

import Order.order;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class client {
    private String username;
    private String password;
    private String email;
    private LocalDate birth_date;
    private int orderCount;
    private List<order> orders;

    public client() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.birth_date = null;
        this.orderCount = 0;
        this.orders = new ArrayList<order>();
    }

    public client(String username, String password, String email, LocalDate birth_date,int orderCount, List<order> orders) {
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

    public List<order> getOrderList() {
        return new ArrayList<>(orders);
    }

    public int getOrderCount(){
        return this.orderCount;
    }

    public void setOrderList(List<order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }
        this.orderCount++;
        this.orders = new ArrayList<>(orders);
    }
    
    public static client deserialize(DataInputStream in) {
        try {
            String username = in.readUTF();
            String password = in.readUTF();
            String email = in.readUTF();
            String birthDateString = in.readUTF();
            LocalDate birth_date = LocalDate.parse(birthDateString);
    
            int ordersSize = in.readInt();
            List<order> orders = new ArrayList<>();
            for (int i = 0; i < ordersSize; i++) {
                order o = order.deserialize(in);
                orders.add(o);
            }
    
            return new client(username,password,email, birth_date, ordersSize, orders);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.getUsername());
        out.writeUTF(this.getPassword());
        out.writeUTF(this.getEmail());
        out.writeUTF(this.getBirth_date().toString());
    
        List<order> orders = this.getOrderList();
        out.writeInt(orders.size());
        for (order o : orders) {
            o.serialize(out);
        }
    }


}
