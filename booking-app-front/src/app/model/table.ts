export interface ITable{
  restaurantId: number;
  id: number;
  maxPlaces: number;
  comment: string;
  identifier: string;
  toJson(): string;
}

export class Table implements ITable {

  restaurantId: number;
  id: number;
  maxPlaces: number;
  identifier: string = '';
  comment: string = '';

  constructor() { }

  public toJson(): string {
    return JSON.stringify({
      tableId: this.id,
      maxPlaces: this.maxPlaces,
      comment: this.comment,
      identifier: this.identifier
    });
  }


}
