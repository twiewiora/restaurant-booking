import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {RestaurantInfoService} from "../service/restaurantInfo.service";

@Injectable()
export class InitialGuard implements CanActivate {

  constructor(private router: Router,
              private restaurantService: RestaurantInfoService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    debugger;
    if (localStorage.getItem('jwt')) {
      // logged in so return true
      this.restaurantService.getRestaurant().subscribe(_ => {
          this.router.navigate(['/reservation']);
          return false
        },
        _ => {
          return true;
        });

    }
    this.router.navigate(['/start'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
