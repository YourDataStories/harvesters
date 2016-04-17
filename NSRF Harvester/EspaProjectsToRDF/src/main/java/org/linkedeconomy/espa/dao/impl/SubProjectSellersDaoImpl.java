/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.espa.dao.SubProjectSellersDao;
import org.linkedeconomy.espa.jpa.SubProjectSellers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class SubProjectSellersDaoImpl implements SubProjectSellersDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SubProjectSellers> getSubProjectSellers() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from SubProjectSellers");
        return query.list();
    }

}
