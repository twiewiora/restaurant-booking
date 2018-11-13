import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbDateAdapter, NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateTimeAdapter} from "../../../adapters/ngbDateTimeAdapter";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DATE_FORMAT, Reservation} from "../../../model/reservation";
import {ITable, Table} from "../../../model/table";
import {ReservationService} from "../../../service/reservation.service";
import {ReservationCommunicationService} from "../reservation-communication.service";
import {TableService} from "../../../service/table.service";
import {startOfDay} from "date-fns";
import {NotificationsService} from "angular2-notifications";
import {ConfirmationDialogService} from "../../confirmation-dialog/confirmation-dialog.service";
import {NgbDateNativeAdapter} from "../../../adapters/ngbDateNativeAdapter";

@Component({
  selector: 'app-add-reservation-dialog',
  templateUrl: './add-reservation-dialog.component.html',
  styleUrls: ['./add-reservation-dialog.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter}]
})
export class AddReservationDialogComponent implements OnInit {

  DEFAULT_RESERVATION_LENGTH = 60;
  DEFAULT_RESERVATION_PLACES = 4;
  MINIMUN_RESERVATION_PLACES = 1;
  MINIMUM_RESERVATION_LENGTH = 1;

  @Input() date: Date;

  isLoaded = true;


  tableList: ITable[];
  reservation: Reservation = new Reservation();
  showList: boolean = false;

  time: NgbTimeStruct;
  private options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };
  public reservationForm: FormGroup;

  constructor(private activeModal: NgbActiveModal,
              private reservationService: ReservationService,
              private tableService: TableService,
              private notificationsService: NotificationsService,
              private reservationCommunicationService: ReservationCommunicationService,
              private confirmationDialogService: ConfirmationDialogService) {
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


  public dismiss() {
    this.activeModal.dismiss();
  }


  defaultValues() {
    this.time = NgbDateTimeAdapter.fromModel(this.date);
    this.reservation.reservationLength = this.DEFAULT_RESERVATION_LENGTH;
    this.reservation.reservedPlaces = this.DEFAULT_RESERVATION_PLACES;
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
    this.reservation.restaurantTableId = table.id;
    this.reservation.dateReservation = this.reservation.dateReservation.replace('T', '_');
    this.reservationService.addReservation(reservation).subscribe(any => {
      const index = this.tableList.indexOf(table);
      if (index > -1) {
        this.tableList.splice(index, 1);
      }
      this.reservationCommunicationService.reservationAdded(true);
      this.notificationsService.success("Reservation Added", '', this.options);
    });
  }

  getBestFreeTableList(reservation: Reservation) {
    this.isLoaded = false;
    this.reservation.setDateReservation(this.date, NgbDateTimeAdapter.toModel(this.time));
    this.tableService.searchBestFreeTables(reservation.dateReservation, reservation.reservationLength, reservation.reservedPlaces).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.showList = true;
      this.isLoaded = true;
    })
  }

  getAllFreeTableList(reservation: Reservation) {
    this.isLoaded = false;
    this.reservation.setDateReservation(this.date, NgbDateTimeAdapter.toModel(this.time));
    this.tableService.searchAllFreeTables(reservation.dateReservation, reservation.reservationLength).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.showList = true;
      this.isLoaded = true;
    })
  }

  public openConfirmationAddReservationDialog(table: ITable, reservation: Reservation) {
    const msg = `<ul><li>table: ${table.identifier}</li><li>reservation length: ${reservation.reservationLength} min</li><li>data: ${reservation.comment}</li></ul>`;

    this.confirmationDialogService.confirm('Create Reservation', `<div>Do you really want to create this reservation?</div> ${msg}`, 'create', 'cancel', 'btn-success', 'btn-secondary')
      .then((confirmed) => {
        if (confirmed) {
          this.addReservation(table, reservation);
          this.dismiss();
        }
      })
      .catch(() => {
      });
  }

  eraseReservationOptions() {
    this.showList = false;
    this.tableList = undefined;
  }
}
