import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { Account } from '../../account';
import { Transaction } from './transaction';
import { TransactionCreatorComponent } from './transaction-creator/transaction-creator.component';
import { TransactionService } from './transaction.service';
import { TransactionComponent } from './transaction/transaction.component';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [CommonModule, TransactionComponent, TransactionCreatorComponent],
  templateUrl: './account-detail.component.html',
  styleUrl: './account-detail.component.scss',
})
export class AccountDetailComponent implements OnInit {
  @Input() account: Account | null = null;

  public transactions: Transaction[] = [];

  constructor(private _transactionService: TransactionService) {}

  public ngOnInit(): void {
    if (this.account) {
      this._transactionService
        .getTransactionsForAccount(this.account?.id)
        .pipe(take(1))
        .subscribe((transactions: Transaction[]) => {
          this.transactions = transactions;
        });
    }
  }

  public onTransactionCreated(createdTransaction: Transaction): void {
    this.transactions.push(createdTransaction);
    if (this.account) {
      this.account.balance += createdTransaction.amount;
    }
  }
}
