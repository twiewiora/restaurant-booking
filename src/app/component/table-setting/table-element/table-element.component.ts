import {Component, Input, OnInit} from '@angular/core';
import {ITable, Table} from "../../../model/table";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {TableCommunicationService} from "../table-communication.service";
import {TableService} from "../../../service/table.service";
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: '[app-table-element]',
  templateUrl: './table-element.component.html',
  styleUrls: ['./table-element.component.scss']
})
export class TableElementComponent implements OnInit {

  MIN = 1;

  @Input() table: Table;

  private options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };


  private tableForm: FormGroup;


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

  deleteTable(table: ITable) {
    this.tableService.deleteTable(table).subscribe((any) => {
        this.notificationService.error("Table Deleted", '', this.options);
        this.tableCommunicationService.tableDeleted(true);
      }
    );
  }

  updateTable(table: ITable) {
    this.tableService.updateTable(table).subscribe((any) => {
        this.notificationService.success("Table Updated", '', this.options);
        this.tableCommunicationService.tableUpdated(true);

      }
    );
  }
}
