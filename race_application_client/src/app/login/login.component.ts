// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls:['./login.component.scss']
})
export class LoginComponent {
  constructor(private authService: AuthService, private router: Router) {
    // Preusmjeri ako je već ulogiran
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  login(role: 'APPLICANT' | 'ADMINISTRATOR') {
    let token: string;
    
    if (role === 'ADMINISTRATOR') {
      // Token za ADMINISTRATOR
      token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTklTVFJBVE9SIiwiZXhwIjo0NzQ4MTA2ODAwfQ.0XTwHBeiJ6VWqcRD1sinexwecfBSHSJX5Wgo_LkaCFU';
    } else {
      // Token za APPLICANT  
      token = 'eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTklTVFJBVE9SIiwiZXhwIjo0NzQ4MTA2ODAwLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGxpY2FudCIsInJvbGUiOiJBUFBMSUNBTlQiLCJleHAiOjQ3NDgxMDY4MDB9.dD6jpadmPs_7iDsPykiUq1MnYE3u_KGF7tE3mbDTkTI';
    }
    
    // Koristi AuthService umjesto direktno localStorage
    this.authService.setToken(token);
    this.router.navigate(['/dashboard']);
  }
}