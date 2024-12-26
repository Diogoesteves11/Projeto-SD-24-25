package connectionProtocol;

import java.util.List;

public class ProductSender extends Package{
    private List<String> produtos;
    ProductSender(String clientKey, List<String> produtos)
    {
        super(clientKey, 4);
        this.produtos = produtos;
    }
}
