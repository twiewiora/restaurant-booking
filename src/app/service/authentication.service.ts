import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../model/user";
import {Observable} from "rxjs/Rx";
import 'rxjs/add/operator/map';
import {RestaurantInfoService} from "./restaurantInfo.service";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class AuthenticationService {

  constructor(private _router: Router,
              private http: HttpClient,
              private restaurantService: RestaurantInfoService) {
  }

  logout() {
    localStorage.removeItem("jwt");
    if (localStorage.getItem("rest")) {
      localStorage.removeItem("rest");
    }
    this._router.navigate(['/start']);
  }

  login(username: string, password: string): Observable<Response> {
    const body = new User(username, password).toJSON();

    return this.http.post<Response>(`api/auth/`, body, options)
      .map((response: any) => {
        if (response.status === 403) {
          Observable.throw(response);
        }
        const token = response.token;
        localStorage.setItem("jwt", `${token}`);

        this.restaurantService.getRestaurant()
          .subscribe(
            data => {
              localStorage.setItem("rest", "ok");
              this._router.navigate(['/reservation']);
            },
            err =>{
              this._router.navigate(['/initialize']);
            });


        return response;
      });
  }

  register(user: User): Observable<User> {
    const body = user.toJSON();


    return this.http.post<User>(`api/auth/register`, body, options)
      .do(() => {
        this._router.navigate(['/start'])
      });
  }

  checkCredentials() {
    if (localStorage.getItem("jwt") === null) {
      this._router.navigate(['/start']);
    }
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('jwt') !== null;
  }
}
