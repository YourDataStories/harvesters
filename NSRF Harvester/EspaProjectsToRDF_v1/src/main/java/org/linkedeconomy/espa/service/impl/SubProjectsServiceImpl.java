/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl;

import java.util.List;
import org.linkedeconomy.espa.dao.SubProjectsDao;
import org.linkedeconomy.espa.jpa.SubProjects;
import org.linkedeconomy.espa.service.SubProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class SubProjectsServiceImpl implements SubProjectsService {

    @Autowired
    private SubProjectsDao subProjectsDao;
    public List<SubProjects> getSubProjects() {
        return subProjectsDao.getSubProjects();
    }
    
}
