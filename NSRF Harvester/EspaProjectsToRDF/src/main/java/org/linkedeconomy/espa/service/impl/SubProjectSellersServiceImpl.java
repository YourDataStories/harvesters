/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl;

import java.util.List;
import org.linkedeconomy.espa.dao.SubProjectSellersDao;
import org.linkedeconomy.espa.jpa.SubProjectSellers;
import org.linkedeconomy.espa.service.SubProjectSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class SubProjectSellersServiceImpl implements SubProjectSellersService {

    @Autowired
    private SubProjectSellersDao subProjectSellersDao;
    public List<SubProjectSellers> getSubProjectSellers() {
        return subProjectSellersDao.getSubProjectSellers();
    }
    
}
