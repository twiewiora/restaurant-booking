import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';
  errorMsg: string = '';
  result: string;

  constructor(private authenticationService: AuthenticationService,
              private http: HttpClient) { }

  ngOnInit() {
  }

  onSubmit() {
    this.authenticationService.login(this.username, this.password)
      .catch((err: any, caught: Observable<Response>) => {
      this.errorMsg = 'Failed to login';

      return Observable.throw(err);
    })
      .subscribe();
  }
}
