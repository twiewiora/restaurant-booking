import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ITable, Table} from "../model/table";
import {catchError} from "rxjs/operators";
import {of} from "rxjs/observable/of";
import {environment} from "../../environments/environment";

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

  getTables(): Observable<ITable[]> {
    return this.http.get<ITable[]>(`${environment.baseUrl}/tables`);
  }

  searchBestFreeTables(date: string, duration: number, places: number): Observable<ITable[]> {
    return this.http.get<ITable[]>(`${environment.baseUrl}/tables/search?date=${date}&length=${duration}&places=${places}`)
      .pipe(
        catchError(this.handleError('getTables', []))
      );
  }

  searchAllFreeTables(date: string, duration: number): Observable<ITable[]> {
    return this.http.get<ITable[]>(`${environment.baseUrl}/freeTables?date=${date}&length=${duration}`)
      .pipe(
        catchError(this.handleError('getTables', []))
      );
  }

  addTable(table: ITable): Observable<ITable> {
    return this.http.post<ITable>(`${environment.baseUrl}/table/add`, table.toJson(), options)
      .pipe(
        catchError(this.handleError('addTable', new Table()))
      );
  }

  updateTable(table: ITable): Observable<ITable> {
    return this.http.post<ITable>(`${environment.baseUrl}/table/update`, table.toJson(), options)
      .pipe(
        catchError(this.handleError('updateTable', new Table()))
      );
  }

  deleteTable(table: ITable | number): Observable<ITable> {
    const id = typeof table === 'number' ? table : table.id;
    return this.http.delete<ITable>(`${environment.baseUrl}/table/delete/tableId=${id}`)
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
