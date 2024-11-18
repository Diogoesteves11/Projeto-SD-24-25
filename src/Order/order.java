package Order;

import java.util.HashMap;
import java.util.Map;

public class order {
    private static int order_id = 0; 
    private final int currentOrderId; 
    private Map<Integer, product> products;

    public order() {
        order_id++;
        this.currentOrderId = order_id;
        this.products = new HashMap<>();
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    public Map<Integer, product> getProducts() {
        return products;
    }

    public product geProduct(int id){
        return this.products.get(id);
    }

    public void addProduct(int productId, product product) {
        products.put(productId, product);
    }
}
