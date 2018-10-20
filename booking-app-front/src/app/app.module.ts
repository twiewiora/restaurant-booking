import {BrowserModule} from '@angular/platform-browser';
import {NgModule, Injectable, LOCALE_ID} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbDateAdapter, NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {LoginComponent} from './component/login/login.component';
import {RegistrationComponent} from './component/registration/registration.component';
import {AuthenticationService} from "./service/authentication.service";
import {TokenInterceptor} from "./service/token-interceptor";
import {AppRoutingModule} from "./app-routing.module";
import {StartComponent} from './component/start/start.component';
import {AuthGuard} from "./guard/auth-guard";
import {HomeGuard} from "./guard/home-guard";
import {AddTableComponent} from './component/table-setting/add-table/add-table.component';
import {TablePageComponent} from './component/table-setting/table-page/table-page.component';
import {ReservationPageComponent} from './component/reservation/reservation-page/reservation-page.component';
import {RestaurantInfoPageComponent} from './component/restaurant-info/restaurant-info-page/restaurant-info-page.component';
import {TopBarComponent} from './component/top-bar/top-bar.component';
import {TableService} from "./service/table.service";
import {OpenHoursService} from "./service/open-hours.service";
import {RestaurantInfoService} from "./service/restaurantInfo.service";
import {ReservationService} from "./service/reservation.service";
import {OpenHoursComponent} from './component/restaurant-info/open-hours/open-hours.component';
import {TableListComponent} from './component/table-setting/table-list/table-list.component';
import {TableCommunicationService} from "./component/table-setting/table-communication.service";
import {MapValuesPipe} from "./component/pipes/map-values-pipe";
import {AddReservationComponent} from './component/reservation/add-reservation/add-reservation.component';
import {RestaurantInfoComponent} from './component/restaurant-info/restaurant-info/restaurant-info.component';
import {RlTagInputModule} from "angular2-tags/dist";
import {TagInputModule} from "ngx-chips";
import {BrowserAnimationsModule, NoopAnimationsModule} from "@angular/platform-browser/animations";
import {ReservationsDisplayComponent} from './component/reservation/reservations-display/reservations-display.component';
import {CalendarModule} from 'angular-calendar';
import {NgbDateNativeAdapter} from "./adapters/ngbDateNativeAdapter";
import {NgbDateTimeAdapter} from "./adapters/ngbDateTimeAdapter";
import {NgbStringTimeAdapter} from "./adapters/ngbStringTimeAdapter";
import { TableElementComponent } from './component/table-setting/table-element/table-element.component';
import {SimpleNotificationsModule} from "angular2-notifications";
import {ReservationCommunicationService} from "./component/reservation/reservation-communication.service";
import {ClientService} from "./service/client.service";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
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
    RestaurantInfoComponent,
    ReservationsDisplayComponent,
    TableElementComponent,
  ],
  imports: [
    NgbModule.forRoot(),
    RlTagInputModule,
    TagInputModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    NoopAnimationsModule,
    BrowserAnimationsModule,
    CalendarModule.forRoot(),
    NgbModalModule.forRoot(),
    SimpleNotificationsModule.forRoot()
  ],
  providers: [
    AuthGuard,
    HomeGuard,
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
    TableCommunicationService,
    ReservationCommunicationService,
    ClientService,
    {provide: NgbDateAdapter, useClass: NgbDateNativeAdapter},
    {provide: LOCALE_ID, useValue: 'en-gb' },
    NgbStringTimeAdapter,
    NgbDateTimeAdapter
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
