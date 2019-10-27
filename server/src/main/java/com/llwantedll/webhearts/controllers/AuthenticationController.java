package com.llwantedll.webhearts.controllers;

import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.services.AuthenticationService;
import com.llwantedll.webhearts.models.utils.AuthUtils;
import com.llwantedll.webhearts.models.validators.UserValidator;
import com.llwantedll.webhearts.models.validators.ValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@CrossOrigin("http://localhost:4200")
public class AuthenticationController {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthenticationService authenticationService;

    private final UserValidator userValidator;

    private final ValidationErrorBuilder validationErrorBuilder;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    UserValidator userValidator,
                                    ValidationErrorBuilder validationErrorBuilder) {
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
        this.validationErrorBuilder = validationErrorBuilder;
    }

    @PostMapping(value = "/user", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public Principal user(HttpServletRequest request) {
        return () -> AuthUtils.getTokenFromHeader(request.getHeader(AUTHORIZATION_HEADER));
    }

    @PostMapping(value = "/register",
            consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestBody UserForm userForm,
                                   BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(validationErrorBuilder.getErrorsText(bindingResult));
        }

        authenticationService.register(userForm);

        return ResponseEntity.ok().build();
    }
}
