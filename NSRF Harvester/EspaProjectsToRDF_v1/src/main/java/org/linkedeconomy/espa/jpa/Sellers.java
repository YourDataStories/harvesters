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
@Table(name = "Sellers")
@SuppressWarnings("PersistenceUnitPresent")
public class Sellers {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "title", nullable = false)
    private String eponimia;

    @Column(name = "address", nullable = false)
    private String diefthinsh;

    public int getId() {
        return id;
    }

    public String getEponimia() {
        return eponimia;
    }

    public String getDiefthinsh() {
        return diefthinsh;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEponimia(String eponimia) {
        this.eponimia = eponimia;
    }

    public void setDiefthinsh(String diefthinsh) {
        this.diefthinsh = diefthinsh;
    }

}
