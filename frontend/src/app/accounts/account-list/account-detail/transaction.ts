export class Transaction {
  public static deserialise(json: any): Transaction {
    return new Transaction(
      json.id,
      Date.parse(json.createdAt),
      json.accountId,
      +json.amount,
    );
  }

  constructor(
    public id: string,
    public createdAt: number,
    public accountId: string,
    public amount: number,
  ) {}
}
