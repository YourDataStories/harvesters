/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.nsrfapi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.linkedeconomy.nsrfapi.dao.SubProjectsDao;
import org.linkedeconomy.nsrfapi.dto.SubprojectsProjects;
import org.linkedeconomy.nsrfapi.jpa.SubProjects;
import org.linkedeconomy.nsrfapi.service.SubProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class SubProjectsServiceImpl implements SubProjectsService {

    /**
     *
     * Implements alla abstract methods of SubProjectsService Interface
     *
     */
    @Autowired
    private SubProjectsDao subProjectsDao;

    public List<SubProjects> getSubProjects() {
        return subProjectsDao.getSubProjects();
    }

    public List<SubprojectsProjects> getInfoSubproject() {
        List<Object> subprojects = subProjectsDao.getInfoSubproject();
        List<SubprojectsProjects> subprojectinfo = new ArrayList<SubprojectsProjects>();
        for (Object orgs1 : subprojects) {
            Object[] o = (Object[]) orgs1;
            ArrayList<Object> subpr = new ArrayList<Object>(Arrays.asList(o));
            SubprojectsProjects v = new SubprojectsProjects();
            v.setSubprojectId((Integer) subpr.get(0));
            v.setOps((String) subpr.get(1));
            v.setTitle((String) subpr.get(2));
            v.setConstructorId((Integer) subpr.get(3));
            v.setConstructorName((String) subpr.get(4));
            v.setStart((String) subpr.get(5));
            v.setFinish((String) subpr.get(6));
            v.setBudget((BigDecimal) subpr.get(7));
            subprojectinfo.add(v);
        }

        return subprojectinfo;
    }

}
