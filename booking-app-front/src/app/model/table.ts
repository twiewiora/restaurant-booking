export interface ITable{
  restaurantId: number;
  id: number;
  maxPlaces: number;
  comment: string;
  identifier: string;
  edit: boolean
  toJson(): string;
}

export class Table implements ITable {

  restaurantId: number;
  id: number;
  maxPlaces: number;
  identifier: string = '';
  comment: string = '';
  edit: boolean = false;

  constructor() { }

  public toJson(): string {
    return JSON.stringify({
      tableId: this.id,
      maxPlaces: this.maxPlaces,
      comment: this.comment,
      identifier: this.identifier
    });
  }


  static fromJsonToMap(json): Map<number, Table>{
    let tables: Map<number, Table> = new Map<number, Table>();
    json.forEach(jTable =>{
      let table: Table = new Table();
      table.identifier = jTable['identifier'] ?  jTable['identifier'] : '';
      table.id = jTable['id'];
      table.comment = jTable['comment'] ? jTable['comment'] : '';
      table.maxPlaces = jTable['maxPlaces'];
      tables[table.id] = table;
    });
    return tables;
  }


  static fromJsonToArray(json): Table[]{
    let tables: Table[] = [];
    json.forEach(jTable =>{
      let table: Table = new Table();
      table.identifier = jTable['identifier'] ?  jTable['identifier'] : '';
      table.id = jTable['id'];
      table.comment = jTable['comment'] ? jTable['comment'] : '';
      table.maxPlaces = jTable['maxPlaces'];
      tables.push(table);
    });
    return tables;
  }


}
