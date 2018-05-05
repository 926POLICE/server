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

        if(blood.getClinic()==null||blood.getDonation()==null)
            res =  new BloodDTO(blood.getCollectionDate(),blood.getQuantity(),blood.getState(),blood.getType(),blood.getShelfLife(),-1l,-1l);
        else
            res = new BloodDTO(blood.getCollectionDate(),blood.getQuantity(),blood.getState(),blood.getType(),blood.getShelfLife(),blood.getDonation().getId(),blood.getClinic().getId());

        res.setId(blood.getId());

        return res;
    }
}
