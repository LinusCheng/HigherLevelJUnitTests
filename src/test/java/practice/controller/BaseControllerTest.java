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
import practice.constant.OperationRole;
import practice.constant.SessionConstant;
import practice.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@PrepareForTest({BaseController.class, Thread.class})
@RunWith(PowerMockRunner.class)
public class BaseControllerTest {

    private BaseController baseController;

    @Mock
    HttpServletRequest reqMock;
    @Mock
    HttpSession sessionMock;
    @Mock
    Thread threadMock;
    StackTraceElement stackTraceElementMock;
    StackTraceElement[] stackTraceElementArrayMock;

    @Before
    public void setup() {
        baseController = new BaseController();
        Mockito.doReturn(sessionMock).when(reqMock).getSession();
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.when(Thread.currentThread()).thenReturn(threadMock);

        stackTraceElementMock = new StackTraceElement(
                "practice.controller.MoneyOrderController",
                "generateMoneyOrder",
                "",
                1000
        );
        stackTraceElementArrayMock = new StackTraceElement[3];
        Arrays.fill(stackTraceElementArrayMock, stackTraceElementMock);
        Mockito.doReturn(stackTraceElementArrayMock).when(threadMock).getStackTrace();

    }

    @Test
    public void authorize_admin_success() throws UnauthorizedException {
        Mockito.doReturn(OperationRole.OP_ADMIN).when(sessionMock).getAttribute(SessionConstant.OP_ROLE);
        Mockito.doReturn("a1507293").when(sessionMock).getAttribute(SessionConstant.USER_ID);
        baseController.authorize(reqMock);
    }

    @Test(expected = UnauthorizedException.class)
    public void authorize_viewer_fail() throws UnauthorizedException {
        Mockito.doReturn(OperationRole.OP_VIEWER).when(sessionMock).getAttribute(SessionConstant.OP_ROLE);
        Mockito.doReturn("a1507293").when(sessionMock).getAttribute(SessionConstant.USER_ID);
        baseController.authorize(reqMock);
    }


    @Test(expected = UnauthorizedException.class)
    public void authorize_fail() throws UnauthorizedException {
        Mockito.doReturn(OperationRole.OP_VIEWER).when(sessionMock).getAttribute(SessionConstant.OP_ROLE);
        baseController.authorize(reqMock);
    }


    @Test
    public void exceptionCasesReturn() {
        ResponseEntity re1 = baseController.exceptionCasesReturn(new UnauthorizedException(""));
        ResponseEntity re2 = baseController.exceptionCasesReturn(new Exception());
        Assert.assertEquals(HttpStatus.FORBIDDEN, re1.getStatusCode());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, re2.getStatusCode());

    }


}
