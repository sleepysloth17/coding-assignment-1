export class Customer {
  public static deserialise(json: any): Customer {
    return new Customer(json.id, json.name, json.surname);
  }

  constructor(
    public id: string,
    public name: string,
    public surname: string,
  ) {}
}
