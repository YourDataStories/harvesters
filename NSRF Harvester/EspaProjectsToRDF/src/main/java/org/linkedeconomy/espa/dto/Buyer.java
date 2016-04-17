/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author G. Vafeiadis
 */
public class Buyer {

    private String ops;
    private Integer buyerId;
    private String eponimia;
    private String dieuthunsh;

    public String getOps() {
        return ops;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public String getEponimia() {
        return eponimia;
    }

    public String getDieuthunsh() {
        return dieuthunsh;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public void setEponimia(String eponimia) {
        this.eponimia = eponimia;
    }

    public void setDieuthunsh(String dieuthunsh) {
        this.dieuthunsh = dieuthunsh;
    }

}
