package com.bafagroupe.christab_security.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


public class HeaderUtil {

	private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "christaB";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-christaBApp-alert", message);
        headers.add("X-christaBApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }
    
    public static HttpHeaders createEntityFailedAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + "echec", param);
    }
    
    public static HttpHeaders createEntitySuccessAlert(String entityName) {
        return createAlert(APPLICATION_NAME + "." + entityName + "success", "1" );
    }
    
    public static HttpHeaders createFailedAlert(String entityName) {
        return createAlert(APPLICATION_NAME + "." + entityName + "echec", "0");
    }
    
    /*public static HttpHeaders createSuccessAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + "success", param);
    }*/
    public static HttpHeaders createSuccessAlert(String param) {
        return createAlert( "Vérification du compte : ", param+" effectuée avec succès");
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-christaBApp-error", "error." + errorKey);
        headers.add("X-christaBApp-params", entityName);
        return headers;
    }
	
}
