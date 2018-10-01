import {Component, OnInit} from '@angular/core';
import {ITable, Table} from "../../../model/table";
import {TableService} from "../../../service/table.service";
import {TableCommunicationService} from "../table-communication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.scss']
})
export class TableListComponent implements OnInit {
  tables: ITable[];

  constructor(private tableService: TableService,
              private tableCommunicationService: TableCommunicationService,
              private _router: Router) {
  }

  ngOnInit() {
    this.tableCommunicationService.updateList.subscribe(tableAdded => {
      if (tableAdded) {
        this.getAllTables();
      }
    });
    this.getAllTables();
  }

  getAllTables() {
    this.tableService.getTables().subscribe(
      (tables: Table[]) => {
        this.tables = Table.fromJsonToArray(tables);
      },
      error => {
        this._router.navigate(['/info']);
      });
  }
}
