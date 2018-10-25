import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {AuthenticationService} from "../../service/authentication.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  private options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };

  usernameInput: string = '';
  usernameRepeatInput: string = '';
  passwordInput: string = '';
  passwordRepeatInput: string = '';
  errorMsg: string = '';
  public registrationForm: FormGroup;
  private MIN: number = 5;

  constructor(private authenticationService: AuthenticationService,
              private notificationService: NotificationsService) {
  }

  ngOnInit() {
    this.registrationForm = new FormGroup({
      'username': new FormControl(this.usernameInput, [
        Validators.required
      ]),
      'username2': new FormControl(this.usernameRepeatInput, [
        Validators.required
      ]),
      'password': new FormControl(this.passwordInput, [Validators.required, Validators.minLength(this.MIN)]),
      'password2': new FormControl(this.passwordRepeatInput, [Validators.required, Validators.minLength(this.MIN)])
    });
  }

  get username() {
    return this.registrationForm.get('username');
  }

  get username2() {
    return this.registrationForm.get('username2');
  }

  get password() {
    return this.registrationForm.get('password');
  }

  get password2() {
    return this.registrationForm.get('password2');
  }


  onSubmit() {
    if (this.usernameInput === this.usernameRepeatInput && this.passwordInput === this.passwordRepeatInput) {
      this.authenticationService.register(new User(this.usernameInput, this.passwordInput))
        .subscribe(any => {
            this.notificationService.success("Registration successful", '', this.options);
          },
          err => {
            this.errorMsg = err.error;
            this.errorAction(err.error);
          });
    }
    else if (this.usernameInput !== this.usernameRepeatInput) {
      this.errorAction("Usernames don't match");
    }
    else {
      this.errorAction("Passwords don't match");
    }
  }

  errorAction(msg: string) {
    this.errorMsg = msg;
    this.registrationForm.reset({username: this.username.value, username2: this.username2.value});
  }

  cleanErrMsg(){
    this.errorMsg = '';
  }



}
