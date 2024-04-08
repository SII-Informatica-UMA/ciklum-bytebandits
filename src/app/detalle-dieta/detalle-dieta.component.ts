import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Dieta } from '../dieta';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioDietaComponent} from '../formulario-dieta/formulario-dieta.component'


@Component({
  selector: 'app-detalle-dieta',
  templateUrl: './detalle-dieta.component.html',
  standalone: true,
  styleUrls: ['./detalle-dieta.component.css']
})
export class DetalleDietaComponent {
  @Input() dieta?: Dieta;
  @Output() dietaEditada = new EventEmitter<Dieta>();
  @Output() dietaEliminada = new EventEmitter<number>();

  constructor(private modalService: NgbModal) { }

  editarDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.dieta = {...this.dieta};
    ref.result.then((dieta: Dieta) => {
      this.dietaEditada.emit(dieta);
    }, (reason) => {});
  }

  eliminarDieta(): void {
    this.dietaEliminada.emit(this.dieta?.id);
  }
}
