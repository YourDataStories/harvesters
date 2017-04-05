/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service;

import java.util.List;
import org.linkedeconomy.espa.dto.Buyer;
import org.linkedeconomy.espa.jpa.Buyers;

/**
 *
 * @author G. Vafeiadis
 */
public interface BuyersService {
    
    List<Buyers> getBuyers();
    List<Buyer> getProjectBuyers();
    
}
