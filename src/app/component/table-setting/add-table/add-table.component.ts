import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";
import {TableCommunicationService} from "../table-communication.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: '[app-add-table]',
  templateUrl: './add-table.component.html',
  styleUrls: ['./add-table.component.scss']
})
export class AddTableComponent implements OnInit {

  @Output() canceledAddTable = new EventEmitter<boolean>();
  MIN = 1;
  table: Table = new Table();
  private tableForm: FormGroup;
  private options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };

  constructor(private tableService: TableService,
              private tableCommunicationService: TableCommunicationService,
              private notificationService: NotificationsService) {
  }

  ngOnInit() {
    this.tableForm = new FormGroup({
      'identifier': new FormControl(this.table.identifier, [
        Validators.required
      ]),
      'maxPlaces': new FormControl(this.table.maxPlaces, [Validators.required, Validators.min(this.MIN)]),
      'comment': new FormControl(this.table.comment)
    });
  }

  get identifier() {
    return this.tableForm.get('identifier');
  }

  get maxPlaces() {
    return this.tableForm.get('maxPlaces');
  }

  get comment() {
    return this.tableForm.get('comment');
  }

  cancelAddTable() {
    this.canceledAddTable.emit(true);
  }

  addTable(table: ITable) {
    this.tableService.addTable(table).subscribe(any => {
      this.tableCommunicationService.tableAdded(true);
      this.table = new Table();
      this.tableForm.reset({indentifier: '', maxPlaces: null, comment: ''});
      this.notificationService.success("Table added", '', this.options);
    });
  }
}
