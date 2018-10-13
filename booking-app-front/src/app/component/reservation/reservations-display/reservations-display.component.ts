import {Component, OnInit, LOCALE_ID, Inject} from '@angular/core';
import {DATE_TIME_FORMAT, IReservation, Reservation, State} from "../../../model/reservation";
import {ReservationService} from "../../../service/reservation.service";
import * as moment from "moment";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";


import {
  ViewChild,
  TemplateRef
} from '@angular/core';
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  addHours
} from 'date-fns';
import {Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent
} from 'angular-calendar';
import {NgbDateNativeAdapter} from "../../../adapters/ngbDateNativeAdapter";
import {OpenHoursService} from "../../../service/open-hours.service";
import {IOpenHours, OpenHours} from "../../../model/open-hours";
import {Table} from "../../../model/table";
import {TableService} from "../../../service/table.service";
import {Router} from "@angular/router";
import {NotificationsService} from "angular2-notifications";
import {ReservationCommunicationService} from "../reservation-communication.service";


@Component({
  selector: 'app-reservations-display',
  templateUrl: './reservations-display.component.html',
  styleUrls: ['./reservations-display.component.scss'],

  providers: [{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter}]
})


export class ReservationsDisplayComponent implements OnInit {

  HOURS_SEGMENT = 4;

  @ViewChild('modalContent') modalContent: TemplateRef<any>;

  view: string = 'day';

  openHours: OpenHours;
  tables: Map<number, Table>;
  viewDate: Date = new Date();

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
              private modal: NgbModal,
              @Inject(LOCALE_ID) locale: string,
              private tableService: TableService,
              private _router: Router,
              private notificationService: NotificationsService,
              private reservationCommunicationService: ReservationCommunicationService) {
    this.locale = locale;
  }

  eventTimesChanged({
                      event,
                      newStart,
                      newEnd
                    }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    this.handleEvent('Dropped or resized', event);
    this.refresh.next();
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
    this.reservationCommunicationService.updateList.subscribe(reservationAdded => {
      if (reservationAdded) {
        this.onDateSelection(this.viewDate);
      }
    });
    this.onDateSelection(this.viewDate);
    this.getAllTables();
  }

  getReservationForAllTables(dateFrom: string, dateTo: string) {
    this.reservationService.getReservationsForAllTables(dateFrom, dateTo).subscribe(
      (reservations: IReservation[]) => {
        this.reservations = reservations.map((reservation: IReservation) => Reservation.fromJson(reservation));
        this.events = this.reservations.map((reservation: Reservation) => Reservation.reservationToEventMapper(reservation, this.tables[reservation.tableId]));
        this.refresh.next();
      })
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

  getAllTables() {
    this.tableService.getTables().subscribe(
      (tables: Table[]) => {
        this.tables = Table.fromJsonToMap(tables);
      });
  }

  onDateSelection(date: Date) {
    let start: Date = startOfDay(date);
    let end: Date = endOfDay(date);
    let weekday = moment(date).format('dddd');
    this.getOpeningHoursForDay(weekday);
    this.getReservationForAllTables(moment(start).format(DATE_TIME_FORMAT), moment(end).format(DATE_TIME_FORMAT));
  }
}
