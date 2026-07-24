import { Component, inject, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { CategoryService } from '../../services/category.service';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatCardModule, MatIconModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private productService = inject(ProductService);

  totalCategories = 0;
  totalProducts = 0;

  ngOnInit() {
    this.categoryService.getAll().subscribe(cats => {
      this.totalCategories = cats.length;
    });
    this.productService.getProducts(0, 1, 'id', 'asc', '', '').subscribe(res => {
      this.totalProducts = res.totalElements || 0;
    });
  }
}
