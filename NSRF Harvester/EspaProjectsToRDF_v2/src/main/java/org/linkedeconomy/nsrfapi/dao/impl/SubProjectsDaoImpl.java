package org.linkedeconomy.nsrfapi.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.nsrfapi.dao.SubProjectsDao;
import org.linkedeconomy.nsrfapi.jpa.SubProjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class SubProjectsDaoImpl implements SubProjectsDao {

    /**
     *
     * Implements alla abstract methods of SubProjectsDao Interface
     *
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SubProjects> getSubProjects() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from SubProjects");
        return query.list();
    }
    
    public List<Object> getInfoSubproject() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT subproject.id, details.kodikos, subproject.title,\n"
                        + "seller.constructorID, sellerList.name, \n"
                        + "subproject.start, subproject.finish, subproject.budget "
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
