import * as moment from "moment";
import {CalendarEvent} from "angular-calendar";
import {Table} from "./table";
import {IClient} from "./client";

export const DATE_TIME_FORMAT = 'YYYY-MM-DD_HH:mm';
export const DATE_FORMAT = 'YYYY-MM-DD';
export const TIME_FORMAT = 'HH:mm';
export enum State {FREE, OCCUPIED, FINISHED}

export interface IReservation {


  cancelled: boolean;
  clientId: number;
  comment: string;
  dateReservation: string;
  id: number;
  reservationLength: number;
  reservedPlaces: number;
  restaurantTableId: number;
}


export class Reservation {
  cancelled: boolean;
  clientId: number;
  comment: string = '';
  dateReservation: string;
  id: number;
  reservationLength: number;
  reservedPlaces: number;
  restaurantTableId: number;
  table: Table;

  constructor() {
  }

  toJson(): string {
    return JSON.stringify({
      restaurantTableId: this.restaurantTableId,
      dateReservation: this.dateReservation,
      reservationLength: this.reservationLength,
      reservedPlaces: this.reservedPlaces,
      comment: this.comment
    });
  }

  setDateReservation(date: Date, time: Date) {
    this.dateReservation = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + '_' + time.getHours() + ':' + time.getMinutes();
  }

  static getReservationStart(reservationDate: string): Date {
    return moment(reservationDate, DATE_TIME_FORMAT).toDate();
  }

  static getReservationEnd(reservationDate: string, length: number): Date {
    return moment(reservationDate, DATE_TIME_FORMAT).add(length, 'm').toDate();
  }

  static reservationToEventMapper(reservation: Reservation, table: Table, client: IClient = undefined): CalendarEvent {
    reservation.table = table;
    if (client) {
      reservation.comment = client.username;
    }
    const event: CalendarEvent<Reservation> = {
      title: reservation.comment,
      start: Reservation.getReservationStart(reservation.dateReservation),
      end: Reservation.getReservationEnd(reservation.dateReservation, reservation.reservationLength),
      meta: reservation
    };
    event.color = reservation.cancelled ? colors.red : colors.blue;
    return event;
  }

  static fromJson(json: IReservation): Reservation {
    let reservation: Reservation = new Reservation();
    reservation.restaurantTableId = json.restaurantTableId;
    reservation.dateReservation = json.dateReservation;
    reservation.reservationLength = json.reservationLength;
    reservation.comment = json.comment;
    reservation.cancelled = json.cancelled;
    reservation.id = json.id;
    reservation.clientId = json.clientId;
    return reservation;
  }
}


const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF'
  }
};
