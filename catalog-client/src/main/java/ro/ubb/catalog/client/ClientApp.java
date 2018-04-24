package ro.ubb.catalog.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.model.BloodList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by radu.
 */
public class ClientApp {

    public static void printAllBloods(RestTemplate restTemplate)
    {
        System.out.println("Blood list: ");

        // ---

        JsonNode recieved = restTemplate.getForObject("http://localhost:8080/api/bloodStocks", JsonNode.class);

        ObjectMapper mapper = new ObjectMapper();

        List<Blood> res = null;

        try {
            res = mapper.readValue(
                    mapper.treeAsTokens(recieved),
                    new TypeReference<List<Blood>>(){}
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        res.forEach(System.out::println);

        // ---

        System.out.println("Blood List finished.");
        System.out.println();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.catalog.client.config");
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        // ---

        System.out.println("List should contain one already existing element:");
        printAllBloods(restTemplate);

        Blood blood = restTemplate.postForObject("http://localhost:8080/api/bloods", new Blood("1",2.0f,3,"4"), Blood.class);

        System.out.println("Created new blood" + blood);

        System.out.println("List should contain the newly created blood:");
        printAllBloods(restTemplate);

        blood.setQuantity(blood.getQuantity()+3.0f);
        restTemplate.put("http://localhost:8080/api/bloods/{bloodId}", blood, blood.getId());

        System.out.println("List should contain an updated blood:");
        printAllBloods(restTemplate);

        restTemplate.delete("http://localhost:8080/api/bloods/{bloodId}", blood.getId());

        System.out.println("List should contain one already existing element:");;
        printAllBloods(restTemplate);

        System.out.println("bye ");
    }
}
