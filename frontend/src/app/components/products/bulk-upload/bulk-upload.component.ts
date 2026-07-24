import { Component, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../services/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-bulk-upload',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatProgressBarModule, MatButtonModule],
  templateUrl: './bulk-upload.component.html',
  styleUrls: ['./bulk-upload.component.css']
})
export class BulkUploadComponent {
  private productService = inject(ProductService);
  private snackBar = inject(MatSnackBar);

  isDragging = false;
  uploading = false;

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.uploadFile(files[0]);
    }
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files && files.length > 0) {
      this.uploadFile(files[0]);
    }
  }

  uploadFile(file: File) {
    if (!file.name.endsWith('.csv')) {
      this.snackBar.open('Only CSV files are allowed', 'Close', { duration: 3000 });
      return;
    }

    this.uploading = true;
    this.productService.bulkUpload(file).subscribe({
      next: () => {
        this.uploading = false;
        this.snackBar.open('Upload successful', 'Close', { duration: 3000 });
      },
      error: (err) => {
        this.uploading = false;
        console.error(err);
        this.snackBar.open('Upload failed', 'Close', { duration: 3000 });
      }
    });
  }
}
