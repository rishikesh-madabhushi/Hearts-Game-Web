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
  strings: string[] = [];
  roomName: string;
  minimumPlayers: number;
  maximumPlayers: number;
  currentPlayers: number;
  gameTitle: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
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
