package org.linkedeconomy.nsrfapi.dao;

import java.util.List;
import org.linkedeconomy.nsrfapi.jpa.Beneficiaries;

/**
 *
 * @author G. Vafeiadis
 */
public interface BeneficiariesDao {

    /**
     *
     * A DAO interface to handle the database operation required to manipulate a
     * beneficiary entity
     *
     * @return
     */
    List<Beneficiaries> getBeneficiaries();

    List<Object> getProjectBeneficiaries();

    List<Object> getProjectBuyers();

    List<Object> getSubProjectBuyers();
}
