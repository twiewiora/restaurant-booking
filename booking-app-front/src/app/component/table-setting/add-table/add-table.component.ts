import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {TableService} from "../../../service/table.service";
import {ITable, Table} from "../../../model/table";

@Component({
  selector: 'app-add-table',
  templateUrl: './add-table.component.html',
  styleUrls: ['./add-table.component.scss']
})
export class AddTableComponent implements OnInit {

  table: Table = new Table();
  @Output() tableAdded = new EventEmitter<boolean>();

  constructor(private tableService: TableService) {
  }

  ngOnInit() {
  }

  addTable(table: ITable) {
    table.restaurantId = 2;
    this.tableService.addTable(table).subscribe( any =>
    {
      this.tableAdded.emit(true);
    });
  }

}
