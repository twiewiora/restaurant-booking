import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {of} from "rxjs/observable/of";
import {Observable} from "rxjs/Observable";
import {catchError} from "rxjs/operators";
import {IOpenHours, OpenHours} from "../model/open-hours";
import {environment} from "../../environments/environment";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class OpenHoursService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }


  getOpeningHoursForAllDays(): Observable<any> {
    return this.http.get<any>(`${environment.baseUrl}/openHours//all`);
  }

  getOpeningHoursForDay(day: string): Observable<IOpenHours> {
    return this.http.get<IOpenHours>(`${environment.baseUrl}/openHours/day=${day}`)
      // .pipe(
      //   catchError(this.handleError('getOpeningHoursForDay', )))
      // );
  }

// {"restaurantId": 2, "wednesday":["12:30:00", "22:30:00"], "monday":["12:30:00", "22:30:00"],  "tuesday":["12:30:00", "22:30:00"],  "thursday":["12:30:00", "22:30:00"],
//   "friday":["12:30:00", "22:30:00"],  "saturday":["12:30:00", "22:30:00"],  "sunday":["12:30:00", "22:30:00"]
// }

  updateOpenHours(json: string): Observable<OpenHours> {
    return this.http.post<any>(`${environment.baseUrl}//openHours/update`, json, options)
      .pipe(
        catchError(this.handleError('getOpeningHoursForAllDays', []))
      );
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // // TODO: better job of transforming error for user consumption
      // this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
