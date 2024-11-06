import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestingModule } from '../../../../../testing.module.spec';
import { TransactionCreatorComponent } from './transaction-creator.component';

describe('TransactionCreatorComponent', () => {
  let component: TransactionCreatorComponent;
  let fixture: ComponentFixture<TransactionCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionCreatorComponent, TestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(TransactionCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
