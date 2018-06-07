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


export class Reservation implements  IReservation{
  id: number;
  dateReservation: string;
  reservationLength: number;
  comment: string;
  restaurantTableId: number;
  clientId: number;
  reservedPlaces: number;
  cancelled: boolean;

  getDate(): string{
    return '';
  }

  constructor() { }

  toJson(): string {
    return JSON.stringify({
      tableId: this.restaurantTableId,
      date: this.dateReservation,
      length: this.reservationLength,
      places: this.reservedPlaces,
      comment: this.comment
    });
  }


}
