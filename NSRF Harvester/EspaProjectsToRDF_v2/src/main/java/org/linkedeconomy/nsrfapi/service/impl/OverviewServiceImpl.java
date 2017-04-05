package org.linkedeconomy.nsrfapi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.linkedeconomy.nsrfapi.dao.OverviewDao;
import org.linkedeconomy.nsrfapi.dto.OverviewCoordinates;
import org.linkedeconomy.nsrfapi.dto.OverviewDetails;
import org.linkedeconomy.nsrfapi.jpa.Overview;
import org.linkedeconomy.nsrfapi.service.OverviewService;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class OverviewServiceImpl implements OverviewService {

    /**
     *
     * Implements alla abstract methods of OverviewService Interface
     *
     */
    @Autowired
    private OverviewDao overviewDao;

    public List<Overview> getOverviewEnisxiseis() {
        return overviewDao.getOverviewEnisxiseis();
    }

    public List<Overview> getOverviewErga() {
        return overviewDao.getOverviewErga();
    }

    public List<OverviewDetails> getOverviewAndDetailsEnisxiseis() {
        List<Object> details = overviewDao.getOverviewAndDetailsEnisxiseis();
        List<OverviewDetails> overviewDetails = new ArrayList<OverviewDetails>();
        for (Object details1 : details) {
            Object[] o = (Object[]) details1;
            ArrayList<Object> detail = new ArrayList<Object>(Arrays.asList(o));
            OverviewDetails v = new OverviewDetails();
            v.setProjectId((Integer) detail.get(0));
            v.setOps((String) detail.get(1));
            v.setTitle((String) detail.get(2));
            v.setPublishDate((Date) detail.get(3));
            v.setBudget((BigDecimal) detail.get(4));
            v.setContracts((BigDecimal) detail.get(5));
            v.setPayments((BigDecimal) detail.get(6));
            v.setEpKodikos((String) detail.get(7));
            v.setPerifereia((String) detail.get(8));
            v.setTrexousaKatastash((Integer) detail.get(9));
            v.setTitleEnglish((String) detail.get(10));
            v.setDescription((String) detail.get(11));
            v.setStartDate((Date) detail.get(12));
            v.setEndDate((Date) detail.get(13));
            v.setCompletion((String) detail.get(14));
            v.setCountSubprojects((Integer) detail.get(15));
            overviewDetails.add(v);
        }

        return overviewDetails;
    }

    public List<OverviewDetails> getOverviewAndDetailsErga() {
        List<Object> details = overviewDao.getOverviewAndDetailsErga();
        List<OverviewDetails> overviewDetails = new ArrayList<OverviewDetails>();
        for (Object details1 : details) {
            Object[] o = (Object[]) details1;
            ArrayList<Object> detail = new ArrayList<Object>(Arrays.asList(o));
            OverviewDetails v = new OverviewDetails();
            v.setProjectId((Integer) detail.get(0));
            v.setOps((String) detail.get(1));
            v.setTitle((String) detail.get(2));
            v.setPublishDate((Date) detail.get(3));
            v.setBudget((BigDecimal) detail.get(4));
            v.setContracts((BigDecimal) detail.get(5));
            v.setPayments((BigDecimal) detail.get(6));
            v.setEpKodikos((String) detail.get(7));
            v.setPerifereia((String) detail.get(8));
            v.setTrexousaKatastash((Integer) detail.get(9));
            v.setTitleEnglish((String) detail.get(10));
            v.setDescription((String) detail.get(11));
            v.setStartDate((Date) detail.get(12));
            v.setEndDate((Date) detail.get(13));
            v.setCompletion((String) detail.get(14));
            v.setCountSubprojects((Integer) detail.get(15));
            overviewDetails.add(v);
        }

        return overviewDetails;
    }

    public List<OverviewCoordinates> getOverviewCoordinates() {
        List<Object> coordinates = overviewDao.getOverviewCoordinates();
        List<OverviewCoordinates> overviewCoordinates = new ArrayList<OverviewCoordinates>();
        for (Object coordinates1 : coordinates) {
            Object[] o = (Object[]) coordinates1;
            ArrayList<Object> coordinate = new ArrayList<Object>(Arrays.asList(o));
            OverviewCoordinates v = new OverviewCoordinates();
            v.setKodikos((String) coordinate.get(0));
            v.setCoordinatesId((Integer) coordinate.get(1));
            v.setCoordinates((String) coordinate.get(2));
            overviewCoordinates.add(v);
        }

        return overviewCoordinates;
    }

}
