import { Component } from '@angular/core';
import { TransactionHistoryTableComponent } from '../../components/transaction-history-table/transaction-history-table.component';

@Component({
  selector: 'app-transfer',
  imports: [TransactionHistoryTableComponent],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.scss'
})
export class TransferComponent {

}
