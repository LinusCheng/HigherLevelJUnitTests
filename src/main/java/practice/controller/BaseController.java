package practice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import practice.constant.AuthorizedRole;
import practice.constant.OperationRole;
import practice.constant.SessionConstant;
import practice.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
public class BaseController {

    protected void authorize(HttpServletRequest req) throws UnauthorizedException {

        try {
            // StackTraceElement of the extended controller
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            System.out.println(ste.getClassName());
            System.out.println(ste.getMethodName());

            Method[] allMethods = Class.forName(ste.getClassName()).getMethods();
            Method calledMethods = Arrays.stream(allMethods).filter(m -> m.getName().equals(ste.getMethodName())).findFirst().get();

            AuthorizedRole authorizedRoles = calledMethods.getAnnotation(AuthorizedRole.class);
            OperationRole opRoleInSession = OperationRole.parseString2Enum(req.getSession().getAttribute(SessionConstant.OP_ROLE).toString());

            String userId = req.getSession().getAttribute(SessionConstant.USER_ID).toString();

            if (authorizedRoles.opRole().length>0) {
                List<OperationRole> roles =  Arrays.asList(authorizedRoles.opRole());
                if (!roles.contains(opRoleInSession)) {
                    throw new UnauthorizedException("User: " + userId + " is not authorized with this endpoint");
                }
            }
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                throw new UnauthorizedException(e.getMessage());
            } else {
                throw new UnauthorizedException("Internal error during authorization", e);
            }

        }

    }



    protected ResponseEntity exceptionCasesReturn(Exception e) {
        if (e instanceof UnauthorizedException) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
