import {Component, Input, OnInit, SimpleChange} from '@angular/core';
import {OpenHours, Weekday} from "../../../model/open-hours";
import {OpenHoursService} from "../../../service/open-hours.service";
import {Time} from "@angular/common";
import {NgbStringTimeAdapter} from "../../../adapters/ngbStringTimeAdapter";
//import {NgbDateTimeAdapter} from "../../../adapters/ngbDateTimeAdapter";
//import {NgbTimeAdapter} from "@ng-bootstrap/ng-bootstrap";
//import {NgbStringTimeAdapter} from "../../../adapters/ngbStringTimeAdapter";

@Component({
  selector: 'app-open-hours',
  templateUrl: './open-hours.component.html',
  styleUrls: ['./open-hours.component.scss'],
 // providers: [{provide: NgbTimeAdapter, useClass: NgbStringTimeAdapter}]
})
export class OpenHoursComponent implements OnInit {
  @Input() edit: boolean;
  openHoursWeek: Map<Weekday, OpenHours>;
  editOpenHoursWeek: Map<Weekday, OpenHours>;

  ngbStringTimeAdapter: NgbStringTimeAdapter = new NgbStringTimeAdapter();

  constructor(private openHoursService: OpenHoursService) {
  }

  ngOnInit() {
    this.getOpeningHours()
  }

  updateOpenHours() {
    this.openHoursService.updateOpenHours(OpenHours.toJson(this.editOpenHoursWeek)).subscribe(any => {
      this.getOpeningHours();
    })
  }

  getOpeningHours() {
    this.openHoursService.getOpeningHoursForAllDays().subscribe(request => {
        this.openHoursWeek = OpenHours.fromJsonArray(request);
        this.editOpenHoursWeek = this.openHoursWeek;
      },
      _ => {
        this.openHoursWeek = OpenHours.createNewOpenHoursWeek();
        this.editOpenHoursWeek = this.openHoursWeek;
      });
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    let editChange = changes['edit'];
    if (editChange) {
      if (!editChange.previousValue) {
        this.editOpenHoursWeek = this.openHoursWeek;
      }
      if (!editChange.isFirstChange() && editChange.previousValue)
        this.updateOpenHours();
    }

  }
}
