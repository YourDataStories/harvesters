/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.dao;

import java.util.List;
import org.linkedeconomy.espa.jpa.Buyers;

/**
 *
 * @author G. Vafeiadis
 */
public interface BuyersDao {
    
    List<Buyers> getBuyers();
    List<Object> getProjectBuyers();
    
}
