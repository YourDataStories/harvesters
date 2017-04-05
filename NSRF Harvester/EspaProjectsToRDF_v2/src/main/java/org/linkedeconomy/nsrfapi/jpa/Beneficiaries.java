package org.linkedeconomy.nsrfapi.jpa;

/**
 *
 * @author G. Vafeiadis
 */
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Protash")
@SuppressWarnings("PersistenceUnitPresent")
public class Beneficiaries implements Serializable {
    
    /**
     *
     * object/relational mapping facility for managing relational data. 
     * Managing "Protash" Table.
     *
     */

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "vatId", nullable = false)
    private String vatId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getVatId() {
        return vatId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

}
