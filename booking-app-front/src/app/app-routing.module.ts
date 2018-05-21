import { NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegistrationComponent} from "./component/registration/registration.component";
import {LoginComponent} from "./component/login/login.component";
import {MainComponent} from "./component/main/main.component";
import {StartComponent} from "./component/start/start.component";
import {AuthGuard} from "./guard/auth-guard";
import {HomeGuard} from "./guard/home-guard";

const routes: Routes = [
  { path: '', redirectTo: '/start', pathMatch: 'full'},
  { path: 'start', component: StartComponent},
  { path: 'home', component: MainComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent},
  { path: 'registration', component: RegistrationComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
