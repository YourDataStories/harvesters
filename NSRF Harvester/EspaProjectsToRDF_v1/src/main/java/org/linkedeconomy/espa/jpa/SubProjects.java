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
@Table(name = "Sub_Projects")
@SuppressWarnings("PersistenceUnitPresent")
public class SubProjects {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "ops", nullable = false)
    private String ops;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "start", nullable = false)
    private String start;

    @Column(name = "finish", nullable = false)
    private String finish;

    public int getId() {
        return id;
    }

    public String getOps() {
        return ops;
    }

    public String getTitle() {
        return title;
    }

    public Double getBudget() {
        return budget;
    }

    public String getStart() {
        return start;
    }

    public String getFinish() {
        return finish;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

}
