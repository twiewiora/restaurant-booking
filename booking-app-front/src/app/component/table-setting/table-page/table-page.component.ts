import { Component, OnInit } from '@angular/core';
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";

@Component({
  selector: 'app-table-page',
  templateUrl: './table-page.component.html',
  styleUrls: ['./table-page.component.scss']
})
export class TablePageComponent implements OnInit {

  tables: ITable[];

  constructor(private tableService: TableService) { }

  ngOnInit() {
    this.tableService.getTables(2).subscribe(
      (tables: Table[]) => {
        this.tables = <Table[]> tables;
      });
  }

  deleteTable(table: ITable) {
    this.tableService.deleteTable(table).subscribe();
  }

}
