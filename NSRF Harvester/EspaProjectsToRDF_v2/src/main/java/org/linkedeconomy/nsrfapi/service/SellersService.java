package org.linkedeconomy.nsrfapi.service;

import java.util.List;
import org.linkedeconomy.nsrfapi.dto.ProjectSellers;
import org.linkedeconomy.nsrfapi.jpa.Sellers;

/**
 *
 * @author G. Vafeiadis
 */
public interface SellersService {

    /**
     *
     * Sellers service to provide logic to operate on the data sent to and from
     * the DAO and the client.
     *
     * @return
     */
    List<Sellers> getSellers();

    List<ProjectSellers> getProjectSellers();

}
