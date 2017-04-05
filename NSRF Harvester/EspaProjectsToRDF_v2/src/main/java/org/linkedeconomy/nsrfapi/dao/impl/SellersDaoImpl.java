package org.linkedeconomy.nsrfapi.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.nsrfapi.dao.SellersDao;
import org.linkedeconomy.nsrfapi.jpa.Sellers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class SellersDaoImpl implements SellersDao {

    /**
     *
     * Implements alla abstract methods of SellersDao Interface
     *
     */
    @Autowired
    private SessionFactory sessionFactory;

    public List<Sellers> getSellers() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Sellers as seller where seller.id != '12244'");
//        query.setMaxResults(500);
        return query.list();
    }

    public List<Object> getProjectSellers() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT subproject.id,\n"
                        + "details.kodikos ,seller.constructorID, sellerList.name, sellerList.vatId\n"
                        + "FROM espa_Api4.Sub_Projects subproject \n"
                        + "inner join \n"
                        + "espa_Api4.Projects_Sellers seller \n"
                        + "on subproject.id =  seller.subProjectID\n"
                        + "inner join \n"
                        + "espa_Api4.Sellers sellerList \n"
                        + "on seller.constructorID =  sellerList.id\n"
                        + "inner join \n"
                        + "espa_Api4.SubProject_Project projectjoin \n"
                        + "on subproject.id =  projectjoin.subProjectId\n"
                        + "inner join\n"
                        + "espa_Api4.Details details\n"
                        + "on projectjoin.detailsId = details.id");
        return query.list();
    }

}
