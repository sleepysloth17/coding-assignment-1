import { Account } from '../accounts/account';

export class Customer {
  public static deserialise(json: any): Customer {
    return new Customer(
      json.id,
      json.name,
      json.surname,
      Array.isArray(json.accounts)
        ? json.accounts.map(Account.deserialise)
        : [],
    );
  }

  constructor(
    public id: string,
    public name: string,
    public surname: string,
    public accounts: Account[],
  ) {}
}
