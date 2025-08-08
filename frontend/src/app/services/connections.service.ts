import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
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

  /** Ajout d’une relation (retourne juste un code HTTP) */
  add(userId: number, friendEmail: string): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.base,
      { userId, friendEmail },
      { observe: 'response' }
    );
  }
}
