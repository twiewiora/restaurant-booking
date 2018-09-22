import {Component, Input, OnInit, SimpleChange} from '@angular/core';
import {Restaurant} from "../../../model/restaurant";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-restaurant-info',
  templateUrl: './restaurant-info.component.html',
  styleUrls: ['./restaurant-info.component.scss']
})
export class RestaurantInfoComponent implements OnInit {

  @Input() restaurant: Restaurant;
  @Input() editRestaurant: Restaurant;
  @Input() edit: boolean;
  @Input() autocompleteItems: string[];


  private restaurantInfoForm: FormGroup;
  private validationCreated: boolean = false;


  constructor() {
  }

  ngOnInit() {

  }

  get name() {
    return this.restaurantInfoForm.get('name');
  }

  get city() {
    return this.restaurantInfoForm.get('city');
  }

  get street() {
    return this.restaurantInfoForm.get('street');
  }

  get phoneNumber() {
    return this.restaurantInfoForm.get('phoneNumber');
  }

  createFormValidation() {
    this.restaurantInfoForm = new FormGroup({
      'name': new FormControl(this.editRestaurant.name, [
        Validators.required
      ]),
      'city': new FormControl(this.editRestaurant.city, [Validators.required]),
      'street': new FormControl(this.editRestaurant.street, [Validators.required]),
      'phoneNumber': new FormControl(this.editRestaurant.phoneNumber, [Validators.required, Validators.pattern('^[0-9]*$')])
    });
    this.validationCreated = true;
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    let editRestaurantChange = changes['editRestaurant'];
    if (!this.validationCreated && editRestaurantChange && editRestaurantChange.currentValue) {
      this.createFormValidation();
    }
  }
}
