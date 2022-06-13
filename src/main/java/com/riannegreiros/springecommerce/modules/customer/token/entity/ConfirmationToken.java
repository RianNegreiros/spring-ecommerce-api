package com.riannegreiros.springecommerce.modules.customer.token.entity;

import com.riannegreiros.springecommerce.modules.customer.entity.Customer;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date createTime;

    @Column(nullable = false)
    private Date expiresTime;

    private Date confirmedTime;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public ConfirmationToken(String token, Date createTime, Date expiresTime, Customer customer) {
        this.token = token;
        this.createTime = createTime;
        this.expiresTime = expiresTime;
        this.customer = customer;
    }

    public ConfirmationToken() {

    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(Date confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
