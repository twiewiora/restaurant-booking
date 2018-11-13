import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {DATE_FORMAT, IReservation, Reservation} from "../../../model/reservation";
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";
import {NgbDateTimeAdapter} from "../../../adapters/ngbDateTimeAdapter";
import {NgbDateAdapter, NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NotificationsService} from "angular2-notifications";
import {startOfDay} from "date-fns";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ReservationCommunicationService} from "../reservation-communication.service";
import {ConfirmationDialogService} from "../../confirmation-dialog/confirmation-dialog.service";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {NgbDateNativeAdapter} from "../../../adapters/ngbDateNativeAdapter";
import * as moment from "moment";
import {ActivatedRoute} from "@angular/router";
import {UrlQueryConverter} from "../../../converters/url-query.converter";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter},
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy},]
})
export class AddReservationComponent implements OnInit {

  DEFAULT_RESERVATION_LENGTH = 60;
  DEFAULT_RESERVATION_PLACES = 4;
  MINIMUN_RESERVATION_PLACES = 1;
  MINIMUM_RESERVATION_LENGTH = 1;

  isLoaded = false;

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
  public reservationForm: FormGroup;

  defaultValues() {
    this.time = NgbDateTimeAdapter.fromModel(new Date());
    this.reservation.reservationLength = this.DEFAULT_RESERVATION_LENGTH;
    this.reservation.reservedPlaces = this.DEFAULT_RESERVATION_PLACES;
  }

  constructor(private reservationService: ReservationService,
              private tableService: TableService,
              private notificationsService: NotificationsService,
              private reservationCommunicationService: ReservationCommunicationService,
              private confirmationDialogService: ConfirmationDialogService,
              private location: Location,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      const fixedParams = UrlQueryConverter.fixQueryDictionary(params);
      this.date = fixedParams['date']
        ? moment(fixedParams['date'], DATE_FORMAT).toDate()
        : new Date();
    });
    this.defaultValues();
    this.reservationForm = new FormGroup({
      'length': new FormControl(this.reservation.reservationLength, [
        Validators.required, Validators.min(this.MINIMUM_RESERVATION_LENGTH)
      ]),
      'places': new FormControl(this.reservation.reservedPlaces, [Validators.required, Validators.min(this.MINIMUN_RESERVATION_PLACES)]),
      'comment': new FormControl(this.reservation.comment, [Validators.required])
    });
  }

  setUrl() {
    let params = {};

    this.reservationCommunicationService.reservationUrlChange(params);
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
      if (index > -1) {
        this.tableList.splice(index, 1);
      }
      this.reservationCommunicationService.reservationAdded(true);
      this.notificationsService.success("Reservation Added", '', this.options);
    });
  }

  getBestFreeTableList(reservation: Reservation) {
    this.showList = true;
    this.isLoaded = false;
    this.reservation.setDateReservation(this.date, NgbDateTimeAdapter.toModel(this.time));
    this.tableService.searchBestFreeTables(reservation.dateReservation, reservation.reservationLength, reservation.reservedPlaces).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.isLoaded = true;
    })
  }

  getAllFreeTableList(reservation: Reservation) {
    this.showList = true;
    this.isLoaded = false;
    this.reservation.setDateReservation(this.date, NgbDateTimeAdapter.toModel(this.time));
    this.tableService.searchAllFreeTables(reservation.dateReservation, reservation.reservationLength).subscribe(tableList => {
      this.tableList = Table.fromJsonToArray(tableList);
      this.isLoaded = true;
    })
  }

  public openConfirmationAddReservationDialog(table: ITable, reservation: Reservation) {
    const msg = `<ul><li>table: ${table.identifier}</li><li>reservation length: ${reservation.reservationLength} min</li><li>data: ${reservation.comment}</li></ul>`;
    this.confirmationDialogService.confirm('Create Reservation', `<div>Do you really want to create this reservation?</div> ${msg}`, 'create', 'cancel', 'btn-success', 'btn-secondary')
      .then((confirmed) => {
        if (confirmed) {
          this.addReservation(table, reservation);
        }
      })
      .catch(() => {
      });
  }


  onDateSelection(date: Date){
    this.reservationCommunicationService.reservationDateChanged(date);
  }
}
