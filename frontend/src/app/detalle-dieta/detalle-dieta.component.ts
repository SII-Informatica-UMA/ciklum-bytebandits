import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Dieta } from '../dieta';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioDietaComponent} from '../formulario-dieta/formulario-dieta.component'
import { UsuariosService } from '../services/usuarios.service';
import { Rol } from '../entities/login';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detalle-dieta',
  templateUrl: './detalle-dieta.component.html',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule, DetalleDietaComponent],
  styleUrls: ['./detalle-dieta.component.css']
})
export class DetalleDietaComponent {
  @Input() dieta?: Dieta;
  @Output() dietaEditada = new EventEmitter<Dieta>();
  @Output() dietaEliminada = new EventEmitter<number>();

  constructor(private modalService: NgbModal, private usuarioService: UsuariosService) { }

  get usuarioSesion() {
    return this.usuarioService.getUsuarioSesion();
  }

  private get rol() {
    return this.usuarioService.rolCentro;
  }

  isAdministrador(): boolean {
    console.log("Pregunta admin: "+this.rol);
    return this.rol?.rol == Rol.ADMINISTRADOR;
  } 

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
