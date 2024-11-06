export class Customer {
  public static deserialise(json: any): Customer {
    return new Customer(
      json.id,
      Date.parse(json.createdAt),
      json.name,
      json.surname,
    );
  }

  constructor(
    public id: string,
    public createdAt: number,
    public name: string,
    public surname: string,
  ) {}
}
