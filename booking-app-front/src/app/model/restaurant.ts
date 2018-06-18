export interface IRestaurant{
  restorerId: number;
  id: number;
  name: string;
  city: string;
  street: string;
  phoneNumber: number;
  tags: string[];

}


export class Restaurant implements IRestaurant {

  restorerId: number;
  id: number;
  name: string = '';
  city: string = '';
  street: string = '';
  phoneNumber: number;
  tags: string[] = [];

  constructor() { }

  static toJson(restaurant: IRestaurant): string {
    return JSON.stringify({
      name: restaurant.name,
      city: restaurant.city,
      street: restaurant.street,
      phoneNumber: restaurant.phoneNumber,
      tags: restaurant.tags
    });
  }

}
