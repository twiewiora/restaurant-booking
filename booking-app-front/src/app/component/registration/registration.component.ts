import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {AuthenticationService} from "../../service/authentication.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  usernameInput: string = '';
  usernameRepeatInput: string = '';
  passwordInput: string = '';
  passwordRepeatInput: string = '';
  errorMsg: string = '';
  private registrationForm: FormGroup;
  private MIN: number = 5;

  constructor(private authenticationService: AuthenticationService) {
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
        .subscribe();
    }
    else if(this.usernameInput !== this.usernameRepeatInput) {
      this.errorMsg = "Usernames don't match";
    }
    else{
      this.errorMsg = "Passwords don't match";
    }
  }

}
