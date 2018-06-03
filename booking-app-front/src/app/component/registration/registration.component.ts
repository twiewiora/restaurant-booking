import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {AuthenticationService} from "../../service/authentication.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  user: User = new User('', '');

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
  }

  onSubmit() {

    this.authenticationService.register(this.user)
      .subscribe();

  }

}
