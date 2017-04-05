package org.linkedeconomy.nsrfapi.service;

import java.util.List;
import org.linkedeconomy.nsrfapi.dto.SubprojectsProjects;
import org.linkedeconomy.nsrfapi.jpa.SubProjects;

/**
 *
 * @author G. Vafeiadis
 */
public interface SubProjectsService {

    /**
     *
     * Subprojects service to provide logic to operate on the data sent to and
     * from the DAO and the client.
     *
     * @return
     */
    List<SubProjects> getSubProjects();

    List<SubprojectsProjects> getInfoSubproject();

}
