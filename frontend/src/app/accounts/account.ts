export class Account {
  public static deserialise(json: any): Account {
    return new Account(
      json.id,
      Date.parse(json.createdAt),
      json.customerId,
      +json.balance,
    );
  }

  constructor(
    public id: string,
    public createdAt: number,
    public customerId: string,
    public balance: number,
  ) {}
}
