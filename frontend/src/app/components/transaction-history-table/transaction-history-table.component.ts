import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TransactionDTO } from '../../models/transaction.dto';
import { TransactionService } from '../../services/transactions.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-transaction-history-table',
  imports: [CommonModule],
  templateUrl: './transaction-history-table.component.html',
  styleUrl: './transaction-history-table.component.scss',
})
export class TransactionHistoryTableComponent implements OnInit {
  transactions: TransactionDTO[] = [];
  loading = true;
  error: string | null = null;
  currentId!: number;

  constructor(
    private readonly txService: TransactionService,
    private readonly auth: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.auth.getUserId();
    if (id == null) {
      this.error = 'Utilisateur non identifié';
      this.loading = false;
      return;
    }
    this.currentId = id;
    this.fetch();
  }

  /** public pour que le parent puisse rafraîchir */
  public reload(): void {
    this.fetch();
  }

  private fetch(): void {
    this.loading = true;
    this.txService.listByUser(this.currentId).subscribe({
      next: (list) => {
        this.transactions = list;
        this.loading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement';
        this.loading = false;
      },
    });
  }

  relationLabel(tx: TransactionDTO): string {
    return tx.senderId === this.currentId
      ? `${tx.receiverUsername}`
      : `${tx.senderUsername}`;
  }
}
