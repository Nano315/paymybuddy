import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';

import { ConnectionsService } from '../../services/connections.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-relations',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './relations.component.html',
  styleUrl: './relations.component.scss',
})
export class RelationsComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly connections = inject(ConnectionsService);

  submitting = false;
  success: string | null = null;
  error: string | null = null;
  private currentUserId!: number;

  readonly form = this.fb.nonNullable.group({
    friendEmail: ['', [Validators.required, Validators.email]],
  });

  ngOnInit(): void {
    const id = this.auth.getUserId();
    if (id == null) {
      this.error = 'Utilisateur non identifié';
      return;
    }
    this.currentUserId = id;
  }

  add(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.success = null;
    this.error = null;

    const email = this.form.value.friendEmail!;

    this.connections.add(this.currentUserId, email).subscribe({
      next: (res) => {
        // 201 attendu
        if (res.status === 201) {
          this.success = 'Relation ajoutée.';
        } else {
          this.success = `Terminé (code ${res.status}).`;
        }
        this.submitting = false;
        this.form.reset();
      },
      error: (err) => {
        const status = err?.status;
        // message générique; ajuste selon tes codes serveur (409, 400, etc.)
        this.error = status
          ? `Échec (code ${status}).`
          : 'Échec de la requête.';
        this.submitting = false;
      },
    });
  }
}
