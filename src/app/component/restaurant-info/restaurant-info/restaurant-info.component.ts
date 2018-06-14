import {Component, Input, OnInit} from '@angular/core';
import {IRestaurant, Restaurant} from "../../../model/restaurant";
import {RestaurantInfoService} from "../../../service/restaurantInfo.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-restaurant-info',
  templateUrl: './restaurant-info.component.html',
  styleUrls: ['./restaurant-info.component.scss']
})
export class RestaurantInfoComponent implements OnInit {

  restaurant: Restaurant;
  @Input() edit: boolean;
  @Input() action: Action;
  public autocompleteItems: string[] = [];

  constructor(private restaurantInfoService: RestaurantInfoService,
              private router: Router) {
  }

  ngOnInit() {
    this.getRestaurant();
    this.getTags();
  }

  getRestaurant() {
    this.restaurantInfoService.getRestaurant().subscribe((response: IRestaurant) => {
        this.restaurant = <Restaurant> response;
      },
      _ => {
        this.restaurant = new Restaurant();
      });
  }

  updateRestaurant(restaurant: IRestaurant) {
    this.restaurantInfoService.updateRestaurant(restaurant).subscribe(response => {
      this.getRestaurant();
    });
  }


  createRestaurant(restaurant: IRestaurant) {
    this.restaurantInfoService.createRestaurant(restaurant).subscribe(response => {
      localStorage.setItem('rest', 'ok');
      this.router.navigate(["/info"])
    });
  }

  getTags(){
    this.restaurantInfoService.getTags().subscribe(tags =>{
      this.autocompleteItems = <string[]> tags;
    })
  }
}

export enum Action {
  Update = 0,
  Create = 1
}
