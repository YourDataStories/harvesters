/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl;

import java.util.List;
import org.linkedeconomy.espa.dao.SellersDao;
import org.linkedeconomy.espa.jpa.Sellers;
import org.linkedeconomy.espa.service.SellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class SellersServiceImpl implements SellersService {

    @Autowired
    private SellersDao sellersDao;
    public List<Sellers> getSellers() {
        return sellersDao.getSellers();
    }
    
}
