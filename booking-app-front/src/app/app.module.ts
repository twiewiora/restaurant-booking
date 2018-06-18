import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
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
import {OpenHoursService} from "./service/open-hours.service";
import {RestaurantInfoService} from "./service/restaurantInfo.service";
import {ReservationService} from "./service/reservation.service";
import { OpenHoursComponent } from './component/restaurant-info/open-hours/open-hours.component';
import { TableListComponent } from './component/table-setting/table-list/table-list.component';
import {TableCommunicationService} from "./component/table-setting/table-communication.service";
import {MapValuesPipe} from "./component/pipes/map-values-pipe";
import { AddReservationComponent } from './component/reservation/add-reservation/add-reservation.component';
import { InitialComponent } from './component/initial/initial.component';
import {InitialGuard} from "./guard/initial-guard";
import { RestaurantInfoComponent } from './component/restaurant-info/restaurant-info/restaurant-info.component';
import {RlTagInputModule} from "angular2-tags/dist";
import {TagInputModule} from "ngx-chips";
import {BrowserAnimationsModule, NoopAnimationsModule} from "@angular/platform-browser/animations";
import { ReservationsDisplayComponent } from './component/reservation/reservations-display/reservations-display.component';
import { CalendarModule } from 'angular-calendar';


@NgModule({
  declarations: [
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
    OpenHoursComponent,
    TableListComponent,
    MapValuesPipe,
    AddReservationComponent,
    InitialComponent,
    RestaurantInfoComponent,
    ReservationsDisplayComponent
  ],
  imports: [
    NgbModule.forRoot(),
    RlTagInputModule,
    TagInputModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    NoopAnimationsModule,
    BrowserAnimationsModule,
    CalendarModule.forRoot(),
    NgbModalModule.forRoot()
  ],
  providers: [
    AuthGuard,
    HomeGuard,
    InitialGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthenticationService,
    TableService,
    OpenHoursService,
    RestaurantInfoService,
    ReservationService,
    TableCommunicationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
