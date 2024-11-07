import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Transaction } from './transaction';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private static readonly BASE_URL: string = `${environment.apiUrl}/accounts`;

  constructor(private _http: HttpClient) {}

  public createTransaction(
    accountId: string,
    amount: number,
  ): Observable<Transaction | null> {
    return this._http
      .post(
        `${TransactionService.BASE_URL}/${accountId}/transactions`,
        {},
        { params: { amount } },
      )
      .pipe(
        map(Transaction.deserialise),
        catchError((err: any) => {
          console.error('Error in transaction creation: ', err);
          return of(null);
        }),
      );
  }
}
