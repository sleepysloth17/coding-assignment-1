import { Injectable } from '@angular/core';
import { Transaction } from './transaction';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  public getTransactionsForAccount(accountId: string): Promise<Transaction[]> {
    return Promise.resolve([
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
      { id: '', accountId, amount: 10 },
    ]);
  }

  public createTransaction(
    accountId: string,
    amount: number,
  ): Promise<Transaction> {
    return Promise.resolve({ id: '', accountId, amount });
  }
}
