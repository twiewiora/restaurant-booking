import {Time} from "./time";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {NgbTime} from "@ng-bootstrap/ng-bootstrap/timepicker/ngb-time";

export interface IReservation {

  id: number;
  reservedPlaces: number;
  reservationLength: number;
  comment: string;
  dateReservation: string;
  restaurantTableId: number;
  clientId: number;
  cancelled: boolean;

  toJson(): string;
}


export class Reservation implements IReservation {
  id: number;
  dateReservation: string;
  reservationLength: number;
  comment: string;
  restaurantTableId: number;
  clientId: number;
  reservedPlaces: number;
  cancelled: boolean;

  constructor() {
  }

  toJson(): string {
    return JSON.stringify({
      tableId: this.restaurantTableId,
      date: this.dateReservation,
      length: this.reservationLength,
      places: this.reservedPlaces,
      comment: this.comment
    });
  }


  setDateReservation(date: NgbDate, time: NgbTime) {
    this.dateReservation = date.year + '-' + date.month + '-' + date.day + '_' + time.hour + ':' + time.minute;
  }

}
