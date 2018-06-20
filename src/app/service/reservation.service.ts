import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {of} from "rxjs/observable/of";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {IReservation, Reservation} from "../model/reservation";
import {environment} from "../../environments/environment";


const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class ReservationService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }



  addReservation(reservation: IReservation): Observable<IReservation> {
    return this.http.post<any>(`${environment.baseUrl}/reservation/add`, reservation.toJson(), options)
      .pipe(
        catchError(this.handleError('getReservation', new Reservation()))
      );
  }

  cancelReservation(reservation: IReservation): Observable<IReservation> {
    return this.http.post<IReservation>(`${environment.baseUrl}/reservation/cancel/reservationId=${reservation.id}`, [], options)
      .pipe(
        catchError(this.handleError('deleteReservation', new Reservation()))
      );
  }

  deleteReservation(reservation: IReservation): Observable<IReservation> {
    return this.http.delete<IReservation>(`${environment.baseUrl}/reservation/delete/reservationId=${reservation.id}`)
      .pipe(
        catchError(this.handleError('deleteReservation', new Reservation()))
      );
  }

  getReservationsForOneTable(tableId: number, dateFrom: string, dateTo: string): Observable<IReservation[]> {
    return this.http.get<IReservation[]>(`${environment.baseUrl}/reservation/forTable/tableId=${tableId}/dateFrom=${dateFrom}/dateTo=${dateTo}`)
      .pipe(
        catchError(this.handleError('getReservationsForOneTable', []))
      );
  }

  getReservationsForAllTables(dateFrom: string, dateTo: string): Observable<IReservation[]> {
    return this.http.get<IReservation[]>(`${environment.baseUrl}/reservation/all/dateFrom=${dateFrom}/dateTo=${dateTo}`)
      .pipe(
        catchError(this.handleError('getReservationsForOneTable', []))
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
