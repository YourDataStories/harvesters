package org.linkedeconomy.nsrfapi.service;

import java.util.List;
import org.linkedeconomy.nsrfapi.dto.OverviewCoordinates;
import org.linkedeconomy.nsrfapi.dto.OverviewDetails;
import org.linkedeconomy.nsrfapi.jpa.Overview;

/**
 *
 * @author G. Vafeiadis
 */
public interface OverviewService {

    /**
     *
     * Public Works service to provide logic to operate on the data sent to and
     * from the DAO and the client.
     *
     * @return
     */
    List<Overview> getOverviewEnisxiseis();

    List<Overview> getOverviewErga();

    List<OverviewDetails> getOverviewAndDetailsEnisxiseis();

    List<OverviewDetails> getOverviewAndDetailsErga();

    List<OverviewCoordinates> getOverviewCoordinates();

}
