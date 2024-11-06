import { NgModule } from '@angular/core';
import { TransactionService } from './app/accounts/account-list/account-detail/transaction.service';
import { AccountService } from './app/accounts/account.service';
import { CustomerService } from './app/home/customer.service';

const getMock = (c: any) => {
  return jasmine.createSpyObj(Object.getOwnPropertyNames(c.prototype));
};

@NgModule({
  providers: [
    {
      provide: CustomerService,
      useFactory: () => getMock(CustomerService),
    },
    {
      provide: AccountService,
      useFactory: () => getMock(AccountService),
    },
    {
      provide: TransactionService,
      useFactory: () => getMock(TransactionService),
    },
  ],
})
export class TestingModule {}
