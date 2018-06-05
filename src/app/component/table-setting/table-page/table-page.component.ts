import {Component, OnInit} from '@angular/core';
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";

@Component({
  selector: 'app-table-page',
  templateUrl: './table-page.component.html',
  styleUrls: ['./table-page.component.scss']
})
export class TablePageComponent implements OnInit {

  tables: ITable[];

  constructor(private tableService: TableService) {
  }

  ngOnInit() {
    this.getAllTables(2);
  }

  deleteTable(table: ITable) {
    this.tableService.deleteTable(table).subscribe((any) => {
        this.getAllTables(2);
      }
    );
  }

  getAllTables(restaurantId: number){
    this.tableService.getTables(restaurantId).subscribe(
      (tables: Table[]) => {
        this.tables = <Table[]> tables;
      });
  }

  onTableAdded(tableAdded: boolean) {
    tableAdded ? this.getAllTables(2): null;
  }

}
