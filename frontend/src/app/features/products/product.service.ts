import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import { API_BASE_URL } from '../../core/api/api.config';
import { Product, ProductRequest } from './product.models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly productUrl = `${API_BASE_URL}/products`;

  findAll() {
    return this.http.get<Product[]>(this.productUrl);
  }

  create(request: ProductRequest) {
    return this.http.post<Product>(this.productUrl, request);
  }

  update(id: number, request: ProductRequest) {
    return this.http.put<Product>(`${this.productUrl}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.productUrl}/${id}`);
  }
}
