package com.llwantedll.webhearts.models.validators;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomForm;
import com.llwantedll.webhearts.models.services.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class GameRoomValidator implements Validator {

    private static final int NAME_MINIMUM_CHARS = 4;
    private static final int NAME_MAXIMUM_CHARS = 16;
    private static final int PASSWORD_MINIMUM_CHARS = 0;
    private static final int PASSWORD_MAXIMUM_CHARS = 20;

    private static final String NAME_VALUE = "name";
    private static final String PASSWORD_VALUE = "password";

    private static final String REQUIRED_ERROR_CODE = "required";

    private final GameRoomService gameRoomService;

    @Autowired
    public GameRoomValidator(GameRoomService gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return GameRoomForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GameRoomForm gameRoomForm = (GameRoomForm) o;

        String name = gameRoomForm.getName();
        String password = gameRoomForm.getPassword();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME_VALUE, REQUIRED_ERROR_CODE);
        if (Objects.isNull(name) || name.length() < NAME_MINIMUM_CHARS || name.length() >= NAME_MAXIMUM_CHARS) {
            errors.rejectValue(NAME_VALUE, "game-room-form.name.size");
        }

        if (gameRoomService.isExistByName(name)) {
            errors.rejectValue(NAME_VALUE, "game-room-form.name.exists");
        }

        if (Objects.nonNull(password)) {
            if (password.length() < PASSWORD_MINIMUM_CHARS || password.length() >= PASSWORD_MAXIMUM_CHARS) {
                errors.rejectValue(PASSWORD_VALUE, "game-room-form.password.size");
            }
        }

        gameRoomService.isExistByName(gameRoomForm.getName());
    }
}
