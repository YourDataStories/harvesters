package org.linkedeconomy.nsrfapi.dto;

/**
 *
 * @author G. Vafeiadis
 */
public class ProjectSellers {
    
    /**
     *
     * Data Transfer Object for Subprojects and Sellers
     * to transfer the data between classes and modules of application
     *
     */

    private Integer subprojectId;
    private String code;
    private Integer sellerId;
    private String sellerName;
    private String vatId;

    public Integer getSubprojectId() {
        return subprojectId;
    }

    public String getCode() {
        return code;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getVatId() {
        return vatId;
    }
    
    public void setSubprojectId(Integer subprojectId) {
        this.subprojectId = subprojectId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }
    
}
