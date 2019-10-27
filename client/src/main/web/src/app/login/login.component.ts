import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    if(sessionStorage.getItem("token") !== ''){
      this.router.navigate(['']);
      return;
    }

    sessionStorage.setItem('token', '');
  }

  login(){
    let url = 'http://localhost:8080/login';

    let params = new URLSearchParams();
    params.set("login", this.model.username);
    params.set("password", this.model.password);

    this.http.post<Observable<boolean>>(url, params.toString(),
      {headers: {'Content-Type': "application/x-www-form-urlencoded; charset=UTF-8"}})
      .subscribe(isValid => {
      if(isValid){
        sessionStorage.setItem('token', btoa(this.model.username + ":" + this.model.password));
        this.router.navigate(['']);
      } else {
        alert("Authentication failed");
      }
    })
  }

}
