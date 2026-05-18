import { CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import {
  PRODUCT_CATEGORY_OPTIONS,
  Product,
  ProductCategory,
  ProductRequest,
} from './product.models';
import { ProductService } from './product.service';

@Component({
  selector: 'app-product-home',
  imports: [CurrencyPipe, ReactiveFormsModule],
  templateUrl: './product-home.html',
  styleUrl: './product-home.css',
})
export class ProductHomeComponent {
  private readonly productService = inject(ProductService);

  protected readonly products = signal<Product[]>([]);
  protected readonly selectedProduct = signal<Product | null>(null);
  protected readonly isLoading = signal(false);
  protected readonly isSaving = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly successMessage = signal('');
  protected readonly categories = PRODUCT_CATEGORY_OPTIONS;

  protected readonly totalStock = computed(() =>
    this.products().reduce((total, product) => total + product.stockQuantity, 0),
  );

  protected readonly inventoryValue = computed(() =>
    this.products().reduce((total, product) => total + product.price * product.stockQuantity, 0),
  );

  protected readonly form = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(120)],
    }),
    brand: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(80)],
    }),
    sku: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(40)],
    }),
    category: new FormControl<ProductCategory>('LAPTOPS', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    price: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0.01)],
    }),
    stockQuantity: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0)],
    }),
    description: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(500)],
    }),
  });

  constructor() {
    this.loadProducts();
  }

  protected loadProducts() {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.productService
      .findAll()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (products) => this.products.set(products),
        error: () => this.errorMessage.set('Unable to load products.'),
      });
  }

  protected startCreate() {
    this.selectedProduct.set(null);
    this.successMessage.set('');
    this.errorMessage.set('');
    this.form.reset({
      name: '',
      brand: '',
      sku: '',
      category: 'LAPTOPS',
      price: 0,
      stockQuantity: 0,
      description: '',
    });
  }

  protected editProduct(product: Product) {
    this.selectedProduct.set(product);
    this.successMessage.set('');
    this.errorMessage.set('');
    this.form.setValue({
      name: product.name,
      brand: product.brand,
      sku: product.sku,
      category: product.category,
      price: product.price,
      stockQuantity: product.stockQuantity,
      description: product.description,
    });
  }

  protected saveProduct() {
    this.successMessage.set('');
    this.errorMessage.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const selectedProduct = this.selectedProduct();
    const request = this.formRequest();
    const saveAction = selectedProduct
      ? this.productService.update(selectedProduct.id, request)
      : this.productService.create(request);

    this.isSaving.set(true);
    saveAction.pipe(finalize(() => this.isSaving.set(false))).subscribe({
      next: (product) => {
        this.successMessage.set(selectedProduct ? 'Product updated.' : 'Product created.');
        this.selectedProduct.set(product);
        this.loadProducts();
      },
      error: () => this.errorMessage.set('Unable to save product. Check the SKU and required fields.'),
    });
  }

  protected deleteProduct(product: Product) {
    const confirmed = window.confirm(`Delete ${product.name}?`);

    if (!confirmed) {
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');

    this.productService.delete(product.id).subscribe({
      next: () => {
        this.successMessage.set('Product deleted.');
        if (this.selectedProduct()?.id === product.id) {
          this.startCreate();
        }
        this.loadProducts();
      },
      error: () => this.errorMessage.set('Unable to delete product.'),
    });
  }

  protected categoryLabel(category: ProductCategory) {
    return this.categories.find((option) => option.value === category)?.label ?? category;
  }

  private formRequest(): ProductRequest {
    const value = this.form.getRawValue();

    return {
      ...value,
      price: Number(value.price),
      stockQuantity: Number(value.stockQuantity),
    };
  }
}
