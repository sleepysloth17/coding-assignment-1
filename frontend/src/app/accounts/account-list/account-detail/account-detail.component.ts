import { Component, Input } from '@angular/core';
import { Account } from '../../account';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [],
  templateUrl: './account-detail.component.html',
  styleUrl: './account-detail.component.scss',
})
export class AccountDetailComponent {
  @Input() account: Account | null = null;
}
