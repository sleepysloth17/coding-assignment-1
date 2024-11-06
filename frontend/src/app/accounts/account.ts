export class Account {
  public static deserialise(json: any): Account {
    return new Account(json.id, json.customerId, +json.balance);
  }

  constructor(
    public id: string,
    public customerId: string,
    public balance: number,
  ) {}
}
