package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Donation;
import ro.ubb.catalog.web.dto.DonationDTO;

public class DonationConverter extends BaseConverter<Donation,DonationDTO> {
    @Override
    public Donation convertDtoToModel(DonationDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public DonationDTO convertModelToDto(Donation donation)
    {
        Long R, P, T, donor, patient,clinic;
        if(donation.getR()==null)
            R=-1L;
        else
            R=donation.getR().getId();
        if(donation.getP()==null)
            P=-1L;
        else
            P=donation.getP().getId();
        if(donation.getT()==null)
            T=-1L;
        else
            T=donation.getT().getId();
        if(donation.getDonor()==null)
            donor=-1L;
        else
            donor = donation.getDonor().getId();
        if(donation.getPatient()==null)
            patient=-1L;
        else
            patient = donation.getPatient().getId();
        if(donation.getClinic()==null)
            clinic=-1L;
        else
            clinic = donation.getClinic().getId();

        DonationDTO res = new DonationDTO(R,P,T,donor,patient,donation.getAnalysisResult(),clinic);

        res.setId(donation.getId());

        return res;
    }
}
