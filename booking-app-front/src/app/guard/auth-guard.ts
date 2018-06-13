import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('jwt')) {
      if(localStorage.getItem('rest')) {
        return true
      }

      this.router.navigate(['/initialize']);
      return false;
    }

    // not logged in so redirect to login page with the return url
    this.router.navigate(['/start'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
