package connectionProtocol;

import Order.Order;

public class OrderPackage extends Package{
    private Order encomenda;

    OrderPackage(String clientKey, Order order){
        super(clientKey, 4);
        this.encomenda = order;
    }
}
