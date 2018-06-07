import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Observer} from "rxjs/Observer";

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.scss']
})
export class TopBarComponent implements OnInit {
  result: string;

  time$ = new Observable<string>((observer: Observer<string>) => {
    setInterval(() => observer.next(new Date().toLocaleString()), 1000);
  });

  constructor(private authenticationService: AuthenticationService,
              private http: HttpClient) {
  }

  ngOnInit() {
  }

  public isLoggedInAsRestorer(): boolean{
    return this.authenticationService.isLoggedIn();
  }

  logout() {
    this.authenticationService.logout();
  }

}
