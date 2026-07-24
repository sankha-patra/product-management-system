import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../services/product.service';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  public dialogRef = inject(MatDialogRef<ProductFormComponent>);
  public data = inject(MAT_DIALOG_DATA);

  categories: any[] = [];

  form: FormGroup = this.fb.group({
    name: [this.data?.name || '', Validators.required],
    image: [this.data?.image || ''],
    price: [this.data?.price || '', [Validators.required, Validators.min(0)]],
    categoryId: [this.data?.categoryId || '', Validators.required]
  });

  ngOnInit() {
    this.categoryService.getAll().subscribe(data => this.categories = data);
  }

  onSubmit() {
    if (this.form.valid) {
      const operation = this.data ? 
        this.productService.update(this.data.id, this.form.value) :
        this.productService.create(this.form.value);

      operation.subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => console.error(err)
      });
    }
  }
}
