export type ProductCategory =
  | 'TV_AND_HOME_CINEMA'
  | 'LAPTOPS'
  | 'MONITORS'
  | 'MOBILE_PHONES'
  | 'KEYBOARDS'
  | 'MICE'
  | 'HEADPHONES'
  | 'STORAGE'
  | 'PRINTERS'
  | 'SMART_HOME';

export interface Product {
  id: number;
  name: string;
  brand: string;
  sku: string;
  category: ProductCategory;
  price: number;
  stockQuantity: number;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductRequest {
  name: string;
  brand: string;
  sku: string;
  category: ProductCategory;
  price: number;
  stockQuantity: number;
  description: string;
}

export const PRODUCT_CATEGORY_OPTIONS: Array<{ value: ProductCategory; label: string }> = [
  { value: 'TV_AND_HOME_CINEMA', label: 'TV and Home Cinema' },
  { value: 'LAPTOPS', label: 'Laptops' },
  { value: 'MONITORS', label: 'Monitors' },
  { value: 'MOBILE_PHONES', label: 'Mobile Phones' },
  { value: 'KEYBOARDS', label: 'Keyboards' },
  { value: 'MICE', label: 'Mice' },
  { value: 'HEADPHONES', label: 'Headphones' },
  { value: 'STORAGE', label: 'Storage' },
  { value: 'PRINTERS', label: 'Printers' },
  { value: 'SMART_HOME', label: 'Smart Home' },
];
