import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private http: HttpClient,
              private _notifications: NotificationsService) {
  }
}
