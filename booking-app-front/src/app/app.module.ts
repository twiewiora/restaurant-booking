import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import {AuthenticationService} from "./service/authentication.service";
import {TokenInterceptor} from "./service/token-interceptor";
import { MainComponent } from './component/main/main.component';
import {AppRoutingModule} from "./app-routing.module";
import { StartComponent } from './component/start/start.component';
import {AuthGuard} from "./guard/auth-guard";
import {HomeGuard} from "./guard/home-guard";
import { AddTableComponent } from './component/table-setting/add-table/add-table.component';
import { TablePageComponent } from './component/table-setting/table-page/table-page.component';
import { ReservationPageComponent } from './component/reservation/reservation-page/reservation-page.component';
import { RestaurantInfoPageComponent } from './component/restaurant-info/restaurant-info-page/restaurant-info-page.component';
import { TopBarComponent } from './component/top-bar/top-bar.component';
import {TableService} from "./service/table.service";
import {TimeTableService} from "./service/timeTable.service";
import {RestaurantInfoService} from "./service/restaurantInfo.service";
import {ReservationService} from "./service/reservation.service";
import { OpenHoursComponent } from './component/restaurant-info/open-hours/open-hours.component';


@NgModule({
  declarations: [
    AppComponent,
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    MainComponent,
    StartComponent,
    AddTableComponent,
    TablePageComponent,
    ReservationPageComponent,
    RestaurantInfoPageComponent,
    TopBarComponent,
    OpenHoursComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    AuthGuard,
    HomeGuard,
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    TableService,
    TimeTableService,
    RestaurantInfoService,
    ReservationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
