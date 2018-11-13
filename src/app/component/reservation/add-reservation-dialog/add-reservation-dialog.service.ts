import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';

import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {AddReservationDialogComponent} from './add-reservation-dialog.component'

@Injectable()
export class AddReservationDialogService {

  constructor(private modalService: NgbModal) {
  }

  public confirm(date: Date): Promise<boolean> {
    const modalRef = this.modalService.open(AddReservationDialogComponent);
    modalRef.componentInstance.date = date;
    return modalRef.result;
  }
}
