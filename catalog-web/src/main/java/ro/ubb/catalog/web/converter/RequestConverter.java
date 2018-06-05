package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Request;
import ro.ubb.catalog.web.dto.RequestDTO;

public class RequestConverter extends BaseConverter<Request,RequestDTO> {
    @Override
    public Request convertDtoToModel(RequestDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public RequestDTO convertModelToDto(Request request)
    {
        Long patient,doctor, clinic;
        if(request.getDoctor()==null)
            doctor=-1L;
        else
            doctor = request.getDoctor().getId();
        if(request.getPatient()==null)
            patient=-1L;
        else
            patient = request.getPatient().getId();
        if(request.getClinic()==null)
            clinic=-1L;
        else
            clinic = request.getClinic().getId();

        RequestDTO res = new RequestDTO(patient,doctor,request.getRQuantity(),request.getPQuantity(),request.getTQuantity(),request.getPriority(),request.getCompleted(),request.getDate(),clinic);

        res.setId(request.getId());

        return res;
    }
}
