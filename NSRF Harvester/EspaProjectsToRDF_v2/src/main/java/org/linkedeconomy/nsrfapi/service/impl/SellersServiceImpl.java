package org.linkedeconomy.nsrfapi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.linkedeconomy.nsrfapi.dao.SellersDao;
import org.linkedeconomy.nsrfapi.dto.ProjectSellers;
import org.linkedeconomy.nsrfapi.jpa.Sellers;
import org.linkedeconomy.nsrfapi.service.SellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class SellersServiceImpl implements SellersService {

    /**
     *
     * Implements alla abstract methods of SellersService Interface
     *
     */
    @Autowired
    private SellersDao sellersDao;

    public List<Sellers> getSellers() {
        return sellersDao.getSellers();
    }

    public List<ProjectSellers> getProjectSellers() {
        List<Object> sellers = sellersDao.getProjectSellers();
        List<ProjectSellers> sellerInfo = new ArrayList<ProjectSellers>();
        for (Object sellers1 : sellers) {
            Object[] o = (Object[]) sellers1;
            ArrayList<Object> seller = new ArrayList<Object>(Arrays.asList(o));
            ProjectSellers v = new ProjectSellers();
            v.setSubprojectId((Integer) seller.get(0));
            v.setCode((String) seller.get(1));
            v.setSellerId((Integer) seller.get(2));
            v.setSellerName((String) seller.get(3));
            v.setVatId((String) seller.get(4));
            sellerInfo.add(v);
        }

        return sellerInfo;
    }

}
