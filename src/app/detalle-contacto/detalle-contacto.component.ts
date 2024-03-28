import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Dieta } from '../dieta';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactoComponent} from '../formulario-contacto/formulario-contacto.component'
import { DietaService } from '../dieta.service';

@Component({
  selector: 'app-detalle-contacto',
  templateUrl: './detalle-contacto.component.html',
  styleUrls: ['./detalle-contacto.component.css']
})
export class DetalleContactoComponent {
  @Input() dieta?: Dieta;
  @Output() dietaEditada = new EventEmitter<Dieta>();
  @Output() dietaEliminada = new EventEmitter<number>();

  constructor(private dietaService: DietaService, private modalService: NgbModal) { }

  editarDieta(): void {
    let ref = this.modalService.open(FormularioContactoComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.contacto = {...this.dieta};
    ref.result.then((dieta: Dieta) => {
      this.dietaEditada.emit(dieta);
    }, (reason) => {});
  }

  eliminarDieta(): void {
    this.dietaEliminada.emit(this.dieta?.id);
  }
}
