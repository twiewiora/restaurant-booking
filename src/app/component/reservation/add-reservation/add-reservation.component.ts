import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent implements OnInit {

  reservation: IReservation = new Reservation();

  constructor(private reservationService: ReservationService) {
  }

  ngOnInit() {
  }

  addReservation(reservation: IReservation) {
    debugger;
    this.reservation.dateReservation = this.reservation.dateReservation.replace('T', '_');
    debugger;
    this.reservationService.addReservation(reservation).subscribe(any => {

    });
  }

}
