import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { RegisterRequest } from '../models/register-request.model';
import { LoginRequest } from '../models/login-request.model';
import { UserDTO } from '../models/user.dto';
import { AuthResponse } from '../models/auth-response.model';
import { environment } from '../../environments/environment.development';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  /** URL de base de l'API */
  private readonly baseUrl = environment.apiUrl;

  /** Inscription */
  register(data: RegisterRequest): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.baseUrl}/auth/register`, data);
  }

  /** Connexion */
  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, data);
  }

  /** Retourne true si un JWT valide est présent */
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // pour l’instant : existence ­≥ connexion
    /* plus tard : décoder le JWT et vérifier la date d’expiration */
  }

  /** Déconnexion */
  logout(): void {
    localStorage.removeItem('token');
  }
}
