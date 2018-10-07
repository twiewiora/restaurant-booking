import {Component, EventEmitter, Input, OnInit, Output, SimpleChange} from '@angular/core';
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
  @Output() openHoursValidated = new EventEmitter<boolean>();


  ngOnInit() {
  }

  openHoursValid() {
    let isValid = true;
    this.editOpenHoursWeek.forEach((value: OpenHours) => {
      if (!value.validInterval()) {
        isValid = false;
      }
    });
    this.openHoursValidated.emit(isValid);
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    let editOpenHoursWeekChange = changes['editOpenHoursWeek'];
    if ( editOpenHoursWeekChange && editOpenHoursWeekChange.currentValue) {
      this.openHoursValid();
    }
  }
}
