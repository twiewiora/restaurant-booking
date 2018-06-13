import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IRestaurant, Restaurant} from "../../../model/restaurant";
import {RestaurantInfoService} from "../../../service/restaurantInfo.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-restaurant-info',
  templateUrl: './restaurant-info.component.html',
  styleUrls: ['./restaurant-info.component.scss']
})
export class RestaurantInfoComponent implements OnInit {

  restaurant: IRestaurant;
  @Input() edit: boolean;

  constructor(private restaurantInfoService: RestaurantInfoService,
              private router: Router) {
  }

  ngOnInit() {
    this.getRestaurant();
  }

  getRestaurant(){
    this.restaurantInfoService.getRestaurant().subscribe( response => {
      this.restaurant = <Restaurant> response;
    },
      _ => {
      this.restaurant = new Restaurant();
      });
  }

  updateRestaurant(restaurant: IRestaurant){
    this.restaurantInfoService.updateRestaurant(restaurant).subscribe(response => {
      this.getRestaurant();
    });
  }


  createRestaurant(restaurant: IRestaurant){
    this.restaurantInfoService.createRestaurant(restaurant).subscribe(response => {
      localStorage.setItem('rest', 'ok');
      this.router.navigate(["/info"])
    });
  }
}
