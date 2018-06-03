import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from "./component/registration/registration.component";
import {LoginComponent} from "./component/login/login.component";
import {MainComponent} from "./component/main/main.component";
import {StartComponent} from "./component/start/start.component";
import {AuthGuard} from "./guard/auth-guard";
import {HomeGuard} from "./guard/home-guard";
import {ReservationPageComponent} from "./component/reservation/reservation-page/reservation-page.component";
import {RestaurantInfoPageComponent} from "./component/restaurant-info/restaurant-info-page/restaurant-info-page.component";
import {TablePageComponent} from "./component/table-setting/table-page/table-page.component";

const routes: Routes = [
  {path: '', redirectTo: '/start', pathMatch: 'full'},
  {path: 'start', component: StartComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'reservation', component: ReservationPageComponent, canActivate: [AuthGuard]},
  {path: 'tableSettings', component: TablePageComponent, canActivate: [AuthGuard]},
  {path: 'info', component: RestaurantInfoPageComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
