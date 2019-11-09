package com.llwantedll.webhearts.models.validators;

import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.services.UserService;
import com.llwantedll.webhearts.models.utils.FormValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class UserValidator implements Validator {

    private static final int LOGIN_MINIMUM_CHARS = 4;
    private static final int LOGIN_MAXIMUM_CHARS = 16;
    private static final int PASSWORD_MINIMUM_CHARS = 4;
    private static final int PASSWORD_MAXIMUM_CHARS = 20;

    private static final String LOGIN_VALUE = "login";
    private static final String EMAIL_VALUE = "email";
    private static final String PASSWORD_VALUE = "password";
    private static final String CONFIRM_PASSWORD_VALUE = "confirmPassword";

    private static final String REQUIRED_ERROR_CODE = "required";

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm user = (UserForm) o;

        String login = user.getLogin();
        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();

        //USER LOGIN VALIDATE
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, LOGIN_VALUE, REQUIRED_ERROR_CODE);
        if (Objects.isNull(login) || login.length() < LOGIN_MINIMUM_CHARS || login.length() >= LOGIN_MAXIMUM_CHARS) {
            errors.rejectValue(LOGIN_VALUE, "user-form.login.size");
        }

        //IS USER LOGIN NOT EXISTS VALIDATE
        if(userService.isLoginExists(login)){
            errors.rejectValue(LOGIN_VALUE, "user-form.login.exist");
        }

        //USER EMAIL VALIDATE
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL_VALUE, REQUIRED_ERROR_CODE);
        if(!FormValidationUtils.validate(user.getEmail())) {
            errors.rejectValue(EMAIL_VALUE, "user-form.email.pattern");
        }

        //USER PASSWORD VALIDATE
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD_VALUE, REQUIRED_ERROR_CODE);
        if(Objects.isNull(password) || password.length() < PASSWORD_MINIMUM_CHARS || password.length() >= PASSWORD_MAXIMUM_CHARS){
            errors.rejectValue(PASSWORD_VALUE, "user-form.password.size");
        }

        //CONFIRM PASSWORD VALIDATE
        if(Objects.isNull(confirmPassword) || !password.equals(confirmPassword)){
            errors.rejectValue(CONFIRM_PASSWORD_VALUE, "user-form.password.different");
        }
    }
}
