import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  usernameInput: string = '';
  passwordInput: string = '';
  errorMsg: string = '';
  result: string;
  private loginForm: FormGroup;

  constructor(private authenticationService: AuthenticationService,
              private http: HttpClient) { }

  ngOnInit() {
    this.loginForm = new FormGroup({
      'username': new FormControl(this.usernameInput, [Validators.required]),
      'password': new FormControl(this.passwordInput, [Validators.required])});
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit() {
    this.authenticationService.login(this.usernameInput, this.passwordInput)
      .catch((err: any, caught: Observable<Response>) => {
      this.errorMsg = 'Failed to login';

      return Observable.throw(err);
    })
      .subscribe( _ => {

      });
  }
}
