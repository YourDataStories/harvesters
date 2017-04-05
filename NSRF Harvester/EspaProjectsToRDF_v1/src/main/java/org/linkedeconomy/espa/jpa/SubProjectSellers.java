/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.jpa;

/**
 *
 * @author G. Vafeiadis
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Projects_Sellers")
@SuppressWarnings("PersistenceUnitPresent")
public class SubProjectSellers {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "subProjectID", nullable = false)
    private int subProjectId;

    @Column(name = "constructorID", nullable = false)
    private int sellerId;

    @Column(name = "ops", nullable = false)
    private String ops;

    public int getId() {
        return id;
    }

    public int getSubProjectId() {
        return subProjectId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getOps() {
        return ops;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

}
