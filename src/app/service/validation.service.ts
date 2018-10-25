import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class ValidationService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }

  validate(): Observable<any> {
    return this.http.get<any>(`${environment.baseUrl}/restorer/validate`);
  }
}
