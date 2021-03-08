package practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.constant.AuthorizedRole;
import practice.constant.OperationRole;
import practice.service.FetchDataService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MoneyOrderController extends BaseController{

    @Autowired
    FetchDataService fetchDataService;

    @PostMapping("/echo")
    public ResponseEntity echo(HttpServletRequest request, @RequestBody Object body) {
        return new ResponseEntity("Good", HttpStatus.OK);
    }


    @AuthorizedRole(opRole = {OperationRole.OP_ADMIN, OperationRole.OP_EXECUTE})
    @PostMapping("/moneyOrder")
    public ResponseEntity generateMoneyOrder(HttpServletRequest request, @RequestBody Object body)  {

        try {
            String secureToken = request.getHeader("secureToken");
            String amount = request.getHeader("amount");
            authorize(request);
            return new ResponseEntity(fetchDataService.fetchMoneyOrderCode(secureToken, amount), HttpStatus.OK);
        } catch (Exception e) {
            return exceptionCasesReturn(e);
        }
    }

}
