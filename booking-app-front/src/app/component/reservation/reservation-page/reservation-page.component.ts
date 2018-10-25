import {Component, OnInit} from '@angular/core';
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {cloneDeep} from "lodash";
import {UrlQueryConverter} from "../../../converters/url-query.converter";
import {ReservationCommunicationService} from "../reservation-communication.service";

@Component({
  selector: 'app-reservation-page',
  templateUrl: './reservation-page.component.html',
  styleUrls: ['./reservation-page.component.scss'],
  providers: [
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy},]
})
export class ReservationPageComponent implements OnInit {
  add: boolean = false;

  urlParams = {};

  constructor(private location: Location,
              private activatedRoute: ActivatedRoute,
              private reservationCommunicationService: ReservationCommunicationService) {
  }

  ngOnInit() {
    this.reservationCommunicationService.urlUpdate.subscribe(urlDictionary => {
      if (urlDictionary) {
        this.setUrl(urlDictionary);
      }
    });
    this.activatedRoute.queryParams.subscribe(params => {
      this.urlParams = UrlQueryConverter.fixQueryDictionary(params);
      this.add = this.urlParams['addReservation'] || false;
    });
  }

  setUrl(urlDictionary: {}) {
    this.urlParams = Object.assign({}, this.urlParams, urlDictionary);
    if (this.add) {
      this.urlParams['addReservation'] = true;
    } else {
      delete this.urlParams['addReservation'];
    }
    this.location.go(this.activatedRoute.routeConfig.path, UrlQueryConverter.dictionaryToQuery(this.urlParams));
  }


  toggleAdd() {
    this.add = !this.add;
    this.setUrl({});
  }
}
