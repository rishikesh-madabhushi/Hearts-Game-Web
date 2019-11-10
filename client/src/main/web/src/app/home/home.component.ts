///<reference path="../app.component.ts"/>
import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {ActivatedRoute, Router} from "@angular/router";
import * as $ from 'jquery';
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  userName: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    let appComponent = new AppComponent(this.route, this.router, this.http);

    appComponent.checkAuthentication(this);
  };

  logout() {
    sessionStorage.setItem('token', '');
  }

}
