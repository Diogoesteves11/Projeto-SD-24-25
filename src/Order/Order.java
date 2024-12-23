package Order;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int order_id = 0; 
    private final int currentOrderId; 
    private List<Product> products;

    public Order() {
        order_id++;
        this.currentOrderId = order_id;
        this.products = new ArrayList<>();
    }

    public Order(int order_id, List<Product> products){
        this.currentOrderId = order_id;
        this.products = new ArrayList<>(products);
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product geProduct(int id){
        return this.products.get(id);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.getCurrentOrderId());
        out.writeInt(this.products.size());
    
        for (Product p : this.products) {
            out.writeInt(p.getCurrentProductId());
            p.serialize(out);
        }
    }

    public static Order deserialize(DataInputStream in) throws IOException {
        int orderId = in.readInt();
        int productCount = in.readInt();
        
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productCount; i++) {
            Product p = Product.deserialize(in); 
            products.add(p);
        }
        
        return new Order(orderId, products);
    }
}
