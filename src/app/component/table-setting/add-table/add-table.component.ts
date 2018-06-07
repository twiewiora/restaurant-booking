import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";
import {TableCommunicationService} from "../table-communication.service";

@Component({
  selector: 'app-add-table',
  templateUrl: './add-table.component.html',
  styleUrls: ['./add-table.component.scss']
})
export class AddTableComponent implements OnInit {

  table: Table = new Table();

  constructor(private tableService: TableService,
              private tableCommunicationService: TableCommunicationService) {
  }

  ngOnInit() {
  }

  addTable(table: ITable) {
    table.restaurantId = 2;
    this.tableService.addTable(table).subscribe( any =>
    {
      this.tableCommunicationService.tableAdded(true);
    });
  }

}
