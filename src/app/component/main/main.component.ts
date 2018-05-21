import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  result: string;

  constructor(private authenticationService: AuthenticationService,
              private http: HttpClient) { }

  ngOnInit() {
  }

  private sayHello(): void {
    this.result = 'loading...';
    this.http.get('/api/hello-world').subscribe((response: any) => {
      this.result = response.message
    });
  }

  logout(){
    this.authenticationService.logout();
  }

}
