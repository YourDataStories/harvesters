package org.linkedeconomy.nsrfapi.dao;

import java.util.List;
import org.linkedeconomy.nsrfapi.jpa.SubProjects;

/**
 *
 * @author G. Vafeiadis
 */
public interface SubProjectsDao {

    /**
     *
     * A DAO interface to handle the database operation required to manipulate a
     * Subproject entity
     *
     * @return
     */
    List<SubProjects> getSubProjects();

    List<Object> getInfoSubproject();

}
