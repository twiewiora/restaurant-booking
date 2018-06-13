export interface IRestaurant{
  restorerId: number;
  id: number;
  name: string;
  city: string;
  street: string;
  phoneNumber: number;
  tags: string[];
  toJson(): string;

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

  toJson(): string {
    return JSON.stringify({
      restorerId: this.restorerId,
      name: this.name,
      city: this.city,
      street: this.street,
      phoneNumber: this.phoneNumber,
      tags: this.tags
    });
  }

}
