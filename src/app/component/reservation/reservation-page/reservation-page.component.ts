import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../service/reservation.service";
import {IReservation, Reservation} from "../../../model/reservation";
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import * as moment from "moment";
import {now} from "moment";

@Component({
  selector: 'app-reservation-page',
  templateUrl: './reservation-page.component.html',
  styleUrls: ['./reservation-page.component.scss']
})
export class ReservationPageComponent implements OnInit {
  addNow: boolean = false;
  add: boolean = false;

  constructor() {
  }

  ngOnInit() {
  }


  toggleAddNow(){
    this.addNow = !this.addNow;
  }

  toggleAdd(){
    this.add = !this.add;
  }

}
