import { Component, inject, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CategoryService } from '../../../services/category.service';
import { ProductFormComponent } from '../product-form/product-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatSortModule, MatButtonModule, MatIconModule, MatInputModule, MatSelectModule, MatDialogModule, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['image', 'name', 'categoryName', 'price', 'actions'];
  dataSource: any[] = [];
  categories: any[] = [];
  
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  sortBy = 'id';
  sortDir = 'asc';
  search = '';
  categoryId = '';

  ngOnInit() {
    this.loadCategories();
    this.loadProducts();
  }

  loadCategories() {
    this.categoryService.getAll().subscribe(data => this.categories = data);
  }

  loadProducts() {
    this.productService.getProducts(this.pageIndex, this.pageSize, this.sortBy, this.sortDir, this.search, this.categoryId)
      .subscribe({
        next: (res) => {
          this.dataSource = res.content || [];
          this.totalElements = res.totalElements || 0;
        },
        error: (err) => console.error(err)
      });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadProducts();
  }

  onSortChange(event: Sort) {
    this.sortBy = event.active;
    this.sortDir = event.direction || 'asc';
    this.loadProducts();
  }

  onSearchChange() {
    this.pageIndex = 0;
    this.loadProducts();
  }

  onCategoryFilterChange() {
    this.pageIndex = 0;
    this.loadProducts();
  }

  openDialog(product?: any) {
    const dialogRef = this.dialog.open(ProductFormComponent, {
      width: '500px',
      data: product ? { ...product } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
        this.snackBar.open('Saved successfully', 'Close', { duration: 3000 });
      }
    });
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.delete(id).subscribe({
        next: () => {
          this.loadProducts();
          this.snackBar.open('Deleted successfully', 'Close', { duration: 3000 });
        },
        error: (err) => console.error(err)
      });
    }
  }

  downloadReport(format: string) {
    this.productService.downloadReport(format);
  }
}
