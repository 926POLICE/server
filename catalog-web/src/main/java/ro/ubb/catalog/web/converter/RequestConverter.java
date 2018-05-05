package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Request;
import ro.ubb.catalog.web.dto.RequestDTO;

public class RequestConverter extends BaseConverter<Request,RequestDTO> {
    @Override
    public Request convertDtoToModel(RequestDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public RequestDTO convertModelToDto(Request request) {
        RequestDTO res = new RequestDTO(request.getPatient().getId(),request.getDoctor().getId(),request.getRQuantity(),request.getPQuantity(),request.getTQuantity(),request.getPriority(),request.getCompleted(),request.getClinic().getId());

        res.setId(request.getId());

        return res;
    }
}
