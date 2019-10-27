import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  userName: string;

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private router: Router) {}

  ngOnInit() {
    let url = 'http://localhost:8080/user';

    let tokenItem = sessionStorage.getItem('token');

    if(!tokenItem){
      this.router.navigate(['/login']);
      return;
    }

    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + tokenItem
    });

    let options = { headers: headers };
    this.http.post<Observable<Object>>(url, {}, options).
    subscribe(principal => {
        this.userName = principal['name'];
      },
      error => {
        if(error.status == 401) {
          this.router.navigate(['/login']);
        }

      }
    );
  }

  logout() {
    sessionStorage.setItem('token', '');
  }

}
