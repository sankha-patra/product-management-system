import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CategoryService } from '../../../services/category.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatInputModule, MatButtonModule],
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent {
  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);
  public dialogRef = inject(MatDialogRef<CategoryFormComponent>);
  public data = inject(MAT_DIALOG_DATA);

  form: FormGroup = this.fb.group({
    name: [this.data?.name || '', Validators.required]
  });

  onSubmit() {
    if (this.form.valid) {
      const operation = this.data ? 
        this.categoryService.update(this.data.id, this.form.value) :
        this.categoryService.create(this.form.value);

      operation.subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => console.error(err)
      });
    }
  }
}
