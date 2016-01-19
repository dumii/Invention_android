package fr.afcepf.al25.invention_v1;

import java.util.Date;
import java.util.List;

/**
 * Created by adumi on 18/01/2016.
 */
public class CommandeDTO {

    private int idOrder;
    private Date creationDate;
    private Date closureDate;
    private double totalOrder;
    private String addressLivraison;
    private String stateOrder;
    private List<OrderLineDTO> orderLinesDto;

    public CommandeDTO() {
    }

    public int getIdOrder() {
        return idOrder;
    }
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getClosureDate() {
        return closureDate;
    }
    public void setClosureDate(Date closureDate) {
        this.closureDate = closureDate;
    }
    public double getTotalOrder() {
        return totalOrder;
    }
    public void setTotalOrder(double totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getAddressLivraison() {
        return addressLivraison;
    }

    public void setAddressLivraison(String addressLivraison) {
        this.addressLivraison = addressLivraison;
    }

    public String getStateOrder() {
        return stateOrder;
    }
    public void setStateOrder(String stateOrder) {
        this.stateOrder = stateOrder;
    }

    public List<OrderLineDTO> getOrderLinesDto() {
        return orderLinesDto;
    }

    public void setOrderLinesDto(List<OrderLineDTO> orderLinesDto) {
        this.orderLinesDto = orderLinesDto;
    }

}
