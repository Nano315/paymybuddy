import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

/** Empêche l’accès aux routes protégées si l’utilisateur n’est pas connecté */
export const authGuard: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isLoggedIn()) {
    return true; // accès OK
  }

  // Redirige vers /login et mémorise l’URL demandée
  return router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url },
  });
};
