package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomDetailsWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomForm;
import com.llwantedll.webhearts.models.dtolayer.wrappers.PaginatedWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.*;
import com.llwantedll.webhearts.models.gameapi.entities.Game;
import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;
import com.llwantedll.webhearts.models.gameapi.entities.Player;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.repositories.GameRoomRepository;
import com.llwantedll.webhearts.models.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameRoomServiceImpl implements GameRoomService {

    private static final int ENTITIES_ON_PAGE = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRoomServiceImpl.class);

    private final GameRoomRepository gameRoomRepository;

    private final UserRepository userRepository;

    private final GameMapper gameMapper;

    private final BCryptPasswordEncoder encoder;

    private final DTOConverter<GameRoom, GameRoomDetailsWrapper> gameRoomDetailsDTOConverter;

    @Autowired
    public GameRoomServiceImpl(GameRoomRepository gameRoomRepository,
                               UserRepository userRepository, GameMapper gameMapper,
                               BCryptPasswordEncoder encoder,
                               DTOConverter<GameRoom, GameRoomDetailsWrapper> gameRoomDetailsDTOConverter) {
        this.gameRoomRepository = gameRoomRepository;
        this.userRepository = userRepository;
        this.gameMapper = gameMapper;
        this.encoder = encoder;
        this.gameRoomDetailsDTOConverter = gameRoomDetailsDTOConverter;
    }

    @Override
    public GameRoom create(GameRoomForm gameRoomForm) throws NoGameFoundException {
        GameRoom gameRoom = new GameRoom();

        gameRoom.setPassword(encoder.encode(gameRoomForm.getPassword()));
        gameRoom.setName(gameRoomForm.getName());

        GameDetails gameDetails = gameMapper.getGameDetailsByTitle(gameRoomForm.getGameTitle());

        gameRoom.setGameData(gameMapper.getGameInstanceByGameDetails(gameDetails));
        gameRoom.setMaxPlayers(gameDetails.getMaxPlayers());
        gameRoom.setMinPlayers(gameDetails.getMinPlayers());
        gameRoom.setStatus(GameStatus.WAITING.name());
        gameRoom.setStartedDate(new Date());

        return gameRoomRepository.save(gameRoom);
    }

    @Override
    public void join(GameRoom gameRoom, User name)
            throws UserAlreadyInGameRoomException, NoGameFoundException, FullRoomException {
        List<String> userIds = gameRoom.getUsers()
                .stream()
                .map(e -> e.getId().toString())
                .collect(Collectors.toList());

        if (userIds.contains(name.getId().toString())) {
            throw new UserAlreadyInGameRoomException();
        }

        getGame(gameRoom).addPlayer(name);
        gameRoom.setUser(name);

        name.setGameRoom(gameRoomRepository.save(gameRoom));
        userRepository.save(name);
    }

    @Override
    public void leaveUser(GameRoom gameRoom, User user) throws NoGameFoundException {

        String givenUserId = user.getId().toString();

        removeUserFromGameRoomArray(gameRoom, givenUserId);

        if (gameRoom.getUsers().isEmpty()) {
            gameRoom.setStatus(GameStatus.FINISHED.name());
        }

        getGame(gameRoom).removePlayer(user);

        gameRoomRepository.save(gameRoom);
    }

    private void removeUserFromGameRoomArray(GameRoom gameRoom, String givenUserId) {
        List<User> users = gameRoom.getUsers();

        Optional<User> first = users
                .stream()
                .filter(e -> e.getId().toString().equals(givenUserId))
                .findFirst();

        first.ifPresent(userFromSet -> gameRoom.getUsers().remove(userFromSet));
    }

    @Override
    public Game getGame(GameRoom gameRoom) throws NoGameFoundException {
        try {
            return (Game) gameRoom.getGameData();
        } catch (ClassCastException e) {
            throw new NoGameFoundException();
        }
    }

    @Override
    public void leaveAllByUser(User user) {
        List<GameRoom> gameRooms = user.getGameRooms();

        for (GameRoom gameRoom : gameRooms) {
            try {
                leaveUser(gameRoom, user);
            } catch (NoGameFoundException e) {
                LOGGER.warn("No game object in game room was found");
            }
        }
        user.setGameRooms(new ArrayList<>());
        userRepository.save(user);
    }

    @Override
    public GameRoom getByName(String name) {
        return gameRoomRepository.getByName(name);
    }

    @Override
    public boolean isExistByName(String name) {
        return gameRoomRepository.countByName(name) != 0;
    }

    @Override
    public List<GameRoom> getRoomsPage(int page) {
        PageRequest pagination = PageRequest.of(page - 1, ENTITIES_ON_PAGE, Sort.Direction.DESC, "startedDate");

        return gameRoomRepository.getOpenRooms(GameStatus.WAITING.name(), pagination);
    }

    @Override
    public long getOpenRoomsPagesCount() {
        return gameRoomRepository.getOpenRoomsCount(GameStatus.WAITING.name());
    }

    @Override
    public PaginatedWrapper<GameRoomDetailsWrapper> getOpenRoomsPaginated(int page) {
        PaginatedWrapper<GameRoomDetailsWrapper> gameRooms = new PaginatedWrapper<>();

        List<GameRoom> roomsPage = getRoomsPage(page);
        List<GameRoomDetailsWrapper> roomsPageWrappers = roomsPage.stream()
                .map(gameRoomDetailsDTOConverter::backward)
                .collect(Collectors.toList());

        gameRooms.setEntitiesCount(1 + ((getOpenRoomsPagesCount() - 1) / ENTITIES_ON_PAGE));
        gameRooms.setEntityPage(roomsPageWrappers);

        return gameRooms;
    }

    @Override
    public GameRoom saveGame(GameRoom gameRoom, Object serializedDetails) {
        gameRoom.setGameData(serializedDetails);
        return gameRoomRepository.save(gameRoom);
    }

    @Override
    public GameRoom saveRoom(GameRoom gameRoom) {
        return gameRoomRepository.save(gameRoom);
    }

    @Override
    public boolean isReadyToStart(GameRoom gameRoom) throws NoGameFoundException {
        Game game = getGame(gameRoom);

        List<Player> players = game.getPlayers();

        boolean allReady = players.stream().allMatch(Player::isReady);

        boolean moreThanMinPlayers = players.size() >= gameRoom.getMinPlayers();

        return allReady && moreThanMinPlayers;
    }
}
