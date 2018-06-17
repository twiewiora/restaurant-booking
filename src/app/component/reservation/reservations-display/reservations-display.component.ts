import { Component, OnInit } from '@angular/core';
import {IReservation, Reservation} from "../../../model/reservation";
import {ReservationService} from "../../../service/reservation.service";

@Component({
  selector: 'app-reservations-display',
  templateUrl: './reservations-display.component.html',
  styleUrls: ['./reservations-display.component.scss']
})
export class ReservationsDisplayComponent implements OnInit {

  reservations: IReservation[];

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
