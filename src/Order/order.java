package Order;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class order {
    private static int order_id = 0; 
    private final int currentOrderId; 
    private List<product> products;

    public order() {
        order_id++;
        this.currentOrderId = order_id;
        this.products = new ArrayList<>();
    }

    public order(int order_id, List<product> products){
        this.currentOrderId = order_id;
        this.products = new ArrayList<>(products);
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    public List<product> getProducts() {
        return products;
    }

    public product geProduct(int id){
        return this.products.get(id);
    }

    public void addProduct(product product) {
        products.add(product);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.getCurrentOrderId());
        out.writeInt(this.products.size());
    
        for (product p : this.products) {
            out.writeInt(p.getCurrentProductId());
            p.serialize(out);
        }
    }

    public static order deserialize(DataInputStream in) throws IOException {
        int orderId = in.readInt();
        int productCount = in.readInt();
        
        List<product> products = new ArrayList<>();
        for (int i = 0; i < productCount; i++) {
            int productId = in.readInt(); 
            product p = product.deserialize(in); 
            products.add(p);
        }
        
        return new order(orderId, products);
    }
}
