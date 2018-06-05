import {Component, Input, OnInit} from '@angular/core';
import {OpenHours} from "../../../model/open-hours";
import {OpenHoursService} from "../../../service/open-hours.service";

@Component({
  selector: 'app-open-hours',
  templateUrl: './open-hours.component.html',
  styleUrls: ['./open-hours.component.scss']
})
export class OpenHoursComponent implements OnInit {
  @Input() edit: boolean;
  OpenHoursArray: OpenHours[];

  constructor(private openHoursService: OpenHoursService) {
  }

  ngOnInit() {
    this.getOpeningHours(2);
  }

  updateOpenHours(){
    this.openHoursService.updateOpenHours(2, OpenHours.toJson(2, this.OpenHoursArray)).subscribe(any => {
      this.getOpeningHours(2);
    })
  }

  getOpeningHours(restaurantId: number){
    this.openHoursService.getOpeningHoursForAllDays(restaurantId).subscribe(request => {
      this.OpenHoursArray = OpenHours.fromJsonArray(request);
    });
  }

}
