package Order;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class product {
    private static int product_id;
    private final int current_product_id;
    private String description;

    public product() {
        product_id++;
        this.current_product_id = product_id;
        this.description = "";
    }

    public product(String description) {
        product_id++;
        this.current_product_id = product_id;
        this.description = description;
    }

    public product(int id, String description){
        this.current_product_id = id;
        this.description = description;
    }

    public int getCurrentProductId() {
        return current_product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product ID: " + current_product_id + ", Description: " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        product product = (product) obj;
        return current_product_id == product.current_product_id;
    }

    public void serialize(DataOutputStream out) throws IOException{
        out.writeInt(this.getCurrentProductId());
        out.writeUTF(this.getDescription());
    }

    public static product deserialize(DataInputStream in) throws IOException{
        int id = in.readInt();
        String description = in.readUTF();

        return new product(id,description);
    }
}
