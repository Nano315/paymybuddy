import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment.development';
import { UserDTO } from '../models/user.dto';

export interface UpdateProfileRequest {
  username: string;
  email: string;
  /** Optionnel : si défini et non vide, remplace le mot de passe */
  password?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/users`;

  me(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.base}/me`);
  }

  updateMe(body: UpdateProfileRequest): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.base}/me`, body);
  }
}
