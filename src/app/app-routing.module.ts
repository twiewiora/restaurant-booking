import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from "./component/registration/registration.component";
import {LoginComponent} from "./component/login/login.component";
import {StartComponent} from "./component/start/start.component";
import {AuthGuard} from "./guard/auth-guard";
import {HomeGuard} from "./guard/home-guard";
import {ReservationPageComponent} from "./component/reservation/reservation-page/reservation-page.component";
import {RestaurantInfoPageComponent} from "./component/restaurant-info/restaurant-info-page/restaurant-info-page.component";
import {TablePageComponent} from "./component/table-setting/table-page/table-page.component";
import {InitialComponent} from "./component/initial/initial.component";
import {InitialGuard} from "./guard/initial-guard";

const routes: Routes = [
  {path: '', redirectTo: '/start', pathMatch: 'full'},
  {path: 'start', component: StartComponent, canActivate: [HomeGuard]},
  {path: 'login', component: LoginComponent, canActivate: [HomeGuard]},
  {path: 'registration', component: RegistrationComponent, canActivate: [HomeGuard]},
  {path: 'reservation', component: ReservationPageComponent, canActivate: [AuthGuard]},
  {path: 'tableSettings', component: TablePageComponent, canActivate: [AuthGuard]},
  {path: 'info', component: RestaurantInfoPageComponent, canActivate: [AuthGuard]},
  {path: 'initialize', component: InitialComponent, canActivate: [InitialGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
