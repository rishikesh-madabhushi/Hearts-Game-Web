import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {AppComponent} from "../app.component";
import * as $ from 'jquery';

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.scss']
})



export class GameSearchComponent implements OnInit {
  entitiesCount: number;
  entityPage: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
  }

  ngOnInit() {
    this.loadPage(1);
  }

  loadPage(page) {
    let url = 'http://localhost:8080/get_rooms';

    const params = new HttpParams().set('page', page);
    const options = {params: params};

    let _this = this;

    this.http.get(url, options).subscribe({
      next: function (data: any) {
        $(".game-search-pagination-navigator")
          .html(AppComponent.pagination(page, data.entitiesCount, this.goto).get(0));
        _this.entityPage = data.entityPage;
        _this.entitiesCount = data.entitiesCount;
      },
      error: function (data) {
        console.log(data);
      }
    });
  }

  goto(event) {
    this.loadPage(event.data.pageNum);
  }

}
