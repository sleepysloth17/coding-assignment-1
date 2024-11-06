import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Transaction } from '../transaction';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.scss',
})
export class TransactionComponent {
  @Input() transaction: Transaction | null = null;
}
