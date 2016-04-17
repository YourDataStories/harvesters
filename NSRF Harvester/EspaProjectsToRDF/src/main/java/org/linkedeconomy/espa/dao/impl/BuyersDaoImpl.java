/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.espa.dao.BuyersDao;
import org.linkedeconomy.espa.jpa.Buyers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class BuyersDaoImpl implements BuyersDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Buyers> getBuyers() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Buyers");
        return query.list();
    }

    @Override
    public List<Object> getProjectBuyers() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT proposed.ops, proposed.body_id, foreas.title, \n"
                        + "foreas.address\n"
                        + "FROM Espa_Project_Eng2.Proposal_Body as foreas\n"
                        + "inner join Espa_Project_Eng2.Proposed_By as proposed\n"
                        + "on foreas.id = proposed.body_id ");
        return query.list();
    }

}
