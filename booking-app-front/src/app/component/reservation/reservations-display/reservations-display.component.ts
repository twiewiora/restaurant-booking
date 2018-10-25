import {Component, OnInit, LOCALE_ID, Inject} from '@angular/core';
import {DATE_FORMAT, DATE_TIME_FORMAT, IReservation, Reservation, State} from "../../../model/reservation";
import {ReservationService} from "../../../service/reservation.service";
import * as moment from "moment";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {ViewChild, TemplateRef} from '@angular/core';
import {startOfDay, endOfDay,} from 'date-fns';
import {Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarEvent} from 'angular-calendar';
import {NgbDateNativeAdapter} from "../../../adapters/ngbDateNativeAdapter";
import {OpenHoursService} from "../../../service/open-hours.service";
import {IOpenHours, OpenHours} from "../../../model/open-hours";
import {ITable, Table} from "../../../model/table";
import {TableService} from "../../../service/table.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationsService} from "angular2-notifications";
import {ReservationCommunicationService} from "../reservation-communication.service";
import {ClientService} from "../../../service/client.service";
import {IClient} from "../../../model/client";
import {ConfirmationDialogService} from "../../confirmation-dialog/confirmation-dialog.service";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {UrlQueryConverter} from "../../../converters/url-query.converter";


@Component({
  selector: 'app-reservations-display',
  templateUrl: './reservations-display.component.html',
  styleUrls: ['./reservations-display.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter},
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy},]
})


export class ReservationsDisplayComponent implements OnInit {


  @ViewChild('modalContent') modalContent: TemplateRef<any>;

  HOURS_SEGMENT = 4;
  isLoaded = false;

  openHours: OpenHours;
  tables: Map<number, Table>;
  viewDate: Date;
  view = 'day';

  locale;

  modalData: {
    state: State;
    reservation: Reservation;
    event: CalendarEvent;
  };

  options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };

  refresh: Subject<any> = new Subject();

  events: CalendarEvent[] = [];

  constructor(private reservationService: ReservationService,
              private openHoursService: OpenHoursService,
              private clientService: ClientService,
              private modal: NgbModal,
              @Inject(LOCALE_ID) locale: string,
              private tableService: TableService,
              private _router: Router,
              private notificationService: NotificationsService,
              private reservationCommunicationService: ReservationCommunicationService,
              private confirmationDialogService: ConfirmationDialogService,
              private location: Location,
              private activatedRoute: ActivatedRoute) {
    this.locale = locale;
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = {event: event, state: State.FREE, reservation: undefined};
    this.modal.open(this.modalContent, {size: 'lg'});
  }

  addEvent(event: CalendarEvent): void {
    this.events.push(event);
    this.refresh.next();
  }


  reservations: Reservation[];

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      const fixedParams = UrlQueryConverter.fixQueryDictionary(params);
      this.viewDate = fixedParams['date']
        ? moment(fixedParams['date'], DATE_FORMAT).toDate()
        : new Date();
    });
    this.reservationCommunicationService.updateList.subscribe(reservationAdded => {
      if (reservationAdded) {
        this.onDateSelection(this.viewDate);
      }
    });
    this.reservationCommunicationService.dateUpdate.subscribe(reservationDate => {
      if (reservationDate) {
        this.viewDate = reservationDate;
        this.onDateSelection(reservationDate);
      }
    });
    this.onDateSelection(this.viewDate);
  }


  setUrl(date: Date) {
    let params = {};
    if (date) {
      params['date'] = moment(date).format(DATE_FORMAT);
    }
    this.reservationCommunicationService.reservationUrlChange(params);
  }

  getReservationForAllTables(dateFrom: string, dateTo: string) {
    this.isLoaded = false;
    this.reservationService.getReservationsForAllTables(dateFrom, dateTo).subscribe(
      (reservations: IReservation[]) => {
        this.reservations = reservations.map((reservation: IReservation) => Reservation.fromJson(reservation));
        this.getAllTables(this.reservations);
        this.isLoaded = true;
      });
  }

  getOpeningHoursForDay(weekday: string) {
    this.openHoursService.getOpeningHoursForDay(weekday).subscribe((openHours: IOpenHours) => {
        this.openHours = OpenHours.fromJson(weekday, openHours);
      },
      error => {
        this._router.navigate(['/info']);
      });
  }

  deleteReservation(event: CalendarEvent<IReservation>) {
    this.reservationService.deleteReservation(event.meta).subscribe(any => {
      this.notificationService.error("Reservation Deleted", '', this.options);
      this.onDateSelection(this.viewDate);
    });
  }

  cancelReservation(event: CalendarEvent<IReservation>) {
    this.reservationService.cancelReservation(event.meta).subscribe(any => {
      this.notificationService.warn("Reservation Canceled", '', this.options);
      this.onDateSelection(this.viewDate);
    });
  }

  getAllTables(reservations: Reservation[]) {
    this.tableService.getTables().subscribe(
      (tables: Table[]) => {
        this.tables = Table.fromJsonToMap(tables);
        reservations.forEach(reservation => this.getClient(reservation.clientId, reservation, this.tables[reservation.tableId]));
        this.events = reservations.map((reservation: Reservation) => Reservation.reservationToEventMapper(reservation, this.tables[reservation.tableId]));
        this.refresh.next();
      });
  }

  getClient(id: number, reservation: Reservation, table: Table) {
    if (id) {
      this.clientService.getClient(id).subscribe(client => {
        this.addEvent(Reservation.reservationToEventMapper(reservation, this.tables[reservation.tableId], <IClient>client));
      });
    }
    else {
      this.addEvent(Reservation.reservationToEventMapper(reservation, this.tables[reservation.tableId]));
    }
  }

  onDateSelection(date: Date) {
    this.events = [];
    this.setUrl(date);
    let start: Date = startOfDay(date);
    let end: Date = endOfDay(date);
    let weekday = moment(date).format('dddd');
    this.getOpeningHoursForDay(weekday);
    this.getReservationForAllTables(moment(start).format(DATE_TIME_FORMAT), moment(end).format(DATE_TIME_FORMAT));
  }

  public openConfirmationDeleteReservationDialog(event: CalendarEvent<IReservation>) {
    this.confirmationDialogService.confirm('Delete Reservation', `Do you really want to delete ${event.meta.comment} reservation?`, 'DELETE', 'back', 'btn-danger', 'btn-secondary')
      .then((confirmed) => {
        if (confirmed) {
          this.deleteReservation(event);
          close();
        }
      })
      .catch(() => {
      });
  }

  public openConfirmationCancelReservationDialog(event: CalendarEvent<IReservation>) {
    this.confirmationDialogService.confirm('Cancel Reservation', `Do you really want to cancel ${event.meta.comment} reservation ?`, 'CANCEL', 'back', 'btn-warning', 'btn-secondary')
      .then((confirmed) => {
        if (confirmed) {
          this.cancelReservation(event);
          close();
        }
      })
      .catch(() => {
      });
  }
}
