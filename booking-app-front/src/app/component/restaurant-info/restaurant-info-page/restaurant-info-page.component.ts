import {Component, OnInit} from '@angular/core';
import {RestaurantInfoService} from "../../../service/restaurantInfo.service";
import {IRestaurant, Restaurant} from "../../../model/restaurant";
import {OpenHours, Weekday} from "../../../model/open-hours";
import {OpenHoursService} from "../../../service/open-hours.service";
import {cloneDeep} from "lodash";
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: 'app-restaurant-info-page',
  templateUrl: './restaurant-info-page.component.html',
  styleUrls: ['./restaurant-info-page.component.scss']
})
export class RestaurantInfoPageComponent implements OnInit {

  edit: boolean;
  create: boolean;

  validOpenHours;

  restaurant: Restaurant;
  editRestaurant: Restaurant;
  autocompleteItems: string[];

  options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };


  openHoursWeek: Map<Weekday, OpenHours>;
  editOpenHoursWeek: Map<Weekday, OpenHours>;

  constructor(private restaurantInfoService: RestaurantInfoService,
              private openHoursService: OpenHoursService,
              private notificationService: NotificationsService) {
  }

  ngOnInit() {
    this.edit = false;
    this.create = false;
    this.getTags();
    this.getRestaurant();
  }

  update(restaurant: Restaurant, openHoursWeek: Map<Weekday, OpenHours>) {
    this.updateOpenHours(openHoursWeek);
    this.updateRestaurant(restaurant)
  }

  updateOpenHours(updateOpenHoursWeek: Map<Weekday, OpenHours>) {
    if(OpenHours.toJson(updateOpenHoursWeek) != OpenHours.toJson(this.openHoursWeek)) {
      this.openHoursService.updateOpenHours(OpenHours.toJson(updateOpenHoursWeek)).subscribe(any => {
        this.notificationService.success("Open hours updated", '', this.options);
        this.getOpeningHours();
      })
    }
  }

  getOpeningHours() {
    this.openHoursService.getOpeningHoursForAllDays().subscribe(request => {
        this.openHoursWeek = OpenHours.fromJsonArray(request);
        this.editOpenHoursWeek = this.openHoursWeek;
        this.editOpenHoursWeek = <Map<Weekday, OpenHours>>cloneDeep(this.openHoursWeek);
      },
      _ => {
        this.openHoursWeek = OpenHours.createNewOpenHoursWeek();
        this.editOpenHoursWeek = OpenHours.createNewOpenHoursWeek();
      });
  }

  getRestaurant() {
    this.restaurantInfoService.getRestaurant().subscribe((response: IRestaurant) => {
        this.restaurant = <Restaurant> response;
        this.editRestaurant = <Restaurant> cloneDeep(this.restaurant);
        this.getOpeningHours();
      },
      _ => {
        this.edit = true;
        this.create = true;
        this.restaurant = new Restaurant();
        this.editRestaurant = new Restaurant();
      });
  }

  updateRestaurant(updateRestaurant: Restaurant) {
    debugger;
    if(Restaurant.toJson(this.restaurant) !== Restaurant.toJson(updateRestaurant)) {
      this.restaurantInfoService.updateRestaurant(updateRestaurant).subscribe(response => {
        this.notificationService.success("Restaurant information updated", '', this.options);
        this.getRestaurant();
      });
    }
  }

  createRestaurant(restaurant: IRestaurant) {
    this.restaurantInfoService.createRestaurant(restaurant).subscribe(response => {
      this.notificationService.success("Restaurant information created", '', this.options);
      this.getRestaurant();
    });
  }

  getTags() {
    this.restaurantInfoService.getTags().subscribe(tags => {
      this.autocompleteItems = <string[]> tags;
    })
  }

  toggleEdit() {
    this.edit = !this.edit;
  }

  save() {
    this.edit = false;
    this.update(this.editRestaurant, this.editOpenHoursWeek);
  }

  launchCreate() {
    this.edit = false;
    this.create = false;
    this.createRestaurant(this.editRestaurant);
  }

  openHoursValid(isValid: boolean){
    this.validOpenHours = isValid;
  }
}
