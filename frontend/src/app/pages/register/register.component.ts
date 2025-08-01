import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/register-request.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class RegisterComponent {
  /** Formulaire réactif */
  readonly form;

  submitting = false; // désactive le bouton pendant l’appel
  error: string | null = null; // message d’erreur éventuel

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router
  ) {
    this.form = this.fb.nonNullable.group({
      username: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  /** Soumission du formulaire */
  register(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched(); // force l’affichage des erreurs
      return;
    }

    this.submitting = true;
    this.error = null;

    const payload: RegisterRequest = this.form.getRawValue();

    this.auth.register(payload).subscribe({
      next: () => this.router.navigate(['/login']), // redirige après succès
      error: (err) => {
        this.error = err?.error?.message ?? 'Une erreur est survenue';
        this.submitting = false;
      },
    });
  }
}
