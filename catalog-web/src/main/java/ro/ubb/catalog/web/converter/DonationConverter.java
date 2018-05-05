package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Donation;
import ro.ubb.catalog.web.dto.DonationDTO;

public class DonationConverter extends BaseConverter<Donation,DonationDTO> {
    @Override
    public Donation convertDtoToModel(DonationDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public DonationDTO convertModelToDto(Donation donation) {
        DonationDTO res = new DonationDTO(donation.getR().getId(),donation.getP().getId(),donation.getT().getId(),donation.getDonor().getId(),donation.getPatient().getId(),donation.getAnalysisResult(),donation.getClinic().getId());

        res.setId(donation.getId());

        return res;
    }
}
