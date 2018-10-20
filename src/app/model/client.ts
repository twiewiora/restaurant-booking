export interface IClient {
  id: number;
  username: string;
  enabled: boolean;
}

export class Client implements IClient {

  id: number;
  username: string;
  enabled: boolean;
}
