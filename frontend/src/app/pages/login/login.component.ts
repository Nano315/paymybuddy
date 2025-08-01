import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/login-request.model';
import { AuthResponse } from '../../models/auth-response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class LoginComponent {
  readonly form;
  submitting = false;
  error: string | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.form = this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  login(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.error = null;

    const payload: LoginRequest = this.form.getRawValue();

    this.auth.login(payload).subscribe({
      next: (res: AuthResponse) => {
        /** Stocke le JWT pour les appels suivants */
        localStorage.setItem('token', res.token);
        /** Redirige vers la page demandé ou Transfert par defaut */
        const returnUrl =
          this.route.snapshot.queryParamMap.get('returnUrl') ?? '/transfer';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'Identifiants invalides';
        this.submitting = false;
      },
    });
  }
}
