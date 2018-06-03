import { Component, OnInit } from '@angular/core';
import {TimeTableService} from "../../../service/timeTable.service";
import {OpenDay, TimeTable} from "../../../model/timetable";
import {WeekDay} from "@angular/common";

@Component({
  selector: 'app-restaurant-info-page',
  templateUrl: './restaurant-info-page.component.html',
  styleUrls: ['./restaurant-info-page.component.scss']
})
export class RestaurantInfoPageComponent implements OnInit {

  openDays: OpenDay[] = [];
  oneDay: OpenDay;

  constructor(private timeTableService: TimeTableService) { }

  ngOnInit() {
    this.timeTableService.getOpeningHoursForAllDays(2).subscribe(request => {
      this.openDays = OpenDay.fromJsonArray(request);
    });
    this.timeTableService.getOpeningHoursForDay(2, "WEDNESDAY").subscribe(request => {
      this.oneDay = <OpenDay> request;
    });
    this.timeTableService.updateOpenHours(2, JSON.stringify({restaurantId : 2,
      sunday:{openHour:"01:30:00",closeHour:"19:30:00"},
      wednesday:{openHour:"02:30:00",closeHour:"22:30:00"},
      monday:{openHour:"03:30:00",closeHour:"20:30:00"},
      thursday:{openHour:"04:30:00",closeHour:"22:30:00"},
      tuesday: {closeHour: "05:00:00", openHour: "10:00"},
      friday: {closeHour: "06:00:00", openHour: "10:00"},
      saturday: {closeHour: "07:00:00", openHour: "10:00"}})).subscribe();
  }

}
