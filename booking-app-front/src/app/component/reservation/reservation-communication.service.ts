import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class ReservationCommunicationService {

  private reservationUpdate = new BehaviorSubject(false);
  private reservationUrlUpdate = new BehaviorSubject({});
  private reservationDateUpdate = new BehaviorSubject(undefined);
  updateList = this.reservationUpdate.asObservable();
  dateUpdate = this.reservationDateUpdate.asObservable();
  urlUpdate = this.reservationUrlUpdate.asObservable();

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

  reservationDateChanged(reservationDate: Date){
    this.reservationDateUpdate.next(reservationDate);
  }

  reservationUrlChange(queryDictionary: {}){
    this.reservationUrlUpdate.next(queryDictionary);
  }
}
