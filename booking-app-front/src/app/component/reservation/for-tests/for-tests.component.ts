import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-for-tests',
  templateUrl: './for-tests.component.html',
  styleUrls: ['./for-tests.component.scss']
})
export class ForTestsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  model1: Date;
  model2: Date;

  get today() {
    return new Date();
  }
}
