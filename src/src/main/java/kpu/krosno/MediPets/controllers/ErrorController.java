package kpu.krosno.MediPets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model)
    {
        model.addAttribute("error_request_url", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        model.addAttribute("error_status_code", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        model.addAttribute("error_exception", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        model.addAttribute("error_exception_type", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
        model.addAttribute("error_message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("error_status_code_name", HttpStatus.valueOf(statusCode).name());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error";
            }
        }
        return "error";
    }
}
