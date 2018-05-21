export interface ITable{
  restaurantId: number;
  id: number;
  maxPlaces: number;
  comment: string;
  toJson(): string;
}

export class Table implements ITable {

  restaurantId: number;
  id: number;
  maxPlaces: number;
  comment: string = '';

  constructor() { }

  public toJson(): string {
    return JSON.stringify({
      restaurantId: this.restaurantId,
      maxPlaces: this.maxPlaces
    });
  }


}
