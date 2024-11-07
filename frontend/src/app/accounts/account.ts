import { Transaction } from './account-list/account-detail/transaction';

export class Account {
  public static deserialise(json: any): Account {
    return new Account(
      json.id,
      json.customerId,
      +json.balance,
      Array.isArray(json.transactions)
        ? json.transactions.map(Transaction.deserialise)
        : [],
    );
  }

  constructor(
    public id: string,
    public customerId: string,
    public balance: number,
    public transactions: Transaction[],
  ) {}
}
