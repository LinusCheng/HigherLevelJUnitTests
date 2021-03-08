package practice.service;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import practice.exception.MoneyOrderException;
import practice.util.SecureTokenConverter;

@PrepareForTest({FetchDataService.class, SecureTokenConverter.class})
@RunWith(PowerMockRunner.class)
public class FetchDataServiceTest {

    private FetchDataService fetchDataService;

    @Mock
    RestTemplate restTemplateMock;
    @Mock
    JSONObject fetchedJson;

    @Before
    public void setup() throws Exception {
        fetchDataService = new FetchDataService();
        // This class doesn't have an all-args constructor, so we need to fill them in with Reflection
        ReflectionTestUtils.setField(fetchDataService, "branch", "716");
        ReflectionTestUtils.setField(fetchDataService, "currency", "USD");
        ReflectionTestUtils.setField(fetchDataService, "url", "dummyUrl");

        // Mock constructor
        Mockito.doReturn("dummy json stuffs").when(restTemplateMock)
                .postForObject(Mockito.anyString(), Mockito.any(), Mockito.any());
        PowerMockito.whenNew(RestTemplate.class).withNoArguments().thenReturn(restTemplateMock);

        Mockito.doReturn("dummy_money_order_code_1503427798").when(fetchedJson).getString(Mockito.anyString());

        // Mock static method
        PowerMockito.mockStatic(SecureTokenConverter.class);
        PowerMockito.when(SecureTokenConverter.convert(Mockito.anyString())).thenReturn("Mocked good token");
        //can use either Mockito.when(...) or PowerMockito.when(...)

    }

    @Test
    public void fetchMoneyOrderCode_success() throws Exception {
        // Mock constructor
        PowerMockito.whenNew(JSONObject.class).withAnyArguments().thenReturn(fetchedJson);
        String moneyOrderCode = fetchDataService.fetchMoneyOrderCode("fake_token", "500.00");
        Assert.assertEquals("dummy_money_order_code_1503427798", moneyOrderCode);
    }

    @Test(expected = MoneyOrderException.class)
    public void fetchMoneyOrderCode_exp() throws Exception {
        fetchDataService.fetchMoneyOrderCode("fake_token", "500.00");
    }

}
