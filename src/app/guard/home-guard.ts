import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class HomeGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!localStorage.getItem('jwt')) {
      // logged in so return true
      return true;
    }
    if(localStorage.getItem('rest')){
      this.router.navigate(['/reservation'], { queryParams: { returnUrl: state.url }});
      return false;
    }
    else{
      this.router.navigate(['/initialize'], { queryParams: { returnUrl: state.url }});
      return false;
    }
  }
}
