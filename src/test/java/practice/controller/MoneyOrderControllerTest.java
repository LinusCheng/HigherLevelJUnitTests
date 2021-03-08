package practice.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import practice.exception.UnauthorizedException;
import practice.service.FetchDataService;

import javax.servlet.http.HttpServletRequest;

@PrepareForTest(MoneyOrderController.class)
@RunWith(PowerMockRunner.class)
public class MoneyOrderControllerTest {

    MoneyOrderController moneyOrderController;

    @Mock
    FetchDataService fetchDataService;
    @Mock
    HttpServletRequest requestMock;
    @Mock
    Object bodyMock;
    @Mock
    ResponseEntity responseEntityMock;

    @Before
    public void setup() throws Exception {
        // We spy moneyOrderController, because we want to mock one of its method: authorize(...)
        moneyOrderController = Mockito.spy( new MoneyOrderController());
        ReflectionTestUtils.setField(moneyOrderController, "fetchDataService", fetchDataService);
        PowerMockito.whenNew(ResponseEntity.class).withAnyArguments().thenReturn(responseEntityMock);
        Mockito.doReturn(HttpStatus.OK).when(responseEntityMock).getStatusCode();

    }

    @Test
    public void echo(){
        ResponseEntity result = moneyOrderController.echo(requestMock, bodyMock);
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void generateMoneyOrder_success() throws UnauthorizedException {
        Mockito.doNothing().when(moneyOrderController).authorize(requestMock);
        ResponseEntity result = moneyOrderController.generateMoneyOrder(requestMock, bodyMock);
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    public void generateMoneyOrder_404() throws UnauthorizedException {
        Mockito.doThrow(UnauthorizedException.class).when(moneyOrderController).authorize(requestMock);
        Mockito.doReturn(HttpStatus.FORBIDDEN).when(responseEntityMock).getStatusCode();
        ResponseEntity result = moneyOrderController.generateMoneyOrder(requestMock, bodyMock);
        Assert.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

    }

    @Test
    public void check_responseEntityMock_remains_200_after_the_previous_test_404() {
        System.out.println(responseEntityMock.getStatusCode());
    }

}
