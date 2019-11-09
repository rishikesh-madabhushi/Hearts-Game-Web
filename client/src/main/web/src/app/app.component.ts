import { Component } from '@angular/core';
import * as $ from 'jquery';
import {Observable} from "rxjs/index";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'web';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  checkAuthentication(handler){
    let url = 'http://localhost:8080/user';

    let tokenItem = sessionStorage.getItem('token');

    if (!tokenItem) {
      this.router.navigate(['/login']);
      return;
    }

    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + tokenItem
    });

    let options = {headers: headers};
    handler.http.post(url, {}, options)
      .subscribe({
        next: e => handler.userName = e.name,
        error: function (error) {
          if (error.status == 401) {
            handler.router.navigate(['/login']);
          }
        }
      });

    return headers;
  }

  static renderPrevious(currentPage, action) {
    if (currentPage > 1) {
      let link = $("<a class='page-link' href='#'> Previous </a>");
      link.on("click", {pageNum: currentPage - 1}, action);
      return $("<li></li>").addClass("page-item").append(link);
    } else {
      return null;
    }
  }

  static renderNext(currentPage, lastPage, action) {
    if (currentPage < lastPage) {
      let link = $("<a class='page-link' href='#'> Next </a>");
      link.on("click", {pageNum: currentPage + 1}, action);
      return $("<li></li>").addClass("page-item").append(link);
    } else {
      return null;
    }
  }

  static renderPageEntry(index, currentPage, action) {
    if (index === currentPage) {
      return "<li class='page-item active'><a class='page-link'>" + index + "</a></li>";
    } else {
      let link = $("<a class='page-link' href='#'>" + index + "</a>");
      link.on("click", {pageNum: index}, action);
      return $("<li></li>").addClass("page-item").append(link);
    }
  }

  static renderTruncation() {
    let link = $("<a class='page-link disabled'>&hellip;</a>");
    return $("<li></li>").addClass("page-item").append(link);
  }

  static pagination(currentPage, lastPage, action) {
    let paginator = $("<ul></ul>").addClass("pagination").addClass("content-block");

    paginator.append(AppComponent.renderPrevious(currentPage, action));

    let left = Math.max(currentPage - 2, 1);
    let right = Math.min(currentPage + 2, lastPage);

    if (left > 1) {
      paginator.append(AppComponent.renderTruncation());
    }

    for (let i = left; i < right + 1; i++) {
      paginator.append(AppComponent.renderPageEntry(i, currentPage, action));
    }

    if (right < lastPage) {
      paginator.append(AppComponent.renderTruncation());
    }

    paginator.append(AppComponent.renderNext(currentPage, lastPage, action));

    return paginator;
  }

}
