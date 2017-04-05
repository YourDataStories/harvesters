package org.linkedeconomy.nsrfapi.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.linkedeconomy.nsrfapi.dao.BeneficiariesDao;
import org.linkedeconomy.nsrfapi.jpa.Beneficiaries;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class BeneficiariesDaoImpl implements BeneficiariesDao {
    
    /**
     *
     * Implements alla abstract methods of BeneficiariesDao Interface
     *
     */

    @Autowired
    private SessionFactory sessionFactory;

    public List<Beneficiaries> getBeneficiaries() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Beneficiaries");
//        query.setMaxResults(3766);
        return query.list();
    }

    public List<Object> getProjectBeneficiaries() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT benef.id, details.kodikos, details.body\n"
                        + "FROM espa_Api4.Details details\n"
                        + "inner join \n"
                        + "espa_Api4.Protash benef\n"
                        + "on details.body =  benef.name \n"
                        + "inner join \n"
                        + "espa_Api4.Overview overview\n"
                        + "on details.projectId =  overview.id \n"
                        + "where overview.type = \"Enisxiseis\"");
        return query.list();
    }

    public List<Object> getProjectBuyers() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT benef.id, details.kodikos, details.body, benef.vatId "
//                        + ", subproj.subProjectId\n"
                        + "FROM espa_Api4.Details details\n"
                        + "inner join \n"
                        + "espa_Api4.Protash benef\n"
                        + "on details.body =  benef.name \n"
                        + "inner join \n"
                        + "espa_Api4.Overview overview\n"
                        + "on details.projectId =  overview.id \n"
//                        + "inner join \n"
//                        + "espa_Api4.SubProject_Project subproj\n"
//                        + "on details.id =  subproj.detailsId\n"
                        + "where overview.type = \"Erga\"");
        return query.list();
    }
    
    public List<Object> getSubProjectBuyers() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT benef.id, details.kodikos, details.body, benef.vatId"
                        + ", subproj.subProjectId\n"
                        + "FROM espa_Api4.Details details\n"
                        + "inner join \n"
                        + "espa_Api4.Protash benef\n"
                        + "on details.body =  benef.name \n"
                        + "inner join \n"
                        + "espa_Api4.Overview overview\n"
                        + "on details.projectId =  overview.id \n"
                        + "inner join \n"
                        + "espa_Api4.SubProject_Project subproj\n"
                        + "on details.id =  subproj.detailsId\n"
                        + "where overview.type = \"Erga\"");
        return query.list();
    }

}
