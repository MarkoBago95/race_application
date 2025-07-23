import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service'; // prilagodi put ako treba

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log('AuthGuard check');

    if (this.auth.getToken()) {
      return true;
    }

    // Ako nije prijavljen, idi na login
    this.router.navigate(['/login']);
    return false;
  }
}
