/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.linkedeconomy.espa.dao.BuyersDao;
import org.linkedeconomy.espa.dto.Buyer;
import org.linkedeconomy.espa.jpa.Buyers;
import org.linkedeconomy.espa.service.BuyersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class BuyersServiceImpl implements BuyersService{

    @Autowired
    private BuyersDao buyersDao;
    @Override
    public List<Buyers> getBuyers() {
        return buyersDao.getBuyers();
    }
    
    @Override
    public List<Buyer> getProjectBuyers() {
        List<Object> buyers = buyersDao.getProjectBuyers();
        List<Buyer> projectBuyers = new ArrayList<Buyer>();
            for (Object prodBuyers1 : buyers) {
                Object[] o = (Object[]) prodBuyers1;
                ArrayList<Object> buyer = new ArrayList<Object>(Arrays.asList(o));
                Buyer v = new Buyer();
                v.setOps((String) buyer.get(0));
                v.setBuyerId((Integer) buyer.get(1));
                v.setEponimia((String) buyer.get(2));
                v.setDieuthunsh((String) buyer.get(3));
                projectBuyers.add(v);
            }

        return projectBuyers;
    }
    
}
    

