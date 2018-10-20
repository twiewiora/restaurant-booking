export interface IRestaurant {
  restorerId: number;
  id: number;
  name: string;
  city: string;
  street: string;
  streetNumber: string;
  website: string;
  phoneNumber: number;
  tags: string[];
  restaurantPrice: string[];

}


export class Restaurant implements IRestaurant {

  restorerId: number;
  id: number;
  name: string = '';
  city: string = '';
  street: string = '';
  streetNumber: string = '';
  website: string = '';
  phoneNumber: number;
  tags: string[] = [];
  restaurantPrice: string[] = [];

  constructor() {
  }

  static toJson(restaurant: IRestaurant): string {
    return JSON.stringify({
      name: restaurant.name,
      city: restaurant.city,
      street: restaurant.street,
      streetNumber: restaurant.streetNumber,
      website: restaurant.website,
      phoneNumber: restaurant.phoneNumber,
      tags: restaurant.tags,
      restaurantPrice: restaurant.restaurantPrice
    });
  }

}
