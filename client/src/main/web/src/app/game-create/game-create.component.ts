import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import * as $ from 'jquery';
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-game-create',
  templateUrl: './game-create.component.html',
  styleUrls: ['./game-create.component.scss']
})
export class GameCreateComponent implements OnInit {

  userName: string;
  model: any = {};

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
  }

  ngOnInit() {
    let appComponent = new AppComponent(this.route, this.router, this.http);
    appComponent.checkAuthentication(this);
  }

  create() {
    let url = 'http://localhost:8080/create_room';
    this.http.post(url, {
        name: this.model.name,
        password: this.model.password,
        gameTitle: this.model.gameTitle,
      },
      {
        headers: {
          'Content-Type': "application/json",
          'Authorization': 'Basic ' + sessionStorage.getItem('token')
        }
      })
      .subscribe({
        next: () => this.router.navigate(['/game/' + this.model.name]),
        error: function (data) {
          let errors = data.error;
          let errorText = errors.error.split(",").join("<br>");
		  console.log(data);
          $(".error-msg-holder").html(
            "<div class='alert alert-danger alert-dismissible fade show' role='alert'>" +
            "  <strong>Errors!</strong> " + errorText +
            "  <button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
            "    <span aria-hidden='true'>&times;</span>" +
            "  </button>" +
            "</div>");
        }
      });
  }

}
