import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Account } from './account';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private static readonly BASE_URL: string = `${environment.apiUrl}`;

  constructor(private _http: HttpClient) {}

  public createAccount(
    customerId: string,
    initialValue: number,
  ): Observable<Account | null> {
    return this._http
      .post(
        `${AccountService.BASE_URL}/customers/${customerId}/accounts`,
        {},
        { params: { initialValue } },
      )
      .pipe(
        map(Account.deserialise),
        catchError((err: any) => {
          console.error('Error in account creation: ', err);
          return of(null);
        }),
      );
  }
}
