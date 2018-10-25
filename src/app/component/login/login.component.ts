import {Component, OnInit} from '@angular/core';
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
  public loginForm: FormGroup;

  constructor(private authenticationService: AuthenticationService,
              private http: HttpClient) {
  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      'username': new FormControl(this.usernameInput, [Validators.required]),
      'password': new FormControl(this.passwordInput, [Validators.required])
    });
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit() {
    this.authenticationService.login(this.usernameInput, this.passwordInput).subscribe(any => {
      },
      err => {
        this.errorAction(err.error);
      });
  }


  errorAction(msg: string) {
    this.errorMsg = msg;
    this.loginForm.reset({username: this.username.value});
  }

  cleanErrMsg() {
    this.errorMsg = '';
  }
}
