import {Component, OnInit} from '@angular/core';
import {Action} from "../restaurant-info/restaurant-info/restaurant-info.component";

@Component({
  selector: 'app-initial',
  templateUrl: './initial.component.html',
  styleUrls: ['./initial.component.scss']
})

export class InitialComponent implements OnInit {
  edit: boolean = true;
  action: Action = Action.Create;

  constructor() {
  }

  ngOnInit() {
  }
}
