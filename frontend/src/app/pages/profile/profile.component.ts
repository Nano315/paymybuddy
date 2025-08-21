import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {
  UserService,
  UpdateProfileRequest,
} from '../../services/users.service';
import { UserDTO } from '../../models/user.dto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  loading = true;
  saving = false;
  editing = false;
  error: string | null = null;

  user!: UserDTO;
  initialEmail!: string;

  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    password: [''],
  });

  ngOnInit(): void {
    this.userService.me().subscribe({
      next: (u) => {
        this.user = u;
        this.initialEmail = u.email;
        this.form.patchValue({
          username: u.username,
          email: u.email,
          password: '',
        });
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger le profil';
        this.loading = false;
      },
    });
  }

  /** Bouton unique : 1er clic => passe en édition ; 2e clic => enregistre */
  onEditOrSave(): void {
    if (!this.editing) {
      this.editing = true;
      return;
    }
    this.save();
  }

  private save(): void {
    if (this.form.invalid || this.saving) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving = true;
    this.error = null;

    const body = {
      username: this.form.value.username!,
      email: this.form.value.email!,
      // n'envoie pas le mdp s'il est vide
      password: this.form.value.password?.trim()
        ? this.form.value.password
        : undefined,
    };

    const passwordChanged = (body.password?.trim()?.length ?? 0) > 0;

    this.userService.updateMe(body).subscribe({
      next: (updated) => {
        this.saving = false;

        // compare avec ce que le serveur a vraiment enregistré
        const oldEmail = this.initialEmail?.trim().toLowerCase() ?? '';
        const newEmail = updated.email?.trim().toLowerCase() ?? '';
        const emailChangedOnServer = oldEmail !== newEmail;

        if (emailChangedOnServer || passwordChanged) {
          this.auth.logout(); // clear token
          this.router.navigate(['/login']); // force reconnexion
          return;
        }

        // username seulement -> pas de déconnexion
        this.user = updated;
        this.initialEmail = updated.email;
        this.form.patchValue({ password: '' });
        this.editing = false;
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'Erreur lors de la mise à jour';
        this.saving = false;
      },
    });
  }
}
