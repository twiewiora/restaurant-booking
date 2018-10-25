import {Component, OnInit} from '@angular/core';
import {RestaurantInfoService} from "../../../service/restaurantInfo.service";
import {IRestaurant, Restaurant} from "../../../model/restaurant";
import {OpenHours, Weekday} from "../../../model/open-hours";
import {OpenHoursService} from "../../../service/open-hours.service";
import {cloneDeep} from "lodash";
import {NotificationsService} from "angular2-notifications";
import {ConfirmationDialogService} from "../../confirmation-dialog/confirmation-dialog.service";
import {ITable} from "../../../model/table";
import {ActivatedRoute} from "@angular/router";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

@Component({
  selector: 'app-restaurant-info-page',
  templateUrl: './restaurant-info-page.component.html',
  styleUrls: ['./restaurant-info-page.component.scss'],
  providers: [
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy},]
})
export class RestaurantInfoPageComponent implements OnInit {

  edit: boolean;
  create: boolean;

  openHoursIsLoaded;
  restaurantInfoIsLoaded;

  urlParams = {};

  validOpenHours;

  restaurant: Restaurant;
  editRestaurant: Restaurant;
  autocompleteItems: string[];
  autocompleteItemsPrices: string[];

  options = {
    position: 'middle',
    timeOut: 3000,
    animate: 'fade'
  };


  openHoursWeek: Map<Weekday, OpenHours>;
  editOpenHoursWeek: Map<Weekday, OpenHours>;

  constructor(private restaurantInfoService: RestaurantInfoService,
              private openHoursService: OpenHoursService,
              private notificationService: NotificationsService,
              private confirmationDialogService: ConfirmationDialogService,
              private location:  Location,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      this.edit = params['edit'] || false;
    });
    this.create = false;
    this.getTags();
    this.getPrices();
    this.getRestaurant();
    this.getOpeningHours();
  }

  setUrl(){
    let params = [];
    if(this.create){
      params.push(`create=${this.create}`);
    }
    if(this.edit){
      params.push(`edit=${this.edit}`);
    }
    this.location.go(this.activatedRoute.routeConfig.path, params.join('&'));
  }

  update(restaurant: Restaurant, openHoursWeek: Map<Weekday, OpenHours>) {
    this.updateOpenHours(openHoursWeek);
    this.updateRestaurant(restaurant)
  }

  updateOpenHours(updateOpenHoursWeek: Map<Weekday, OpenHours>) {
    if(OpenHours.toJson(updateOpenHoursWeek) != OpenHours.toJson(this.openHoursWeek)) {
      this.openHoursIsLoaded = false;
      this.openHoursService.updateOpenHours(OpenHours.toJson(updateOpenHoursWeek)).subscribe(any => {
        this.notificationService.success("Open hours updated", '', this.options);
        this.getOpeningHours();
      })
    }
  }

  getOpeningHours() {
    this.openHoursIsLoaded = false;
    this.openHoursService.getOpeningHoursForAllDays().subscribe(request => {
        this.openHoursWeek = OpenHours.fromJsonArray(request);
        this.editOpenHoursWeek = this.openHoursWeek;
        this.editOpenHoursWeek = <Map<Weekday, OpenHours>>cloneDeep(this.openHoursWeek);
        this.openHoursIsLoaded = true;
      },
      _ => {
        this.openHoursWeek = OpenHours.createNewOpenHoursWeek();
        this.editOpenHoursWeek = OpenHours.createNewOpenHoursWeek();
        this.openHoursIsLoaded = true;
      });
  }

  getRestaurant() {
    this.restaurantInfoIsLoaded = false;
    this.restaurantInfoService.getRestaurant().subscribe((response: IRestaurant) => {
        this.restaurant = <Restaurant> response;
        this.editRestaurant = <Restaurant> cloneDeep(this.restaurant);
        this.restaurantInfoIsLoaded = true;
      },
      _ => {
        this.edit = true;
        this.create = true;
        this.restaurant = new Restaurant();
        this.editRestaurant = new Restaurant();
        this.restaurantInfoIsLoaded = true;
      });
  }

  updateRestaurant(updateRestaurant: Restaurant) {
    if(Restaurant.toJson(this.restaurant) !== Restaurant.toJson(updateRestaurant)) {
      this.restaurantInfoIsLoaded = false;
      this.restaurantInfoService.updateRestaurant(updateRestaurant).subscribe(response => {
        this.notificationService.success("Restaurant information updated", '', this.options);
        this.getRestaurant();
      });
    }
  }

  createRestaurant(restaurant: IRestaurant) {
    this.restaurantInfoIsLoaded = false;
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

  getPrices() {
    this.restaurantInfoService.getPrices().subscribe(prices => {
      this.autocompleteItemsPrices = <string[]> prices;
    })
  }

  toggleEdit() {
    this.edit = !this.edit;
    this.setUrl();
  }

  launchCreate() {
    this.edit = false;
    this.create = false;
    this.createRestaurant(this.editRestaurant);
  }

  openHoursValid(isValid: boolean){
    this.validOpenHours = isValid;
  }

  public openConfirmationUpdateRestaurantInfoDialog() {
    this.confirmationDialogService.confirm('Update Restaurant Information', `Do you really want to update your restaurant information?`, 'SAVE', 'cancel', 'btn-success', 'btn-secondary')
      .then((confirmed) => {
        if (confirmed) {
          this.edit = false;
          this.update(this.editRestaurant, this.editOpenHoursWeek);
        }
      })
      .catch(() => {
      });
  }
}
