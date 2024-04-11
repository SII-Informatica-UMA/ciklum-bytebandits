import { Component, OnInit } from '@angular/core';
import {Dieta } from '../dieta';
import { DietaService } from '../dieta.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioDietaComponent} from '../formulario-dieta/formulario-dieta.component'
import {DetalleDietaComponent} from '../detalle-dieta/detalle-dieta.component'
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../services/usuarios.service';
import { Rol } from '../entities/login';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule, DetalleDietaComponent],
  templateUrl: './dieta-principal.component.html',
  styleUrls: ['./dieta-principal.component.css']
})

export class DietaPrincipalComponent {
  dieta: Dieta [] = [];
  dietaElegida?: Dieta; 

  constructor(private dietaService: DietaService, private usuarioService: UsuariosService, private modalService: NgbModal) { }

  ngOnInit(): void {

    const usuarioActual = this.usuarioService.getUsuarioSesion();

    //this.dieta = this.dietaService.getDieta();
    if(usuarioActual&& this.isAdministrador()){

      this.dieta = this.dietaService.getDieta();

    }
    else if (usuarioActual) {
      this.dieta = this.dietaService.getDieta().filter(dieta => dieta.idCliente === usuarioActual.id);
    }

  }

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

  elegirDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  aniadirDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: null, alimentos: [], recomendaciones: '',idCliente: null, idEntrenador: null};
    ref.result.then((dieta: Dieta) => {
      this.dietaService.addDieta(dieta);
      this.dieta = this.dietaService.getDieta();
    }, (reason) => {});

  }
  dietaEditada(dieta: Dieta): void {
    this.dietaService.editarDieta(dieta);
    this.dieta = this.dietaService.getDieta();
    this.dietaElegida = this.dieta.find(c => c.id == dieta.id);
  }

  eliminarDieta(id: number): void {
    this.dietaService.eliminarDieta(id);
    this.dieta = this.dietaService.getDieta();
    this.dietaElegida = undefined;
  }
}

