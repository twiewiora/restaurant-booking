import { Component, OnInit } from '@angular/core';
import {IReservation, Reservation} from "../../../model/reservation";
import {ReservationService} from "../../../service/reservation.service";
import * as moment from "moment";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";


@Component({
  selector: 'app-reservations-display',
  templateUrl: './reservations-display.component.html',
  styleUrls: ['./reservations-display.component.scss'],
})
export class ReservationsDisplayComponent implements OnInit {

  reservations: IReservation[];
  changeDate: boolean;
  model: NgbDateStruct;

  selectToday() {
    this.model  = {year:  moment().get('year'), month: moment().get('month') + 1, day: moment().date()};
    this.onDateSelection(this.model);
  }

  constructor(private reservationService: ReservationService) {
  }

  ngOnInit() {
    this.selectToday();
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

  toggleChangeDate(){
    this.changeDate = !this.changeDate;
  }


  onDateSelection(date: NgbDateStruct){
    let day: Date = new Date(date.year, date.month, date.day);
    let nextDay: Date = new Date(day.getTime() + 24 * 60 * 60 * 1000);
    const dayStr: string = date.year + '-' + date.month + '-' + date.day + '_00:00';
    const nextDayStr: string = nextDay.getFullYear() + '-' + nextDay.getMonth() + '-' + nextDay.getDate() + '_00:00';

    this.getReservationForAllTables(dayStr, nextDayStr)
  }
}
