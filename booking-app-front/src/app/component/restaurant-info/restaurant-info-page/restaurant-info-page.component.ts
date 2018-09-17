import {Component, OnInit} from '@angular/core';
import {RestaurantInfoService} from "../../../service/restaurantInfo.service";
import {IRestaurant} from "../../../model/restaurant";
import {Action} from "../restaurant-info/restaurant-info.component";

@Component({
  selector: 'app-restaurant-info-page',
  templateUrl: './restaurant-info-page.component.html',
  styleUrls: ['./restaurant-info-page.component.scss']
})
export class RestaurantInfoPageComponent implements OnInit {

  restaurant: IRestaurant;
  edit: boolean = false;
  action: Action = Action.Update;

  constructor(private restaurantInfoService: RestaurantInfoService) {
  }

  ngOnInit() {
    this.getRestaurant();
  }

  getRestaurant() {
    this.restaurantInfoService.getRestaurant().subscribe(result => {
      this.restaurant = result;
    });
  }

  toggleEdit() {
    this.edit = !this.edit;
  }
}
