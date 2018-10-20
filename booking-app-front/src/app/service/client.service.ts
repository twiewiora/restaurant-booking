import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs/Observable";
import {IClient} from "../model/client";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class ClientService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }

  getClient(id: number): Observable<IClient> {
    return this.http.get<IClient>(`${environment.baseUrl}/client${id}`);
  }
}
