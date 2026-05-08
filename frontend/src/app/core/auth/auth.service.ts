import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

import { API_BASE_URL } from '../api/api.config';
import { LoginRequest, LoginResponse } from './auth.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly router = inject(Router);
  private readonly tokenKey = 'product_inventory_access_token';

  private readonly token = signal<string | null>(this.loadToken());

  readonly isAuthenticated = computed(() => Boolean(this.token()));

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(`${API_BASE_URL}/auth/login`, request).pipe(
      tap((response) => {
        this.storeToken(response.token);
      }),
    );
  }

  logout() {
    this.storeToken(null);
    void this.router.navigateByUrl('/login');
  }

  getToken() {
    return this.token();
  }

  private loadToken() {
    if (!isPlatformBrowser(this.platformId)) {
      return null;
    }

    return localStorage.getItem(this.tokenKey);
  }

  private storeToken(token: string | null) {
    this.token.set(token);

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (token) {
      localStorage.setItem(this.tokenKey, token);
      return;
    }

    localStorage.removeItem(this.tokenKey);
  }
}
