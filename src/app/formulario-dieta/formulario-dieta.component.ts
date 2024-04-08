import { Component } from '@angular/core';
import { Dieta } from '../dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formulario-dieta',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './formulario-dieta.component.html',
  styleUrls: ['./formulario-dieta.component.css']
})
export class FormularioDietaComponent {
  accion?: "Añadir" | "Editar";
  dieta: Dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: '', alimentos: '', recomendaciones: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarDieta(): void {
    this.modal.close(this.dieta);
  }

}
