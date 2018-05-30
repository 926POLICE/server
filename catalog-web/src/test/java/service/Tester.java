package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.core.service.BloodService;
import ro.ubb.catalog.core.service.BloodServiceImpl;
import ro.ubb.catalog.web.dto.RequestDTO;

import java.util.Set;

public class Tester
{
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testDoctor()
    {
        //Set<RequestDTO> requestDTOSet = restTemplate.getForObject("http://localhost:8080/api/requests/doctors")
    }
}