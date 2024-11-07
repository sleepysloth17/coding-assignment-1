import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Account } from '../../account';
import { TransactionCreatorComponent } from './transaction-creator/transaction-creator.component';
import { TransactionComponent } from './transaction/transaction.component';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [CommonModule, TransactionComponent, TransactionCreatorComponent],
  templateUrl: './account-detail.component.html',
  styleUrl: './account-detail.component.scss',
})
export class AccountDetailComponent {
  @Input() account: Account | null = null;
}
