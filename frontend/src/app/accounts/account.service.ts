import { Injectable } from '@angular/core';
import { Account } from './account';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  public createAccount(
    customerId: string,
    initialValue: number,
  ): Promise<Account> {
    return Promise.resolve({ id: '', customerId, balance: initialValue });
  }

  public getAccounts(customerId: string): Promise<Account[]> {
    return Promise.resolve(
      new Array(3).fill(null).map((_, i) => ({
        id: '' + i,
        customerId,
        balance: 3000,
      })),
    );
  }
}
