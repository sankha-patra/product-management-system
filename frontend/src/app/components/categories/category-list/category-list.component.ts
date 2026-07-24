import { Component, inject, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CategoryService } from '../../../services/category.service';
import { CategoryFormComponent } from '../category-form/category-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatDialogModule],
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'name', 'actions'];
  dataSource: any[] = [];

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getAll().subscribe({
      next: (data) => this.dataSource = data,
      error: (err) => console.error(err)
    });
  }

  openDialog(category?: any) {
    const dialogRef = this.dialog.open(CategoryFormComponent, {
      width: '400px',
      data: category ? { ...category } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCategories();
        this.snackBar.open('Saved successfully', 'Close', { duration: 3000 });
      }
    });
  }

  deleteCategory(id: number) {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.delete(id).subscribe({
        next: () => {
          this.loadCategories();
          this.snackBar.open('Deleted successfully', 'Close', { duration: 3000 });
        },
        error: (err) => console.error(err)
      });
    }
  }
}
