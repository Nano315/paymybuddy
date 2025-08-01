import { Component } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  showHeader = false;

  constructor(private router: Router, private auth: AuthService) {
    this.router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe(() => this.toggleHeader());
  }

  private toggleHeader(): void {
    const path = this.router.url.split('?')[0];
    const isAuthPage = path === '/login' || path === '/register';
    this.showHeader = this.auth.isLoggedIn() && !isAuthPage;
  }
}
