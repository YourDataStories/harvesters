package org.linkedeconomy.nsrfapi.jpa;

/**
 *
 * @author G. Vafeiadis
 */
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Overview")
@SuppressWarnings("PersistenceUnitPresent")
public class Overview implements Serializable {
    
    /**
     *
     * object/relational mapping facility for managing relational data. 
     * Managing "Overview" Table.
     *
     */

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "kodikos", nullable = false)
    private String kodikos;

    @Column(name = "publishDate", nullable = false)
    private Date publishDate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "contracts", nullable = false)
    private Double contract;

    @Column(name = "payments", nullable = false)
    private Double payments;

    @Column(name = "countIpoergon", nullable = false)
    private Integer countIpoergon;
    
    @Column(name = "epKodikos", nullable = false)
    private String epKodikos;
    
    @Column(name = "perifereia", nullable = false)
    private String perifereia;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "trexousaKatastash", nullable = false)
    private String trexousaKatastash;

    public int getId() {
        return id;
    }

    public String getKodikos() {
        return kodikos;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getTitle() {
        return title;
    }

    public Double getBudget() {
        return budget;
    }

    public Double getContract() {
        return contract;
    }

    public Double getPayments() {
        return payments;
    }

    public Integer getCountIpoergon() {
        return countIpoergon;
    }

    public String getEpKodikos() {
        return epKodikos;
    }

    public String getPerifereia() {
        return perifereia;
    }

    public String getType() {
        return type;
    }

    public String getTrexousaKatastash() {
        return trexousaKatastash;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setKodikos(String kodikos) {
        this.kodikos = kodikos;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public void setContract(Double contract) {
        this.contract = contract;
    }

    public void setPayments(Double payments) {
        this.payments = payments;
    }

    public void setCountIpoergon(Integer countIpoergon) {
        this.countIpoergon = countIpoergon;
    }

    public void setEpKodikos(String epKodikos) {
        this.epKodikos = epKodikos;
    }

    public void setPerifereia(String perifereia) {
        this.perifereia = perifereia;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setTrexousaKatastash(String trexousaKatastash) {
        this.trexousaKatastash = trexousaKatastash;
    }

}
