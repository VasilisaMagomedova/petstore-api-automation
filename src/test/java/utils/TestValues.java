package utils;

import payload.PetStatus;
import payload.PetTag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestValues {

    public static int testPetId = 563225;

    public static Map<Integer, String> testPetCategory = Map.of(
            1, "Cat",
            2, "Dog",
            3, "Fish"
    );
    public static int testPetCategoryId = 1;
    public static String testPetCategoryName = testPetCategory.get(testPetCategoryId);

    public static String testPetName = "Mr Meow";

    public static List<String> testPetPhotoUrls = Arrays.asList("url1", "url2");

    public static Map<Integer, String> testPetTags = Map.of(
            101, "Fluffy",
            102, "Playful"
    );
    public static List<PetTag> getPetTestTags() {
        return testPetTags.entrySet().stream()
                .map(entry -> new PetTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // берем вариант из enum PetStatus
    public static String testPetStatus = PetStatus.AVAILABLE.getStatus();

    // для изменения данных питомца
    public static String testPetNameUpdated = "Mr Cat";
    public static List<String> testPetPhotoUrlsUpdated = Arrays.asList("photo", "photo2", "photo3");
    public static String testPetStatusUpdated = PetStatus.SOLD.getStatus();
}
