import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";
import {NgbDateTimeAdapter} from "../../../adapters/ngbDateTimeAdapter";
import {NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NotificationsService} from "angular2-notifications";
import {startOfDay} from "date-fns";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent implements OnInit {

  DEFAULT_RESERVATION_LENGTH = 2;
  DEFAULT_RESERVATION_PLACES = 4;

  tableList: ITable[];
  reservation: Reservation = new Reservation();
  showList: boolean = false;

  date: Date = new Date();
  time: NgbTimeStruct = NgbDateTimeAdapter.fromModel(new Date());
  private options= {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };


  selectNow() {
    this.date = new Date();
    this.time = NgbDateTimeAdapter.fromModel(new Date());
  }

  defaultValues(){
    this.date = new Date();
    this.time = NgbDateTimeAdapter.fromModel(new Date());
    this.reservation.reservationLength = this.DEFAULT_RESERVATION_LENGTH;
    this.reservation.reservedPlaces = this.DEFAULT_RESERVATION_PLACES;
  }


  constructor(private reservationService: ReservationService,
              private tableService: TableService,
              private notificationsService: NotificationsService) {
  }

  ngOnInit() {
    this.defaultValues();
  }

  isDisabled(date: NgbDateStruct) {
    const selectedDate: Date = new Date(date.year, date.month - 1, date.day);
    return selectedDate < startOfDay(new Date());
  }

  addReservation(table: ITable, reservation: Reservation) {
    this.reservation.tableId = table.id;
    this.reservation.dateReservation = this.reservation.dateReservation.replace('T', '_');
    this.reservationService.addReservation(reservation).subscribe(any => {
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
