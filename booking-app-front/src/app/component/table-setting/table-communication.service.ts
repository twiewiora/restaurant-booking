import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class TableCommunicationService {

  private newTableAdded = new BehaviorSubject(false);
  updateList = this.newTableAdded.asObservable();

  constructor() { }

  tableAdded(tableAdded: boolean){
    this.newTableAdded.next(tableAdded);
  }
}
