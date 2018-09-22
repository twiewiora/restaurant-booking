import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class TableCommunicationService {

  private tableUpdate = new BehaviorSubject(false);
  updateList = this.tableUpdate.asObservable();

  constructor() { }

  tableAdded(tableAdd: boolean){
    this.tableUpdate.next(tableAdd);
  }

  tableUpdated(tableUpdate: boolean){
    this.tableUpdate.next(tableUpdate);
  }

  tableDeleted(tableDelete: boolean){
    this.tableUpdate.next(tableDelete);
  }
}
