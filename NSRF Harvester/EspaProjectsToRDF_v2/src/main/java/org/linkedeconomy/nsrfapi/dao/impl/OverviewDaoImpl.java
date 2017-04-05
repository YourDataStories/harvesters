package org.linkedeconomy.nsrfapi.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.linkedeconomy.nsrfapi.jpa.Overview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.linkedeconomy.nsrfapi.dao.OverviewDao;

/**
 *
 * @author G. Vafeiadis
 */
@Service
@Transactional
public class OverviewDaoImpl implements OverviewDao {
    
    /**
     *
     * Implements alla abstract methods of OverviewDao Interface
     *
     */

    @Autowired
    private SessionFactory sessionFactory;

    public List<Overview> getOverviewEnisxiseis() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Overview as over where over.type = 'Enisxiseis' order by over.id");
        query.setMaxResults(100);
        return query.list();
    }

    public List<Overview> getOverviewErga() {
        Query query = sessionFactory.
                getCurrentSession().
                createQuery("from Overview as over where over.type = 'Erga' order by over.id");
        query.setMaxResults(100);
        return query.list();
    }

    public List<Object> getOverviewAndDetailsEnisxiseis() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT overview.id, overview.kodikos, overview.title, overview.publishDate, \n"
                        + "overview.budget, overview.contracts, overview.payments, benef.name, benef.id as beneficiary,\n"
                        + "overview.epKodikos, overview.perifereia, overview.trexousaKatastash,\n"
                        + "details.titleEnglish, details.description ,details.startDate, details.endDate, \n"
                        + "details.completion, overview.countIpoergon\n"
                        + "FROM espa_Api4.Overview overview\n"
                        + "inner join\n"
                        + "espa_Api4.Details details\n"
                        + "on overview.id = details.projectId \n"
                        + "inner join \n"
                        + "espa_Api4.Protash benef\n"
                        + "on details.body =  benef.name\n"
                        + "where overview.type = \"Enisxiseis\"\n"
                        + "limit 50");
        return query.list();
    }

    public List<Object> getOverviewAndDetailsErga() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT overview.id, overview.kodikos, overview.title, overview.publishDate, \n"
                        + "overview.budget, overview.contracts, overview.payments, \n"
                        + "overview.epKodikos, overview.perifereia, overview.trexousaKatastash,\n"
                        + "details.titleEnglish, details.description ,details.startDate, details.endDate, \n"
                        + "details.completion, overview.countIpoergon\n"
                        + "FROM espa_Api4.Overview overview\n"
                        + "inner join\n"
                        + "espa_Api4.Details details\n"
                        + "on overview.id = details.projectId \n"
                        + "where overview.type = \"Erga\"");
        return query.list();
    }

    public List<Object> getOverviewCoordinates() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT overview.kodikos, prcoords.coordinatesId , coords.coordinates\n"
                        + "FROM espa_Api4.Overview overview\n"
                        + "inner join \n"
                        + "espa_Api4.Coordinates_Project prcoords\n"
                        + "on overview.id = prcoords.projectId\n"
                        + "inner join \n"
                        + "espa_Api4.Coordinates coords\n"
                        + "on prcoords.coordinatesId = coords.id "
                        + "where overview.type = \"Erga\""
                        + "");
        return query.list();
    }

    public List<Object> getOverviewDetailsCoordinatesEnisxiseis() {
        Query query = sessionFactory.
                getCurrentSession().
                createSQLQuery("SELECT overview.id, overview.kodikos, overview.title, overview.publishDate, \n"
                        + "overview.budget, overview.contracts, overview.payments, benef.name as name, benef.id as beneficiary,\n"
                        + "overview.epKodikos, overview.perifereia, overview.trexousaKatastash,\n"
                        + "details.titleEnglish, details.description ,details.startDate, details.endDate, \n"
                        + "details.completion, overview.countIpoergon, coords.coordinates\n"
                        + "FROM espa_Api4.Overview overview\n"
                        + "inner join\n"
                        + "espa_Api4.Details details\n"
                        + "on overview.id = details.projectId \n"
                        + "inner join \n"
                        + "espa_Api4.Protash benef\n"
                        + "on details.body =  benef.name\n"
                        + "inner join\n"
                        + "espa_Api4.Coordinates_Project prcoords\n"
                        + "on overview.id = prcoords.projectId\n"
                        + "inner join \n"
                        + "espa_Api4.Coordinates coords\n"
                        + "on prcoords.coordinatesId = coords.id \n"
                        + "where overview.type = \"Enisxiseis\" and benef.id = \"811\"\n"
                        + "limit 3");
        return query.list();
    }

}
