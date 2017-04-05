/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl;

import java.util.List;
import org.linkedeconomy.espa.dao.ReviewDao;
import org.linkedeconomy.espa.jpa.Review;
import org.linkedeconomy.espa.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;
    public List<Review> getReview() {
        return reviewDao.getReview();
        
    }
    
}
