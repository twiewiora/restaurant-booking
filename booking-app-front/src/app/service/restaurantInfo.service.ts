import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs/observable/of";
import {Observable} from "rxjs/Observable";
import {catchError} from "rxjs/operators";
import {ITable, Table} from "../model/table";
import {IRestaurant, Restaurant} from "../model/restaurant";

@Injectable()
export class RestaurantInfoService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }


  createRestaurant(restaurant: IRestaurant): Observable<IRestaurant> {
    return this.http.post<IRestaurant>(`/api/restaurant/add`, restaurant.toJson())
      .pipe(
        catchError(this.handleError('createRestaurant', new Restaurant()))
      );
  }

  updateRestaurant(restaurant: IRestaurant): Observable<IRestaurant> {
    return this.http.post<IRestaurant>(`/api/restaurant/update`, restaurant.toJson())
      .pipe(
        catchError(this.handleError('updateRestaurant', new Restaurant()))
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
