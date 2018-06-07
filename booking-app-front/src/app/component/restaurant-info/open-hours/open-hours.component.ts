import {Component, Input, OnInit} from '@angular/core';
import {OpenHours, Weekday} from "../../../model/open-hours";
import {OpenHoursService} from "../../../service/open-hours.service";

@Component({
  selector: 'app-open-hours',
  templateUrl: './open-hours.component.html',
  styleUrls: ['./open-hours.component.scss']
})
export class OpenHoursComponent implements OnInit {
  @Input() edit: boolean;
  OpenHoursWeek: Map<Weekday, OpenHours>;

  constructor(private openHoursService: OpenHoursService) {
  }

  ngOnInit() {
    this.getOpeningHours();
  }

  updateOpenHours() {
    this.openHoursService.updateOpenHours(OpenHours.toJson(this.OpenHoursWeek)).subscribe(any => {
      this.getOpeningHours();
    })
  }

  getOpeningHours() {
    this.openHoursService.getOpeningHoursForAllDays().subscribe(request => {
      this.OpenHoursWeek = OpenHours.fromJsonArray(request);
    });
  }
}
