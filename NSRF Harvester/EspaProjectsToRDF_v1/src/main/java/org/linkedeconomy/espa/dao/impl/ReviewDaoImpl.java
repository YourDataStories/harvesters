/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.espa.dao.ReviewDao;
import org.linkedeconomy.espa.jpa.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class ReviewDaoImpl implements ReviewDao {

    @Autowired
    private SessionFactory sessionFactory;

    String startDateString = "2016-02-02";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate;

    @Override
    public List<Review> getReview() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Review r where r.date = :date_issued");
        try {
            query.setParameter("date_issued", startDate = df.parse(startDateString));
        } catch (ParseException ex) {
            Logger.getLogger(ReviewDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.list();
    }

}
