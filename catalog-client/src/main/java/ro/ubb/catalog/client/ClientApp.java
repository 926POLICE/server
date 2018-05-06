package ro.ubb.catalog.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.web.dto.BloodDTO;

import java.io.IOException;
import java.util.List;

/**
 * Created by radu.
 */
public class ClientApp {

    public static void printAllBloods(RestTemplate restTemplate)
    {
        System.out.println("Blood list: ");

        // ---

        JsonNode recieved = restTemplate.getForObject("http://localhost:8080/api/bloods", JsonNode.class);

        ObjectMapper mapper = new ObjectMapper();

        List<BloodDTO> BloodList = null;

        try {
             BloodList = mapper.readValue(
                    mapper.treeAsTokens(recieved),
                    new TypeReference<List<BloodDTO>>(){}
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        BloodList.forEach(System.out::println);

        // ---

        System.out.println("Blood List finished.");
        System.out.println();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.catalog.client.config");
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        // ---

        System.out.println("List should contain one element:");
        printAllBloods(restTemplate);

        BloodDTO blood = restTemplate.postForObject("http://localhost:8080/api/bloods", new BloodDTO("1",2.0f,3,"p",-1,1l,2l), BloodDTO.class);

        System.out.println("Created new blood" + blood);

        System.out.println("List should contain the previous element and the newly created blood:");
        printAllBloods(restTemplate);

        blood.setQuantity(blood.getQuantity()+3.0f);
        restTemplate.put("http://localhost:8080/api/bloods/{bloodId}", blood, blood.getId());

        System.out.println("List should contain the previous element and an updated blood:");
        printAllBloods(restTemplate);

        restTemplate.delete("http://localhost:8080/api/bloods/{bloodId}", blood.getId());

        System.out.println("List should contain only the initial element:");
        printAllBloods(restTemplate);

        Boolean res = restTemplate.getForObject("http://localhost:8080/api/donors/eligibility/{donorID}",Boolean.class,1l);
        System.out.println(res);

        System.out.println("bye ");
    }
}
