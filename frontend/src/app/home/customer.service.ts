import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Customer } from './customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private static readonly BASE_URL: string = `${environment.apiUrl}/customers`;

  constructor(private _http: HttpClient) {}

  public createCustomer(
    name: string,
    surname: string,
  ): Observable<Customer | null> {
    return this._http
      .post(`${CustomerService.BASE_URL}`, {}, { params: { name, surname } })
      .pipe(
        map(Customer.deserialise),
        catchError((err: any) => {
          console.error('Error in customer creation: ', err);
          return of(null);
        }),
      );
  }

  public getCustomers(): Observable<Customer[]> {
    return this._http.get<any[]>(`${CustomerService.BASE_URL}`).pipe(
      map((json: any[]) => json.map(Customer.deserialise)),
      catchError((err: any) => {
        console.error('Error fetching customers: ', err);
        return of([]);
      }),
    );
  }
}
