package com.llwantedll.webhearts.controllers;

import com.google.gson.Gson;
import com.llwantedll.webhearts.models.configs.ConfigurationData;
import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomDetailsWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomForm;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.PaginatedWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.gameapi.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.services.AuthenticationService;
import com.llwantedll.webhearts.models.services.GameRoomService;
import com.llwantedll.webhearts.models.validators.GameRoomValidator;
import com.llwantedll.webhearts.models.validators.ValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@RestController
@CrossOrigin(ConfigurationData.CROSS_ORIGIN_URL)
public class GameRoomsController {

    private final GameRoomService gameRoomService;

    private final AuthenticationService authenticationService;

    private final DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter;
    private final DTOConverter<GameRoom, GameRoomDetailsWrapper> gameRoomDetailsDTOConverter;

    private final ValidationErrorBuilder validationErrorBuilder;

    private final GameRoomValidator gameRoomValidator;

    @Autowired
    public GameRoomsController(GameRoomService gameRoomService,
                               AuthenticationService authenticationService,
                               DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter,
                               DTOConverter<GameRoom, GameRoomDetailsWrapper> gameRoomDetailsDTOConverter,
                               ValidationErrorBuilder validationErrorBuilder,
                               GameRoomValidator gameRoomValidator) {
        this.gameRoomService = gameRoomService;
        this.authenticationService = authenticationService;
        this.gameRoomDTOConverter = gameRoomDTOConverter;
        this.gameRoomDetailsDTOConverter = gameRoomDetailsDTOConverter;
        this.validationErrorBuilder = validationErrorBuilder;
        this.gameRoomValidator = gameRoomValidator;
    }

    @PostMapping(value = "/create_room",
            consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity createRoom(@RequestBody GameRoomForm gameRoomForm, BindingResult bindingResult) {
        GameRoom gameRoom;

        gameRoomValidator.validate(gameRoomForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrorBuilder.getErrorsText(bindingResult));
        }

        try {
            gameRoom = gameRoomService.create(gameRoomForm);
        } catch (NoGameFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new Gson().toJson(gameRoomDTOConverter.backward(gameRoom)));
    }

    @GetMapping("/get_rooms")
    public ResponseEntity getRoomPage(@RequestParam("page") String page) {
        try {
            int pageDigit = Integer.parseInt(page);
            PaginatedWrapper<GameRoomDetailsWrapper> openRoomsPaginated = gameRoomService.getOpenRoomsPaginated(pageDigit);
            return ResponseEntity.ok(new Gson().toJson(openRoomsPaginated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/join_room")
    public ResponseEntity joinRoom(@RequestParam("roomName") String roomName)
            throws UserPrincipalNotFoundException, UserAlreadyInGameRoomException, FullRoomException, NoGameFoundException {
        GameRoom gameRoom = gameRoomService.getByName(roomName);

        if (Objects.isNull(gameRoom)) {
            return ResponseEntity.badRequest().build();
        }

        gameRoomService.join(gameRoom, authenticationService.getRemoteUser());

        return ResponseEntity.ok().build();
    }

}
