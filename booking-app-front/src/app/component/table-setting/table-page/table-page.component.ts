import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-table-page',
  templateUrl: './table-page.component.html',
  styleUrls: ['./table-page.component.scss']
})
export class TablePageComponent implements OnInit {
  newTable: boolean = false;

  constructor() {
  }

  ngOnInit() {
  }

  toggleNew(){
    this.newTable = !this.newTable;
  }
}
