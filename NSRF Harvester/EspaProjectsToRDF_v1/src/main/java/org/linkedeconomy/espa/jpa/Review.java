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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Overview")
@SuppressWarnings({"PersistenceUnitPresent", "ValidAttributes"})
public class Review {

    @Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "ops", nullable = false)
    private String ops;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "beneficiary_Institution", nullable = false)
    private String foreas;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "public_Expenditure_Budget", nullable = false)
    private Double budget;

    @Column(name = "payments", nullable = false)
    private Double spending;

    @Column(name = "completition_percent", nullable = false)
    private String completion;

    @Column(name = "start", nullable = false)
    private String startDate;

    @Column(name = "finish", nullable = false)
    private String finishDate;

    @Column(name = "sub_projects", nullable = false)
    private String subProjects;

    @Column(name = "title_ops", nullable = false)
    private String opsTitle;

    @Column(name = "map_coordinates", nullable = false)
    private String coordinates;

    public int getId() {
        return id;
    }

    public String getOps() {
        return ops;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getForeas() {
        return foreas;
    }

    public String getDescription() {
        return description;
    }

    public String getOpsTitle() {
        return opsTitle;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setForeas(String foreas) {
        this.foreas = foreas;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBudget() {
        return budget;
    }

    public Double getSpending() {
        return spending;
    }

    public String getCompletion() {
        return completion;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getSubProjects() {
        return subProjects;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public void setSpending(Double spending) {
        this.spending = spending;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setSubProjects(String subProjects) {
        this.subProjects = subProjects;
    }

    public void setOpsTitle(String opsTitle) {
        this.opsTitle = opsTitle;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}
