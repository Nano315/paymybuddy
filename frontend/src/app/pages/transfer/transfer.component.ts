import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';

import { TransactionHistoryTableComponent } from '../../components/transaction-history-table/transaction-history-table.component';
import { ConnectionsService } from '../../services/connections.service';
import { TransactionService } from '../../services/transactions.service';
import { AuthService } from '../../services/auth.service';
import { UserDTO } from '../../models/user.dto';
import { CreateTransactionRequest } from '../../models/create-transaction-request.model';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TransactionHistoryTableComponent,
  ],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.scss',
})
export class TransferComponent implements OnInit {
  @ViewChild('history') history!: TransactionHistoryTableComponent;

  private readonly fb = inject(FormBuilder);

  relations: UserDTO[] = [];
  loadingRelations = true;
  submitLoading = false;
  submitError: string | null = null;

  readonly form = this.fb.nonNullable.group({
    receiverEmail: ['', [Validators.required]],
    description: [''],
    amount: [
      null as number | null,
      [Validators.required, Validators.min(0.01)],
    ],
  });

  private currentUserId!: number;

  constructor(
    private readonly auth: AuthService,
    private readonly connService: ConnectionsService,
    private readonly txService: TransactionService
  ) {}

  ngOnInit(): void {
    const id = this.auth.getUserId();
    if (id == null) {
      this.submitError = 'Utilisateur non identifié';
      return;
    }
    this.currentUserId = id;

    this.connService.listForUser(id).subscribe({
      next: (list) => {
        this.relations = list;
        this.loadingRelations = false;
      },
      error: () => {
        this.loadingRelations = false;
        this.submitError = 'Impossible de charger vos relations';
      },
    });
  }

  pay(): void {
    if (this.form.invalid || this.submitLoading) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitError = null;
    this.submitLoading = true;

    const payload: CreateTransactionRequest = {
      senderId: this.currentUserId,
      receiverEmail: this.form.value.receiverEmail!, // vient du <select>
      amount: Number(this.form.value.amount),
      description: this.form.value.description ?? '',
    };

    this.txService.create(payload).subscribe({
      next: () => {
        this.submitLoading = false;
        this.form.reset(); // reset du formulaire
        this.history.reload(); // rafraîchit le tableau
      },
      error: (err) => {
        this.submitLoading = false;
        this.submitError = err?.error?.message ?? 'Échec du paiement';
      },
    });
  }
}
