import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment.development';
import { UserDTO } from '../models/user.dto';

@Injectable({ providedIn: 'root' })
export class ConnectionsService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/connections`;

  /** Liste des relations d’un utilisateur */
  listForUser(userId: number): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.base}/${userId}`);
  }
}
