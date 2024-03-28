import { Component } from '@angular/core';
import  {Dieta} from '../dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-formulario-contacto',
  templateUrl: './formulario-contacto.component.html',
  styleUrls: ['./formulario-contacto.component.css']
})
export class FormularioContactoComponent {
  accion?: "Añadir" | "Editar";
  dieta: Dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: 0, alimentos: '', recomendaciones: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarDieta(): void {
    this.modal.close(this.dieta);
  }

}
