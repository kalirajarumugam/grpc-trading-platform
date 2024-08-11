package org.example.entity;

import jakarta.persistence.*;
import org.example.common.Ticker;

@Entity
@Table(name ="portfolio_item")
public class PortfolioItem {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "customer_id")
    private Integer userId;
    private org.example.common.Ticker ticker;
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
