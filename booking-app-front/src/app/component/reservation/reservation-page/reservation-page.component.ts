import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import * as moment from "moment";
import {now} from "moment";

@Component({
  selector: 'app-reservation-page',
  templateUrl: './reservation-page.component.html',
  styleUrls: ['./reservation-page.component.scss']
})
export class ReservationPageComponent implements OnInit {
  reservations: IReservation[];
  dateFrom;
  dateTo;
  time = {hour: 13, minute: 30};
  model: NgbDateStruct;
  date: {year: number, month: number};
  x = moment().get('year');
  y = moment().get('month') + 1;
  z = moment().date();

  selectToday() {
    this.model = {year: moment().get('year'), month: moment().get('month') + 1, day: moment().date()};
  }

  constructor(private reservationService: ReservationService) {
  }

  ngOnInit() {
    this.getReservationForAllTables("2018-06-04_22:30", "2018-06-15_23:30");
  }

  getReservationForAllTables(dateFrom: string, dateTo: string) {
    this.reservationService.getReservationsForAllTables(dateFrom, dateTo).subscribe(
      (reservations: Reservation[]) => {
        this.reservations = <Reservation[]>reservations;
      })

  }

  deleteReservation(reservation: IReservation){
    this.reservationService.deleteReservation(reservation).subscribe(any => {

    });
  }

}
