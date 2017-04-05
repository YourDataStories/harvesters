package org.linkedeconomy.nsrfapi.service;

import java.util.List;
import org.linkedeconomy.nsrfapi.dto.ProjectBeneficiaries;
import org.linkedeconomy.nsrfapi.jpa.Beneficiaries;

/**
 *
 * @author G. Vafeiadis
 */
public interface BeneficiariesService {

    /**
     *
     * Beneficiaries service to provide logic to operate on the data sent to and
     * from the DAO and the client.
     *
     * @return
     */
    List<Beneficiaries> getBeneficiaries();

    List<ProjectBeneficiaries> getProjectBeneficiaries();

    List<ProjectBeneficiaries> getProjectBuyers();

    List<ProjectBeneficiaries> getSubProjectBuyers();

}
