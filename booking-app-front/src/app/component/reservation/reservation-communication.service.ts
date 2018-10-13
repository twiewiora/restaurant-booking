import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class ReservationCommunicationService {

  private reservationUpdate = new BehaviorSubject(false);
  updateList = this.reservationUpdate.asObservable();

  constructor() { }

  reservationAdded(reservationAdd: boolean){
    this.reservationUpdate.next(reservationAdd);
  }

  reservationCanceled(reservationDelete: boolean){
    this.reservationUpdate.next(reservationDelete);
  }


  reservationDeleted(reservationDelete: boolean){
    this.reservationUpdate.next(reservationDelete);
  }
}
