package org.linkedeconomy.nsrfapi.dto;

/**
 *
 * @author G. Vafeiadis
 */
public class ProjectBeneficiaries {
    
    /**
     *
     * Data Transfer Object for Public Works and Beneficiaries 
     * to transfer the data between classes and modules of application
     *
     */

    private Integer beneficiaryId;
    private String ops;
    private String name;
    private String vatId;
    private Integer subprojectId;

    public String getOps() {
        return ops;
    }

    public Integer getBeneficiaryId() {
        return beneficiaryId;
    }

    public String getName() {
        return name;
    }

    public String getVatId() {
        return vatId;
    }

    public Integer getSubprojectId() {
        return subprojectId;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public void setBeneficiaryId(Integer beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public void setSubprojectId(Integer subprojectId) {
        this.subprojectId = subprojectId;
    }

}
