package fr.afcepf.al25.invention_v1;

/**
 * Created by adumi on 19/01/2016.
 */
public class OrderLineDTO {

    private int idProduct;
    private int idOrder;
    private boolean isExpediee;
    private int quantity;
    private double totalPrice;
    private ProductDTO productDto;

    public OrderLineDTO() {
    }

    public int getIdProduct() {
        return idProduct;
    }
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
    public int getIdOrder() {
        return idOrder;
    }
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public boolean isExpediee() {
        return isExpediee;
    }

    public void setExpediee(boolean isExpediee) {
        this.isExpediee = isExpediee;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public ProductDTO getProductDto() {
        return productDto;
    }
    public void setProductDto(ProductDTO productDto) {
        this.productDto = productDto;
    }
}
