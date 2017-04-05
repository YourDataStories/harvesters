package org.linkedeconomy.nsrfapi.dao;

import java.util.List;
import org.linkedeconomy.nsrfapi.jpa.Sellers;

/**
 *
 * @author G. Vafeiadis
 */
public interface SellersDao {

    /**
     *
     * A DAO interface to handle the database operation required to manipulate a
     * Seller entity
     *
     * @return
     */
    List<Sellers> getSellers();

    List<Object> getProjectSellers();
}
