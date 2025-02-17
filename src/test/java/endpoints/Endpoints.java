package endpoints;

public class Endpoints {

    public static final String PETSTORE_BASE_URL = "https://petstore.swagger.io/v2/";

    public static final String PET_POST_URL = PETSTORE_BASE_URL + "/pet";
    public static final String PET_GET_BY_ID_URL = PETSTORE_BASE_URL + "/pet/{petId}";
    public static final String PET_GET_BY_STATUS_URL = PETSTORE_BASE_URL + "/pet/findByStatus";
    public static final String PET_UPDATE_URL = PETSTORE_BASE_URL + "/pet";
    public static final String PET_DELETE_URL = PETSTORE_BASE_URL + "/pet/{petId}";

}
