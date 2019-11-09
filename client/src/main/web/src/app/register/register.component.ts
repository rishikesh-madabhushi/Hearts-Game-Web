import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs/index";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import * as $ from 'jquery';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  model: any = {};

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
  }

  ngOnInit() {

  }

  register() {
    let url = 'http://localhost:8080/register';

    this.http.post<Observable<boolean>>(url, {
        login: this.model.username,
        password: this.model.password,
        confirmPassword: this.model.confirmPassword,
        email: this.model.email
      },
      {headers: {'Content-Type': "application/json"}})
      .subscribe({
        next: () => this.router.navigate(['']),
        error: (data) => $(".error-msg-holder").html(
          "<div class='alert alert-danger alert-dismissible fade show' role='alert'>" +
          "  <strong>Errors!</strong> " + data.error.split(",").join("<br>") +
          "  <button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
          "    <span aria-hidden='true'>&times;</span>" +
          "  </button>" +
          "</div>")
      });
  }

}
