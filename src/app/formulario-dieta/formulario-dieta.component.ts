import { Component } from '@angular/core';
import { Dieta } from '../dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { usuariosC } from '../services/backend.fake.service';
import { Usuario } from '../entities/usuario';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-formulario-dieta',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-dieta.component.html',
  styleUrls: ['./formulario-dieta.component.css']
})
export class FormularioDietaComponent {

  usuarios: Usuario [] = [];

  accion?: "Añadir" | "Editar";
  dieta: Dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: '', alimentos: '', recomendaciones: '', usuarioAsociado: {id: 0,
    nombre: '',
    apellido1: '',
    apellido2: '',
    email: '',
    administrador: false,
    password: ''}};


  constructor(private usuariosService: UsuariosService,public modal: NgbActiveModal) {
    this.actualizarUsuarios();

   }

  guardarDieta(): void {
    this.modal.close(this.dieta);
    console.log(this.usuarios);
  }

  getUsuarios(){
    this.actualizarUsuarios();

    return this.usuarios;
  }

 

  actualizarUsuarios() {
    this.usuariosService.getUsuarios().subscribe(usuarios => {
      this.usuarios = usuarios;
    });
  }
}
