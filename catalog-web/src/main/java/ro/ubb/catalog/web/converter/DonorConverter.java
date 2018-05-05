package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Donor;
import ro.ubb.catalog.web.dto.DonorDTO;

public class DonorConverter extends BaseConverter<Donor,DonorDTO> {
    @Override
    public Donor convertDtoToModel(DonorDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public DonorDTO convertModelToDto(Donor donor) {
       DonorDTO res = new DonorDTO(donor.getName(),donor.getBirthday(),donor.getResidence(),donor.getBloodType(),donor.getRh(),donor.getAnticorps(),donor.getIsDonor(),donor.getLatitude(),donor.getLongitude(),donor.getNextDonation(),donor.getEligibility());

       res.setId(donor.getId());

       return res;
    }
}
