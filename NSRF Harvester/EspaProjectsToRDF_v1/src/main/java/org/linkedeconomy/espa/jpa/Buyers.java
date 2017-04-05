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
@Table(name = "Proposal_Body")
@SuppressWarnings("PersistenceUnitPresent")
public class Buyers {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "title", nullable = false)
    private String eponimia;

    @Column(name = "representative", nullable = false)
    private String ekprosopos;

    @Column(name = "address", nullable = false)
    private String diefthinsh;

    @Column(name = "email", nullable = false)
    private String email;

    public int getId() {
        return id;
    }

    public String getEponimia() {
        return eponimia;
    }

    public String getEkprosopos() {
        return ekprosopos;
    }

    public String getDiefthinsh() {
        return diefthinsh;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEponimia(String eponimia) {
        this.eponimia = eponimia;
    }

    public void setEkprosopos(String ekprosopos) {
        this.ekprosopos = ekprosopos;
    }

    public void setDiefthinsh(String diefthinsh) {
        this.diefthinsh = diefthinsh;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
