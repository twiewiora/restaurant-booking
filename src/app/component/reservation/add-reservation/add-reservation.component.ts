import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";
import * as moment from "moment";
import {NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NgbTime} from "@ng-bootstrap/ng-bootstrap/timepicker/ngb-time";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent implements OnInit {

  tableList: ITable[];
  reservation: Reservation = new Reservation();
  showList: boolean = false;

  model: NgbDate = new NgbDate(moment().get('year'), moment().get('month') + 1, moment().date());
  time: NgbTime = new NgbTime();
  date: { year: number, month: number, day: number };


  selectToday() {
    this.time.minute = moment().get('minute');
    this.time.hour = moment().get('hour');
    this.model.year = moment().get('year');
    this.model.month = moment().get('month') + 1;
    this.model.day = moment().date();
  }


  constructor(private reservationService: ReservationService,
              private tableService: TableService) {
  }

  ngOnInit() {

    this.time.minute = moment().get('minute');
    this.time.hour = moment().get('hour');
    this.model.year = moment().get('year');
    this.model.month = moment().get('month') + 1;
    this.model.day = moment().date();
  }

  addReservation(table: ITable, reservation: IReservation) {
    this.reservation.tableId = table.id;
    this.reservation.dateReservation = this.reservation.dateReservation.replace('T', '_');
    this.reservationService.addReservation(reservation).subscribe(any => {

    });
  }

  getFreeTableList(reservation: Reservation) {
    this.reservation.setDateReservation(this.model, this.time);
    this.tableService.searchFreeTables(reservation.dateReservation, reservation.reservationLength, reservation.reservedPlaces).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.showList = true;
    })
  }

}
