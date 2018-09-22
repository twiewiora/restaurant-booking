import {Component, Input, OnInit,} from '@angular/core';
import {OpenHours, Weekday} from "../../../model/open-hours";

@Component({
  selector: 'app-open-hours',
  templateUrl: './open-hours.component.html',
  styleUrls: ['./open-hours.component.scss']
})
export class OpenHoursComponent implements OnInit {
  @Input() edit: boolean;
  @Input() openHoursWeek: Map<Weekday, OpenHours>;
  @Input() editOpenHoursWeek: Map<Weekday, OpenHours>;

  constructor() {
  }

  ngOnInit() {
  }

  openHoursValid(): boolean {
    let isValid = true;
    this.editOpenHoursWeek.forEach((value: OpenHours) => {
      if (!value.validInterval()) {
        isValid = false;
      }
    });
    return isValid;
  }
}
