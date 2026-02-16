import com.fasterxml.jackson.databind.ObjectMapper;
import dev.benj.common.model.Name;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.datatype.jsr310.*;


import static java.lang.IO.println;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



static void main() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    try {
        var namesByType = readNamesFromClasspath();
        var surnames = namesByType.get("surnames");
        var students = populateNameSet(
                namesByType.get("masculine_names"), surnames).stream()
                .map(name -> new StudentWithoutId(
                name,
                LocalDate.of(2022, 9, 1)
        )).collect(Collectors.toSet());

        students.addAll(
                populateNameSet(
                        namesByType.get("feminine_names"), surnames).stream()
                        .map(name -> new StudentWithoutId(
                                name,
                                LocalDate.of(2022, 9, 1)
                        )).collect(Collectors.toSet())
        );

        for(int i = 1; i < 8; i++){
            try(CloseableHttpClient c = HttpClients.createMinimal()) {
                HttpPost r = new HttpPost("http://localhost:8080/admin/bulk/enroll?semesterId=%s".formatted(i));
                r.setHeader("Content-Type", "application/json");
                r.setEntity(new StringEntity(mapper.writeValueAsString(students)));
                var response = c.execute(r);
                println(response);
            }
        }

    } catch (IOException e) {
        println("Error reading file: " + e.getMessage());
    }
}

private static Set<Name> populateNameSet(String[] firstNameList, String[] surnameList){
    Random surnameIndexGenerator = new Random();
    Random middleNameIndexGenerator = new Random();
    Set<Name> randomNames = new HashSet<>();

    for(String name : firstNameList){
        int surnameIndex = surnameIndexGenerator.nextInt(surnameList.length);
        int middleNameIndex = middleNameIndexGenerator.nextInt(firstNameList.length);
        randomNames.add(new Name(name, firstNameList[middleNameIndex], surnameList[surnameIndex]));
    }
    return randomNames;
}


private static Map<String, String[]> readNamesFromClasspath() throws IOException {
    try (InputStream input = ClassLoader.getSystemResourceAsStream("names.txt")) {
        if (input == null) {
            throw new IOException("names.txt not found in classpath");
        }

        Map<String, String[]> namesByType = new HashMap<>();
        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach(nameListLine -> {
                    String[] nameTypeAndList = nameListLine.split("=");
                    String nameType = nameTypeAndList[0].trim().replaceAll("[$',^]", "");
                    String[] nameList = nameTypeAndList[1].trim().replaceAll("[$'^]", "").split(",");
                    namesByType.put(nameType, nameList);
                });
        return namesByType;
    }
}

record StudentWithoutId(Name name, LocalDate enrollmentDate){}


