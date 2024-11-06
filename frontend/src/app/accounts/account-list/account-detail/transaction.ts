export class Transaction {
  public static deserialise(json: any): Transaction {
    return new Transaction(json.id, json.accountId, +json.amount);
  }

  constructor(
    public id: string,
    public accountId: string,
    public amount: number,
  ) {}
}
