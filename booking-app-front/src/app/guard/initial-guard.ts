import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

@Injectable()
export class InitialGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('jwt')) {
      if(!localStorage.getItem('rest')){
        return true;
      }

      this.router.navigate(['/reservation']);
      return false;

    }
    this.router.navigate(['/start'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
