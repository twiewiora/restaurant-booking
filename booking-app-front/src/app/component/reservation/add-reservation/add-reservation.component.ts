import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";
import {NgbDateTimeAdapter} from "../../../adapters/ngbDateTimeAdapter";
import {NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NotificationsService} from "angular2-notifications";
import {startOfDay} from "date-fns";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ReservationCommunicationService} from "../reservation-communication.service";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent implements OnInit {

  DEFAULT_RESERVATION_LENGTH = 2;
  DEFAULT_RESERVATION_PLACES = 4;
  MINIMUN_RESERVATION_PLACES = 1;
  MINIMUM_RESERVATION_LENGTH = 1;

  @Output() canceled = new EventEmitter<boolean>();

  tableList: ITable[];
  reservation: Reservation = new Reservation();
  showList: boolean = false;

  date: Date;
  time: NgbTimeStruct;
  private options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };
  private reservationForm: FormGroup;

  defaultValues() {
    this.date = new Date();
    this.time = NgbDateTimeAdapter.fromModel(new Date());
    this.reservation.reservationLength = this.DEFAULT_RESERVATION_LENGTH;
    this.reservation.reservedPlaces = this.DEFAULT_RESERVATION_PLACES;
  }

  constructor(private reservationService: ReservationService,
              private tableService: TableService,
              private notificationsService: NotificationsService,
              private reservationCommunicationService: ReservationCommunicationService) {
  }

  ngOnInit() {
    this.defaultValues();
    this.reservationForm = new FormGroup({
      'length': new FormControl(this.reservation.reservationLength, [
        Validators.required, Validators.min(this.MINIMUM_RESERVATION_LENGTH)
      ]),
      'places': new FormControl(this.reservation.reservedPlaces, [Validators.required, Validators.min(this.MINIMUN_RESERVATION_PLACES)]),
      'comment': new FormControl(this.reservation.comment, [Validators.required])
    });
  }

  get length() {
    return this.reservationForm.get('length');
  }

  get places() {
    return this.reservationForm.get('places');
  }

  get comment() {
    return this.reservationForm.get('comment');
  }

  isDisabled(date: NgbDateStruct) {
    const selectedDate: Date = new Date(date.year, date.month - 1, date.day);
    return selectedDate < startOfDay(new Date());
  }

  addReservation(table: ITable, reservation: Reservation) {
    this.reservation.tableId = table.id;
    this.reservation.dateReservation = this.reservation.dateReservation.replace('T', '_');
    this.reservationService.addReservation(reservation).subscribe(any => {
      const index = this.tableList.indexOf(table);
      if(index > -1){
        this.tableList.splice(index, 1);
      }
      this.reservationCommunicationService.reservationAdded(true);
      this.notificationsService.success("Reservation Added", '', this.options);
    });
  }

  getFreeTableList(reservation: Reservation) {
    this.reservation.setDateReservation(this.date, NgbDateTimeAdapter.toModel(this.time));
    this.tableService.searchFreeTables(reservation.dateReservation, reservation.reservationLength, reservation.reservedPlaces).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.showList = true;
    })
  }
}
