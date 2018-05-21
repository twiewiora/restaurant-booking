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


@NgModule({
  declarations: [
    AppComponent,
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    MainComponent,
    StartComponent
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
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
