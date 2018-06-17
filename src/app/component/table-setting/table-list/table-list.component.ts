import {Component, OnInit} from '@angular/core';
import {ITable, Table} from "../../../model/table";
import {TableService} from "../../../service/table.service";
import {TableCommunicationService} from "../table-communication.service";

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.scss']
})
export class TableListComponent implements OnInit {
  tables: ITable[];

  constructor(private tableService: TableService,
              private tableCommunicationService: TableCommunicationService) {
  }

  ngOnInit() {
    this.tableCommunicationService.updateList.subscribe(tableAdded => {
      if (tableAdded) {
        this.getAllTables();
      }
    });
    this.getAllTables();
  }

  deleteTable(table: ITable) {
    this.tableService.deleteTable(table).subscribe((any) => {
        this.getAllTables();
      }
    );
  }

  updateTable(table: ITable) {
    this.tableService.updateTable(table).subscribe((any) => {
        this.getAllTables();
      }
    );
  }

  getAllTables() {
    this.tableService.getTables().subscribe(
      (tables: Table[]) => {
        this.tables = Table.fromJsonToArray(tables);
      });
  }
}
