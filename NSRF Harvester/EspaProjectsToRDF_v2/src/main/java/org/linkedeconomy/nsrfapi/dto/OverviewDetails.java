package org.linkedeconomy.nsrfapi.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author G. Vafeiadis
 */
public class OverviewDetails {
    
    private Integer projectId;
    private String ops;
    private String title;
    private Date publishDate;
    private BigDecimal budget;
    private BigDecimal contracts;
    private BigDecimal payments;
    private String epKodikos;
    private String perifereia;
    private Integer trexousaKatastash;
    private String titleEnglish;
    private String description;
    private Date startDate;
    private Date endDate;
    private String completion;
    private Integer countSubprojects;

    public Integer getProjectId() {
        return projectId;
    }

    public String getOps() {
        return ops;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public BigDecimal getContracts() {
        return contracts;
    }

    public BigDecimal getPayments() {
        return payments;
    }
    
    public String getEpKodikos() {
        return epKodikos;
    }

    public String getPerifereia() {
        return perifereia;
    }

    public Integer getTrexousaKatastash() {
        return trexousaKatastash;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCompletion() {
        return completion;
    }

    public Integer getCountSubprojects() {
        return countSubprojects;
    }
    
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public void setContracts(BigDecimal contracts) {
        this.contracts = contracts;
    }

    public void setPayments(BigDecimal payments) {
        this.payments = payments;
    }
    
    public void setEpKodikos(String epKodikos) {
        this.epKodikos = epKodikos;
    }

    public void setPerifereia(String perifereia) {
        this.perifereia = perifereia;
    }

    public void setTrexousaKatastash(Integer trexousaKatastash) {
        this.trexousaKatastash = trexousaKatastash;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }    

    public void setCompletion(String completion) {
        this.completion = completion;
    }
    
    public void setCountSubprojects(Integer countSubprojects) {
        this.countSubprojects = countSubprojects;
    }
}
