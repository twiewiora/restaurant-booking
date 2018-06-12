import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {of} from "rxjs/observable/of";
import {Observable} from "rxjs/Observable";
import {catchError} from "rxjs/operators";
import {ITimeTable, OpenDay, TimeTable} from "../model/timetable";
import {WeekDay} from "@angular/common";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class TimeTableService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }


  getOpeningHoursForAllDays(restaurantId: number): Observable<any> {
    return this.http.get<any>(`/api/openHours/restaurantId=${restaurantId}/all`)
      .pipe(
        catchError(this.handleError('getOpeningHoursForAllDays', [])));
  }

  getOpeningHoursForDay(restaurantId: number, day: string): Observable<OpenDay> {
    return this.http.get<OpenDay>(`/api/openHours/restaurantId=${restaurantId}/day=${day}`)
      .pipe(
        catchError(this.handleError('getOpeningHoursForDay', new OpenDay()))
      );
  }

  updateOpenHours(restaurantId: number, json: string): Observable<any> {
    return this.http.post<any>(`/api/openHours/update`, json, options)
      .pipe(
        catchError(this.handleError('getOpeningHoursForAllDays', new TimeTable()))
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