import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment.development';
import { TransactionDTO } from '../models/transaction.dto';
import { CreateTransactionRequest } from '../models/create-transaction-request.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/transactions`;

  /** Historique d’un utilisateur */
  listByUser(userId: number): Observable<TransactionDTO[]> {
    return this.http.get<TransactionDTO[]>(`${this.base}/${userId}`);
  }

  /** Créer une transaction */
  create(data: CreateTransactionRequest): Observable<TransactionDTO> {
    return this.http.post<TransactionDTO>(this.base, data);
  }
}
