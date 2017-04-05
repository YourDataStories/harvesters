package org.linkedeconomy.nsrfapi.dto;

import java.math.BigDecimal;

/**
 *
 * @author G. Vafeiadis
 */
public class SubprojectsProjects {
    
    /**
     *
     * Data Transfer Object for Public Works and Subprojects 
     * to transfer the data between classes and modules of application
     *
     */

    private Integer subprojectId;
    private String ops;
    private String title;
    private Integer constructorId;
    private String constructorName;
    private String start;
    private String finish;
    private BigDecimal budget;

    public Integer getSubprojectId() {
        return subprojectId;
    }
    
    public String getOps() {
        return ops;
    }

    public String getTitle() {
        return title;
    }

    public Integer getConstructorId() {
        return constructorId;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public String getStart() {
        return start;
    }

    public String getFinish() {
        return finish;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setSubprojectId(Integer subprojectId) {
        this.subprojectId = subprojectId;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setConstructorId(Integer constructorId) {
        this.constructorId = constructorId;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName = constructorName;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

}
