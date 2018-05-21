import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor() {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (!request.url.includes('api/auth')) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
      });
    }

    return next.handle(request);
  }
}
