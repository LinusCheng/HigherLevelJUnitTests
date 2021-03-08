package practice.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import practice.exception.MoneyOrderException;
import practice.exception.SecureTokenConvertingException;
import practice.util.SecureTokenConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FetchDataService {

    private static final Log log = LogFactory.getLog(FetchDataService.class);

    @Value("${moneyOrder.branch}")
    private String branch;
    @Value("${moneyOrder.currency}")
    private String currency;
    @Value("${moneyOrder.url}")
    private String url;

    public String fetchMoneyOrderCode(String secureToken, String amount) throws MoneyOrderException, SecureTokenConvertingException {
        log.info("Start generating money order...");

        String moneyOrderCode = null;

        RestTemplate restTemplate = new RestTemplate();

        String processedSecureToken = SecureTokenConverter.convert(secureToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        httpHeaders.set("secureToken", processedSecureToken);
        httpHeaders.set("amount", amount);

        Map<String,String> params = new HashMap<>();
        params.put("branch", branch);
        params.put("currency", currency);
        HttpEntity<String> req = new HttpEntity<>(
                params.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining("+"))
                , httpHeaders);

        String res = restTemplate.postForObject(url, req, String.class);

        try {
            JSONObject fetchedJson = new JSONObject(res);
            moneyOrderCode= fetchedJson.getString("money_order_code");
            log.info("Money order successfully generated!");
        } catch (JSONException | NullPointerException e) {
            log.error(e);
            throw new MoneyOrderException("Error returned payload", e);
        }

        return moneyOrderCode;
    }

}
