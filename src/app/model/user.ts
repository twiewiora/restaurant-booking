export class User {



  constructor(public username: string,
              public password: string) { }


  toJSON(): string{
    return JSON.stringify({
        username: this.username,
        password: this.password
      })
  }


}
