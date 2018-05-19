package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.web.dto.BloodDTO;

@Component
public class BloodConverter  extends BaseConverter<Blood, BloodDTO>
{
    @Override
    public Blood convertDtoToModel(BloodDTO dto)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public BloodDTO convertModelToDto(Blood blood)
    {
        BloodDTO res = null;

        Long donationID, clinicID;
        if(blood.getDonation()==null)
            donationID=-1L;
        else
            donationID=blood.getDonation().getId();
        if(blood.getClinic()==null)
            clinicID=-1L;
        else
            clinicID = blood.getClinic().getId();

        res = new BloodDTO(blood.getCollectionDate(),blood.getQuantity(),blood.getState(),blood.getType(),blood.getShelfLife(),blood.getTested(),blood.getUsable(),donationID,clinicID);

        res.setId(blood.getId());

        return res;
    }
}
