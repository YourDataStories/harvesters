package org.linkedeconomy.nsrfapi.dao;

import java.util.List;
import org.linkedeconomy.nsrfapi.jpa.Overview;

/**
 *
 * @author G. Vafeiadis
 */
public interface OverviewDao {

    /**
     *
     * A DAO interface to handle the database operation required to manipulate a
     * Public Project entity
     *
     * @return
     */
    List<Overview> getOverviewEnisxiseis();

    List<Overview> getOverviewErga();

    List<Object> getOverviewAndDetailsEnisxiseis();

    List<Object> getOverviewAndDetailsErga();

    List<Object> getOverviewCoordinates();

    List<Object> getOverviewDetailsCoordinatesEnisxiseis();

}
