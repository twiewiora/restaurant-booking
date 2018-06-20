import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {of} from "rxjs/observable/of";
import {Observable} from "rxjs/Observable";
import {catchError} from "rxjs/operators";
import {IRestaurant, Restaurant} from "../model/restaurant";
import {environment} from "../../environments/environment";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class RestaurantInfoService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }

  getRestaurant(): Observable<IRestaurant> {
    return this.http.get<IRestaurant>(`${environment.baseUrl}/restaurant`)
      .pipe(
        catchError(this.handleError('getRestaurant', new Restaurant()))
      );
  }

  getAllRestaurants(): Observable<IRestaurant[]> {
    return this.http.get<IRestaurant[]>(`${environment.baseUrl}/restaurant/list`)
      .pipe(
        catchError(this.handleError('getAllRestaurants', []))
      );
  }

  getRestaurantFreeDates(restaurant: IRestaurant, data: Date): Observable<IRestaurant[]> {
    return this.http.get<IRestaurant[]>(`${environment.baseUrl}/restaurant${restaurant.id}/list`)
      .pipe(
        catchError(this.handleError('getAllRestaurants', []))
      );
  }

  createRestaurant(restaurant: IRestaurant): Observable<IRestaurant> {
    return this.http.post<IRestaurant>(`${environment.baseUrl}/restaurant/add`, Restaurant.toJson(restaurant), options)
      .pipe(
        catchError(this.handleError('createRestaurant', new Restaurant()))
      );
  }


  updateRestaurant(restaurant: IRestaurant): Observable<Restaurant> {
    return this.http.post<Restaurant>(`${environment.baseUrl}/restaurant/update`, Restaurant.toJson(restaurant), options)
      .pipe(
        catchError(this.handleError('updateRestaurant', new Restaurant()))
      );
  }

  getTags(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.baseUrl}/tags`)
      .pipe(
        catchError(this.handleError('getTags', []))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // // TODO: better job of transforming error for user consumption
      // this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return error;
    };
  }
}
