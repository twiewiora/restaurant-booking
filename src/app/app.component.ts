import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  result = '';
  result2 = '';

  constructor(private http: HttpClient) {}

  private sayHello(): void {
    this.result = 'loading...';
    this.http.get('/api/hello-world').subscribe((response: any) => {
      this.result = response.message
    });
  }


}