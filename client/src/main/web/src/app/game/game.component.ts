import {Component, OnInit} from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  private stompClient = null;

  players: any;
  userName: string;
  roomName: string;
  minimumPlayers: number;
  maximumPlayers: number;
  currentPlayers: number;
  gameTitle: string;
  status: string;
  gameData: any;
  canvasProperties: any;
  private playerDrawCoords: any;
  private cardSize: any = {
    width: 60,
    height: 100
  };

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {

    this.canvasProperties = {
      width: 800,
      height: 800
    };

    this.playerDrawCoords = {
      remote: {
        x: this.canvasProperties.width / 5,
        y: 4 * this.canvasProperties.height / 5
      },
      first: {
        x: this.canvasProperties.width / 5 - this.cardSize.width,
        y: this.canvasProperties.height / 2 - (this.cardSize.height / 2)
      },
      second: {
        x: this.canvasProperties.width / 2 - (this.cardSize.width / 2),
        y: (this.canvasProperties.height / 5) - (this.cardSize.height / 2)
      },
      third: {
        x: 4 * this.canvasProperties.width / 5,
        y: this.canvasProperties.height / 2 - (this.cardSize.height / 2)
      }
    };
  }

  ngOnInit() {
    let appComponent = new AppComponent(this.route, this.router, this.http);
    appComponent.checkAuthentication(this);

    const baseUrl = "http://localhost:8080";

    this.roomName = this.route.snapshot.paramMap.get('roomName');

    const gameRoomInfoUrl = baseUrl + "/game_room/" + this.roomName + "/get";

    const _this = this;

    const socket = new SockJS(baseUrl + "/messages", null,
      {
        headers: {
          'Authorization': 'Basic ' + sessionStorage.getItem('token')
        }
      }
    );

    socket.onclose = function () {
      this.stompClient.disconnect();
    };

    this.stompClient = Stomp.Stomp.over(socket);

    this.stompClient.connect({
      roomName: _this.roomName,
      authToken: 'Basic ' + sessionStorage.getItem('token')
    }, function (frame) {

      _this.updateGameInfo(_this, gameRoomInfoUrl);

      _this.stompClient.subscribe('/topic/message/' + _this.roomName, function (game) {
        _this.updateGameInfo(_this, gameRoomInfoUrl);
        _this.updateGameField(_this);
      });

      _this.stompClient.subscribe('/topic/message/' + _this.roomName + "/workflow", function (game) {
        _this.updateGameInfo(_this, gameRoomInfoUrl);
        _this.workflowChange(_this, game.body);
      });

      _this.stompClient.subscribe('/topic/message/global/update', function (data) {
        if (data) {
          _this.updateGameInfo(_this, gameRoomInfoUrl);
        }
      });
    });

    $("body").on("unload", function () {
      this.disconnect({roomName: _this.roomName});
    });
  }

  private workflowChange(_this: GameComponent, state: string) {
    switch (state) {
      case "STARTED":
        _this.updateGameField(_this);
        break;
    }
  }

  private updateGameField(_this: GameComponent) {
    let gameCanvas: any = document.getElementById("game-canvas");

    let ctx = gameCanvas.getContext("2d");
    ctx.fillStyle = "#FFFFEE";
    ctx.fillRect(0, 0, _this.canvasProperties.width, _this.canvasProperties.height);

    let cardDeckCount = 0;

    $(_this.gameData.cardDeck.cards).each(function (e: any, d: any) {
      _this.drawCard(_this, ctx, 50 + cardDeckCount, 50 + cardDeckCount, d);
      cardDeckCount++;
    });

    let currentUserArrayPosition = 1;

    for (let i = 0; i < _this.currentPlayers; i++) {
      if (_this.players[i].login == _this.userName) {
        currentUserArrayPosition = i;
        break;
      }
    }

    _this.drawPlayerCards(_this, ctx, _this.playerDrawCoords.remote.x,
      _this.playerDrawCoords.remote.y,
      _this.gameData.players[currentUserArrayPosition].cards);
    _this.drawPlayerCards(_this, ctx, _this.playerDrawCoords.first.x,
      _this.playerDrawCoords.first.y,
      _this.gameData.players[(currentUserArrayPosition + 1) % _this.currentPlayers].cards);
    _this.drawPlayerCards(_this, ctx, _this.playerDrawCoords.second.x,
      _this.playerDrawCoords.second.y,
      _this.gameData.players[(currentUserArrayPosition + 2) % _this.currentPlayers].cards);
    _this.drawPlayerCards(_this, ctx, _this.playerDrawCoords.third.x,
      _this.playerDrawCoords.third.y,
      _this.gameData.players[(currentUserArrayPosition + 3) % _this.currentPlayers].cards);
  }

  private drawPlayerCards(_this, ctx, xSpot, ySpot, cards) {
    $(cards).each(function (e, d) {
      _this.drawCard(_this, ctx, xSpot, ySpot, d);
    });
  }

  private drawCard(_this, ctx, x, y, card) {
    let cardDeckImage = new Image();

    cardDeckImage.height = _this.cardSize.height;
    cardDeckImage.width = _this.cardSize.width;

    cardDeckImage.src = "assets/images/cards/" + card.standardCardRank + "_" + card.standardCardSuit + ".png";

    cardDeckImage.onload = function () {
      ctx.drawImage(cardDeckImage, x, y, _this.cardSize.width, _this.cardSize.height);
    };
  }

  private updateStatusContainer(status) {
    $(".game-phase-container").hide();

    switch (status) {
      case "WAITING":
        $(".preparation-game-room-phase").show();
        break;
      case "STARTED":
        $(".started-game-room-phase").show();
        break;
      default:
        $(".finished-game-room-phase").show();
        break;
    }
  }

  private updateGameInfo(_this: GameComponent, gameRoomInfoUrl: string) {
    let options = {
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Basic ' + sessionStorage.getItem('token')
      }
    };

    _this.http.get(gameRoomInfoUrl, options).subscribe({
      next: function (data: any) {
        console.log(data);
        _this.players = data.gameData.players;
        _this.minimumPlayers = data.minPlayers;
        _this.maximumPlayers = data.maxPlayers;
        _this.currentPlayers = data.gameData.players.length;
        _this.gameTitle = data.gameData.gameDetails.gameTitle;
        _this.status = data.status;
        _this.gameData = data.gameData;
        _this.updateStatusContainer(data.status);
        _this.updateGameField(_this);
      },
      error: function (data) {
        console.log(data);
      }
    });
  }

  disconnect() {

    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    console.log("Disconnected");
  }

  getReady() {
    this.stompClient.send(
      '/app/game_room/' + this.roomName + "/ready", {
        ready: $("#game-room-ready-checkbox").prop("checked") ? "true" : "false"
      });
  }

}
