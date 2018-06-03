import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ITable, Table} from "../model/table";
import {catchError} from "rxjs/operators";
import {of} from "rxjs/observable/of";

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});
const options = {
  headers: headers,
};

@Injectable()
export class TableService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }

  getTables(id: number): Observable<ITable[]> {
    return this.http.get<ITable[]>(`/api/table/restaurantId=${id}`)
      .pipe(
        catchError(this.handleError('getTables', []))
      );
  }

  addTable(table: ITable): Observable<ITable> {
    return this.http.post<ITable>(`/api/table/add`, table.toJson(), options)
      .pipe(
        catchError(this.handleError('addTable', new Table()))
      );
  }

  deleteTable(table: ITable | number): Observable<ITable> {
    const id = typeof table === 'number' ? table : table.id;
    return this.http.delete<ITable>(`/api/table/delete/tableId=${id}`)
      .pipe(
        catchError(this.handleError('deleteTable', new Table()))
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
