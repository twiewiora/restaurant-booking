import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-restaurant-info-page',
  templateUrl: './restaurant-info-page.component.html',
  styleUrls: ['./restaurant-info-page.component.scss']
})
export class RestaurantInfoPageComponent implements OnInit {

  edit: boolean = false;

  //oneDay: OpenHours;

  constructor() {
  }

  ngOnInit() {
    // this.openHoursService.getOpeningHoursForDay(2, "WEDNESDAY").subscribe(request => {
    //   this.oneDay = <OpenHours> request;
    // });
  }

  toggleEdit(){
    this.edit = !this.edit;
  }
}
