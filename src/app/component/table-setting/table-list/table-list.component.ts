import {Component, OnInit} from '@angular/core';
import {ITable, Table} from "../../../model/table";
import {TableService} from "../../../service/table.service";
import {TableCommunicationService} from "../table-communication.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.scss'],
  providers: [
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy},]
})
export class TableListComponent implements OnInit {
  tables: ITable[];
  isNewTable = false;
  isLoaded = false;

  constructor(private tableService: TableService,
              private tableCommunicationService: TableCommunicationService,
              private _router: Router,
              private location:  Location,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      this.isNewTable = params['newTable'] || false;
    });
    this.tableCommunicationService.updateList.subscribe(tableAdded => {
      if (tableAdded) {
        this.getAllTables();
      }
    });
    this.getAllTables();
  }

  setUrl(){
    let params = [];
    if(this.isNewTable){
      params.push(`newTable=${this.isNewTable}`);
    }
    this.location.go(this.activatedRoute.routeConfig.path, params.join('&'));
  }

  getAllTables() {
    this.isLoaded = false;
    this.tableService.getTables().subscribe(
      (tables: Table[]) => {
        this.tables = Table.fromJsonToArray(tables);
        this.isLoaded = true;
      },
      error => {
        this._router.navigate(['/info']);
      });
  }

  toggleIsNewTable() {
    this.isNewTable = !this.isNewTable;
    this.setUrl();
  }
}
