package org.linkedeconomy.nsrfapi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.linkedeconomy.nsrfapi.dto.ProjectBeneficiaries;
import org.linkedeconomy.nsrfapi.service.BeneficiariesService;
import org.linkedeconomy.nsrfapi.dao.BeneficiariesDao;
import org.linkedeconomy.nsrfapi.jpa.Beneficiaries;

/**
 *
 * @author G. Vafeiadis
 */
@Service
public class BeneficiariesServiceImpl implements BeneficiariesService {

    /**
     *
     * Implements alla abstract methods of BeneficiariesService Interface
     *
     */
    @Autowired
    private BeneficiariesDao beneficiariesDao;

    public List<Beneficiaries> getBeneficiaries() {
        return beneficiariesDao.getBeneficiaries();
    }

    public List<ProjectBeneficiaries> getProjectBeneficiaries() {
        List<Object> beneficiaries = beneficiariesDao.getProjectBeneficiaries();
        List<ProjectBeneficiaries> beneficiaryInfo = new ArrayList<ProjectBeneficiaries>();
        for (Object beneficiary1 : beneficiaries) {
            Object[] o = (Object[]) beneficiary1;
            ArrayList<Object> benef = new ArrayList<Object>(Arrays.asList(o));
            ProjectBeneficiaries v = new ProjectBeneficiaries();
            v.setBeneficiaryId((Integer) benef.get(0));
            v.setOps((String) benef.get(1));
            v.setName((String) benef.get(2));
            beneficiaryInfo.add(v);
        }

        return beneficiaryInfo;
    }

    public List<ProjectBeneficiaries> getProjectBuyers() {
        List<Object> buyers = beneficiariesDao.getProjectBuyers();
        List<ProjectBeneficiaries> buyerInfo = new ArrayList<ProjectBeneficiaries>();
        for (Object buyers1 : buyers) {
            Object[] o = (Object[]) buyers1;
            ArrayList<Object> buyer = new ArrayList<Object>(Arrays.asList(o));
            ProjectBeneficiaries v = new ProjectBeneficiaries();
            v.setBeneficiaryId((Integer) buyer.get(0));
            v.setOps((String) buyer.get(1));
            v.setName((String) buyer.get(2));
            v.setVatId((String) buyer.get(3));
//            v.setSubprojectId((Integer) buyer.get(4));
            buyerInfo.add(v);
        }

        return buyerInfo;
    }

    public List<ProjectBeneficiaries> getSubProjectBuyers() {
        List<Object> buyers = beneficiariesDao.getSubProjectBuyers();
        List<ProjectBeneficiaries> buyerInfo = new ArrayList<ProjectBeneficiaries>();
        for (Object buyers1 : buyers) {
            Object[] o = (Object[]) buyers1;
            ArrayList<Object> buyer = new ArrayList<Object>(Arrays.asList(o));
            ProjectBeneficiaries v = new ProjectBeneficiaries();
            v.setBeneficiaryId((Integer) buyer.get(0));
            v.setOps((String) buyer.get(1));
            v.setName((String) buyer.get(2));
            v.setVatId((String) buyer.get(3));
            v.setSubprojectId((Integer) buyer.get(4));
            buyerInfo.add(v);
        }

        return buyerInfo;
    }

}
