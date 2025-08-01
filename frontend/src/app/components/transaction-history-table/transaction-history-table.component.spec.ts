import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionHistoryTableComponent } from './transaction-history-table.component';

describe('TransactionHistoryTableComponent', () => {
  let component: TransactionHistoryTableComponent;
  let fixture: ComponentFixture<TransactionHistoryTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionHistoryTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionHistoryTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
